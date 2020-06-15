package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.UserProfileModel;

import java.util.List;
import java.util.Set;

public interface CustomUserProfileRepository {

    boolean updateNotNull(UserProfileModel userModel);

    Set<String> findAllUniquePhoneNumber();

    Set<String> findAllUniqueLanguage();

    List<UserProfileModel> findAllUserWithLanguages(String... lang);

}
