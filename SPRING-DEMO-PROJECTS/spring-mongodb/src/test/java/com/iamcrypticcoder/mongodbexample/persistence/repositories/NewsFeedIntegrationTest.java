package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.RandomDateTime;
import com.iamcrypticcoder.mongodbexample.config.MongoDBConfig;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.PostComment;
import com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.PostModel;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.PostCommentBucketRepository;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@ContextConfiguration(classes = MongoDBConfig.class)
public class NewsFeedIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private PostCommentBucketRepository commentRepo;

    @Before
    public void setUp() {
        System.out.println("Before Test");
        mongoTemplate.dropCollection(PostModel.class);
        mongoTemplate.dropCollection(PostCommentBucketModel.class);

        mongoTemplate.createCollection(PostModel.class,
                CollectionOptions.empty().schema(PostModel.buildMongoJsonSchema()));
        mongoTemplate.createCollection(PostCommentBucketModel.class,
                CollectionOptions.empty().schema(PostCommentBucketModel.buildMongoJsonSchema()));
    }

    @Test
    public void should_Find_All_Latest_News_Feed_With_Comments() {
        generateSampleDatabase();

        List<PostModel> postList = postRepo.findAll(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        postList.forEach(post -> {
            System.out.println(commentRepo.findCommentCount(post.getId()));
        });
    }

    private void generateSampleDatabase() {
        Random rand = new Random(System.nanoTime());
        RandomDateTime randTime = new RandomDateTime();
        LocalDateTime dateTime = randTime.nextDateTime();

        List<PostModel> postList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            postList.add(new PostModel()
                    .setUserId("5eaf258bb80a1f03cd97a120")
                    .setText("This is post " + i)
                    .setPrivacy("Public")
                    .setCreatedDateTime(LocalDateTime.ofEpochSecond(dateTime.toEpochSecond(ZoneOffset.UTC), 0, ZoneOffset.UTC))
                    .setUpdatedDateTime(LocalDateTime.ofEpochSecond(dateTime.toEpochSecond(ZoneOffset.UTC), 0, ZoneOffset.UTC)));

            dateTime = dateTime.plusMinutes(30);
        }
        postRepo.saveAll(postList);

        postList.forEach(post -> {
            int commentCount = rand.nextInt(20) + 1;
            LocalDateTime dateTime2 = post.getCreatedDateTime().plusMinutes(30);

            for (int i = 0; i < commentCount; i++) {
                PostComment comment = new PostComment()
                        .setUserId("5eaf258bb80a1f03cd97a120")
                        .setText("This is comment " + i)
                        .setCreatedDate(dateTime2)
                        .setUpdatedDate(dateTime2);

                commentRepo.addComment(post.getId(), comment);

                dateTime2 = dateTime2.plusMinutes(30);
            }
        });
    }

}
