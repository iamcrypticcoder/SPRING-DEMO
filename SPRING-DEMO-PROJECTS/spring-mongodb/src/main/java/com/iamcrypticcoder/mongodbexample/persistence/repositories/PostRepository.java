package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.PostModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<PostModel, String>, CustomPostRepository {


}
