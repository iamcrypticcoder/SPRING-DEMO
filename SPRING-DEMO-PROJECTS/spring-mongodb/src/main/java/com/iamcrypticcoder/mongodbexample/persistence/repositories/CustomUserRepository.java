package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository {

    boolean updateNotNull(UserModel userModel);

}
