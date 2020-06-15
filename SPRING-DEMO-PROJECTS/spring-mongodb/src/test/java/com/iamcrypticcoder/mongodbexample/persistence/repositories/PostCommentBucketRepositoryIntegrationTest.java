package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.RandomDateTime;
import com.iamcrypticcoder.mongodbexample.config.MongoDBConfig;
import com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.PostComment;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.PostCommentBucketRepository;
import com.iamcrypticcoder.mongodbexample.utils.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel.MAX_BUCKET_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@ContextConfiguration(classes = MongoDBConfig.class)
public class PostCommentBucketRepositoryIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PostCommentBucketRepository repo;

    @Before
    public void setUp() {
        System.out.println("Before Test : Creating Collection - " + PostCommentBucketModel.class);
        mongoTemplate.dropCollection(PostCommentBucketModel.class);
        mongoTemplate.createCollection(PostCommentBucketModel.class,
                CollectionOptions.empty().schema(PostCommentBucketModel.buildMongoJsonSchema()));
    }

    @Test
    public void should_Find_Latest_Bucket_By_Post_Id_When_Save() {
        List<PostCommentBucketModel> buckets = generate_PostCommentBucketModel_List();
        repo.saveAll(buckets);

        Optional<PostCommentBucketModel> bucket = repo.findLatestBucketByPostId("5eaf258bb80a1f03cd97a121");
        if (bucket.isPresent())
            System.out.println(bucket.toString());
    }

    @Test
    public void should_Add_Comment_With_Post_Id() {
        String postId = "5eaf258bb80a1f04cd97a121";

        PostComment item = new PostComment()
                .setUserId("5eaf258bb80a1f03cd97a121")
                .setText("This is comment by user 1");

        repo.addComment(postId, item);
    }

    @Test
    public void should_Update_Comment_With_Comment_Id() {
        generateCommentDatabase();
        List<PostComment> commentList = repo.findAllLatestComments("5eaf258bb80a1f03cd97a120", 1);
        PostComment comment = commentList.get(0);

        String actual = "Text is changed";
        comment.setText(actual);
        repo.updateComment(comment);

        commentList = repo.findAllLatestComments("5eaf258bb80a1f03cd97a120", 1);
        String expected = commentList.get(0).getText();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_Remove_Comment_With_Comment_Id() {
        generateCommentDatabase();

        List<PostComment> commentList = repo.findAllLatestComments("5eaf258bb80a1f03cd97a120", 1);
        String actual = commentList.get(0).getCommentId();
        repo.deleteComment(commentList.get(0));

        commentList = repo.findAllLatestComments("5eaf258bb80a1f03cd97a120", 1);
        String expected = commentList.get(0).getCommentId();

        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    public void should_Find_Comment_Count() {
        // Save All
        generateCommentDatabase();

        int expected = repo.findCommentCount("5eaf258bb80a1f03cd97a120");

        assertThat(15).isEqualTo(expected);
    }

    @Test
    public void should_Find_All_Comment_By_Post_Id_When_Save_All() {
        List<PostCommentBucketModel> buckets = generate_PostCommentBucketModel_List();
        repo.saveAll(buckets);

        for (PostCommentBucketModel bucket : buckets)
            bucket.setBucket(generate_PostCommentItem_List(bucket.getId(), 10));

        repo.saveAll(buckets);

        String postId = "5eaf258bb80a1f03cd97a120";

        int actual = 0;
        for (PostCommentBucketModel bucket : buckets)
            if (bucket.getPostId().equals(postId))
                actual += bucket.getBucket().size();

        List<PostCommentBucketModel> foundBuckets = repo.findAllByPostId(postId);

        int expected = 0;
        for (PostCommentBucketModel bucket : foundBuckets)
            if (bucket.getPostId().equals(postId))
                expected += bucket.getBucket().size();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_Find_All_Old_Comment_By_Post_Id_When_Save_All() {
        // Save All
        generateCommentDatabase();

        // Find Old Comment
        repo.findAllOldComments("5eaf258bb80a1f03cd97a120", 6).forEach(val -> {
            System.out.println(val.toString());
        });
    }

    @Test
    public void should_Find_All_Latest_Comment_By_Post_Id_When_Save_All() {
        // Save All
        generateCommentDatabase();

        // Find Latest Comment
        repo.findAllLatestComments("5eaf258bb80a1f03cd97a120", 6).forEach(val -> {
            System.out.println(val.toString());
        });
    }

    @Test
    public void should_Find_All_Comments_After_A_Comment_When_Save_All() {
        // Save All
        generateCommentDatabase();

        List<PostComment> commentList = repo.findAllOldComments("5eaf258bb80a1f03cd97a120", 3);

        commentList.forEach(val -> {
            System.out.println(val.toString());
        });

        List<PostComment> nextCommentList = repo.findAllCommentsAfter(commentList.get(commentList.size()-1), 8);

        nextCommentList.forEach(val -> {
            System.out.println(val.toString());
        });

        nextCommentList = repo.findAllCommentsAfter(nextCommentList.get(nextCommentList.size()-1), 5);

        nextCommentList.forEach(val -> {
            System.out.println(val.toString());
        });

    }

    @Test
    public void should_Find_All_Comments_Before_A_Comment_When_Save_All() {
        // Save All
        generateCommentDatabase();

        List<PostComment> commentList = repo.findAllLatestComments("5eaf258bb80a1f03cd97a120", 3);

        commentList.forEach(val -> {
            System.out.println(val.toString());
        });

        List<PostComment> prevCommentList = repo.findAllCommentsBefore(commentList.get(commentList.size() - 1), 8);

        prevCommentList.forEach(val -> {
            System.out.println(val.toString());
        });

        prevCommentList = repo.findAllCommentsBefore(prevCommentList.get(prevCommentList.size() - 1), 5);

        prevCommentList.forEach(val -> {
            System.out.println(val.toString());
        });
    }

    private void generateCommentDatabase() {
        List<PostCommentBucketModel> bucketList = generate_PostCommentBucketModel_List("5eaf258bb80a1f03cd97a120", 3);
        repo.saveAll(bucketList);

        List<PostComment> commentList = generate_PostCommentItem_List("", 15);
        int startIndex = 0;
        for (int i = 0; i < bucketList.size(); i++) {
            List<PostComment> list = commentList.subList(startIndex, startIndex + MAX_BUCKET_SIZE);
            for (int j = 0; j < list.size(); j++)
                list.get(j).createCommentId(bucketList.get(i).getId());

            repo.addCommentListInBucket(bucketList.get(i).getId(), list);

            startIndex = startIndex + MAX_BUCKET_SIZE;
        }
    }

    private List<PostCommentBucketModel> generate_PostCommentBucketModel_List() {
        List<PostCommentBucketModel> retList = new ArrayList<>();

        retList.addAll(Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).map(val -> {

            PostCommentBucketModel model = new PostCommentBucketModel()
                    .setPostId("5eaf258bb80a1f03cd97a12" + val)
                    .setBucketIndex(0L)
                    .setBucket(new ArrayList<>());

            return model;

        }).collect(Collectors.toList()));

        retList.addAll(Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).map(val -> {

            PostCommentBucketModel model = new PostCommentBucketModel()
                    .setPostId("5eaf258bb80a1f03cd97a12" + val)
                    .setBucketIndex(1L)
                    .setBucket(new ArrayList<>());

            return model;

        }).collect(Collectors.toList()));

        return retList;
    }

    private List<PostCommentBucketModel> generate_PostCommentBucketModel_List(String postId, int count) {
        List<PostCommentBucketModel> retList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            PostCommentBucketModel model = new PostCommentBucketModel()
                    .setPostId(postId)
                    .setBucketIndex((long)i)
                    .setBucket(new ArrayList<>());

            retList.add(model);
        }

        return retList;
    }

    private List<PostComment> generate_PostCommentItem_List(String bucketId, int count) {
        RandomDateTime randTime = new RandomDateTime();
        LocalDateTime dateTime = randTime.nextDateTime();

        List<PostComment> retList = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            PostComment item = new PostComment()
                    .setCommentId(bucketId + StringUtil.generateRandomString(6))
                    .setUserId("5eaf258bb80a1f03cd97a12" + (i%10))
                    .setText("This is comment " + i)
                    .setCreatedDate(dateTime)
                    .setUpdatedDate(dateTime);

            retList.add(item);

            dateTime = dateTime.plusMinutes(30);
        }

        return retList;
    }
}
