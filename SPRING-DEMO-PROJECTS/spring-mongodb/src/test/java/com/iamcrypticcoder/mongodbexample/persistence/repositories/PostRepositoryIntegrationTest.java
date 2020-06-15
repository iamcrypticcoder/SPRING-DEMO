package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.config.MongoDBConfig;
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
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.*;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@ContextConfiguration(classes = MongoDBConfig.class)
public class PostRepositoryIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PostRepository postRepository;

    @Before
    public void setUp() {
        System.out.println("Before Test");
        mongoTemplate.dropCollection(PostModel.class);
        mongoTemplate.createCollection(PostModel.class,
                CollectionOptions.empty().schema(PostModel.buildMongoJsonSchema()));
    }
}
