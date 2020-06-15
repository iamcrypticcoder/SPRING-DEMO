package com.iamcrypticcoder.mongodbexample.persistence.repositories.impl;

import com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.PostComment;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.CustomPostCommentBucketRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel.MAX_BUCKET_SIZE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CustomPostCommentBucketRepositoryImpl implements CustomPostCommentBucketRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    static class CommentCount {
        int count;
    }

    @Override
    public Optional<PostCommentBucketModel> findLatestBucketByPostId(String postId) {
        Query query = new Query();
        query.addCriteria(where("postId").is(postId));
        query.with(Sort.by("bucketIndex").descending());
        query.limit(1);

        PostCommentBucketModel result = mongoTemplate.findOne(query, PostCommentBucketModel.class);

        return Optional.ofNullable(result);
    }

    public boolean addComment(String postId, PostComment comment) {
        Optional<PostCommentBucketModel> foundBucket = findLatestBucketByPostId(postId);

        if (!foundBucket.isPresent()) {
            foundBucket = Optional.ofNullable(mongoTemplate.insert(new PostCommentBucketModel(postId, 0L)));
        } else if (foundBucket.get().getBucket().size() == MAX_BUCKET_SIZE) {
            long nextBucketIndex = foundBucket.get().getBucketIndex() + 1L;
            foundBucket = Optional.ofNullable(mongoTemplate.insert(new PostCommentBucketModel(postId, nextBucketIndex)));
        }

        if (!foundBucket.isPresent()) return false;

        PostCommentBucketModel bucket = foundBucket.get();
        comment.createCommentId(bucket.getId());
        if (null == comment.getCreatedDate()) comment.setCreatedDate(LocalDateTime.now());
        if (null == comment.getUpdatedDate()) comment.setUpdatedDate(LocalDateTime.now());

        return addCommentInBucket(bucket.getId(), comment);
    }

    public boolean updateComment(PostComment item) {
        String bucketId = item.getCommentId().split("_")[0];
        return updateCommentInBucket(bucketId, item);
    }

    public boolean deleteComment(PostComment item) {
        String bucketId = item.getCommentId().split("_")[0];
        return deleteCommentFromBucket(bucketId, item);
    }

    public boolean addCommentListInBucket(String bucketId, List<PostComment> list) {
        Query query = new Query().addCriteria(where("_id").is(bucketId));

        Update update = new Update().push("bucket").each(list);

        UpdateResult res = mongoTemplate.updateFirst(query, update, PostCommentBucketModel.class);
        return res.getModifiedCount() > 0L;
    }

    public int findCommentCount(String postId) {
        MatchOperation matchOperation = Aggregation.match(where("postId").is(postId));
        GroupOperation groupOperation = group().sum(ArrayOperators.Size.lengthOfArray("bucket")).as("count");

        Aggregation agg = newAggregation(matchOperation, groupOperation);
        AggregationResults<CommentCount> result = mongoTemplate.aggregate(agg, "POST_COMMENT_BUCKET", CommentCount.class);

        return result.getMappedResults().get(0).count;
    }

    public List<PostComment> findAllOldComments(String postId, int limit) {
        return findAllCommentsByPostId(postId, "old", limit);
    }

    public List<PostComment> findAllLatestComments(String postId, int limit) {
        return findAllCommentsByPostId(postId, "latest", limit);
    }

    public List<PostComment> findAllCommentsAfter(PostComment comment, int limit) {
        String bucketId = comment.getCommentId().split("_")[0];

        PostCommentBucketModel curBucket = mongoTemplate.findOne(Query.query(where("_id").is(bucketId)), PostCommentBucketModel.class);

        if (null == curBucket) return new ArrayList<>();

        List<PostComment> retList = new ArrayList<>();

        curBucket.getBucket().forEach(item -> {
            if (retList.size() == limit) return;
            if (item.getCreatedDate().isAfter(comment.getCreatedDate()))
                retList.add(item);
        });
        if (retList.size() == limit) return retList;

        Query query = Query
                .query(where("bucketIndex").gt(curBucket.getBucketIndex()))
                .addCriteria(where("postId").is(curBucket.getPostId()))
                .with(Sort.by(Sort.Direction.ASC, "bucketIndex"))
                .limit((limit / PostCommentBucketModel.MAX_BUCKET_SIZE) + 1);

        List<PostCommentBucketModel> bucketList = mongoTemplate.find(query, PostCommentBucketModel.class);

        for (PostCommentBucketModel bucket : bucketList) {
            if (retList.size() == limit) break;

            if (limit - retList.size() >= bucket.getBucket().size())
                retList.addAll(bucket.getBucket());
            else
                retList.addAll(bucket.getBucket().subList(0, limit - retList.size()));
        }

        return retList;
    }

    public List<PostComment> findAllCommentsBefore(PostComment comment, int limit) {
        String bucketId = comment.getCommentId().split("_")[0];

        PostCommentBucketModel curBucket = mongoTemplate.findOne(Query.query(where("_id").is(bucketId)), PostCommentBucketModel.class);

        if (null == curBucket) return new ArrayList<>();

        List<PostComment> retList = new ArrayList<>();

        Collections.reverse(curBucket.getBucket());
        curBucket.getBucket().forEach(item -> {
            if (retList.size() == limit) return;
            if (item.getCreatedDate().isBefore(comment.getCreatedDate()))
                retList.add(item);
        });
        if (retList.size() == limit) return retList;

        Query query = Query
                .query(where("bucketIndex").lt(curBucket.getBucketIndex()))
                .addCriteria(where("postId").is(curBucket.getPostId()))
                .with(Sort.by(Sort.Direction.DESC, "bucketIndex"))
                .limit((limit / PostCommentBucketModel.MAX_BUCKET_SIZE) + 1);

        List<PostCommentBucketModel> bucketList = mongoTemplate.find(query, PostCommentBucketModel.class);

        for (PostCommentBucketModel bucket : bucketList) {
            if (retList.size() == limit) break;

            Collections.reverse(bucket.getBucket());

            if (limit - retList.size() >= bucket.getBucket().size())
                retList.addAll(bucket.getBucket());
            else
                retList.addAll(bucket.getBucket().subList(0, limit - retList.size()));
        }

        return retList;
    }

    private boolean addCommentInBucket(String bucketId, PostComment item) {
        Query query = new Query().addCriteria(where("_id").is(bucketId));

        Update update = new Update().push("bucket", item);

        UpdateResult res = mongoTemplate.updateFirst(query, update, PostCommentBucketModel.class);
        return res.getModifiedCount() > 0L;
    }

    private boolean updateCommentInBucket(String bucketId, PostComment item) {
        Query query = new Query()
                .addCriteria(where("_id").is(bucketId))
                .addCriteria(where("bucket.commentId").is(item.getCommentId()));
        Update update = new Update()
                .set("bucket.$.text", item.getText())
                .set("bucket.$.updatedDate", LocalDateTime.now());

        UpdateResult res = mongoTemplate.updateFirst(query, update, PostCommentBucketModel.class);
        return res.getModifiedCount() > 0L;
    }

    private boolean deleteCommentFromBucket(String bucketId, PostComment item) {
        Query query = new Query().addCriteria(where("_id").is(bucketId));

        Update update = new Update().pull("bucket", new PostComment().setCommentId(item.getCommentId()));

        UpdateResult res = mongoTemplate.updateFirst(query, update, PostCommentBucketModel.class);
        return res.getModifiedCount() > 0L;
    }

    private List<PostComment> findAllCommentsByPostId(String postId, String oldOLatest, int limit) {
        Query query = Query.query(where("postId").is(postId));
        query.with(Sort.by(oldOLatest.equals("latest") ? Sort.Direction.DESC : Sort.Direction.ASC, "bucketIndex"));

        List<PostCommentBucketModel> bucketModelList = mongoTemplate.find(query, PostCommentBucketModel.class);

        List<PostComment> retList = new ArrayList<>();

        for (PostCommentBucketModel bucketModel : bucketModelList) {
            if (retList.size() == limit)
                break;

            if (oldOLatest.equals("latest"))
                Collections.reverse(bucketModel.getBucket());

            if (limit - retList.size() >= bucketModel.getBucket().size())
                retList.addAll(bucketModel.getBucket());
            else
                retList.addAll(bucketModel.getBucket().subList(0, limit - retList.size()));
        }

        return retList;
    }

}
