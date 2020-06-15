package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String>, CustomUserRepository {

    Optional<UserModel> findByEmail(String email);

    UserModel findByUsername(String username);
}
