package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.PostCommentBucketModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostCommentBucketRepository extends
        MongoRepository<PostCommentBucketModel, String>, CustomPostCommentBucketRepository {

    List<PostCommentBucketModel> findAllByPostId(String postId);

}
