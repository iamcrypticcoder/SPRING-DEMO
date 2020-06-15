package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.PostComment;

import java.util.List;
import java.util.Optional;

public interface CustomPostCommentBucketRepository {

    Optional<PostCommentBucketModel> findLatestBucketByPostId(String postId);

    boolean addComment(String postId, PostComment comment);

    boolean updateComment(PostComment item);

    boolean deleteComment(PostComment item);

    boolean addCommentListInBucket(String bucketId, List<PostComment> list);

    int findCommentCount(String postId);

    List<PostComment> findAllOldComments(String postId, int limit);

    List<PostComment> findAllLatestComments(String postId, int limit);

    List<PostComment> findAllCommentsAfter(PostComment comment, int limit);

    List<PostComment> findAllCommentsBefore(PostComment comment, int limit);
}
