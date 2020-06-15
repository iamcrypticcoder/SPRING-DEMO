package com.iamcrypticcoder.mongodbexample.persistence.repositories.impl;


import com.iamcrypticcoder.mongodbexample.persistence.model.UserProfileModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.UserProfileModelConverter;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.CustomUserProfileRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
public class CustomUserProfileRepositoryImpl implements CustomUserProfileRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserProfileModelConverter converter;

    @Override
    public boolean updateNotNull(UserProfileModel model) {
        if (null == model.getUserId())
            return false;

        // Set updated time if not set yet
        if (null == model.getUpdatedDateTime())
            model.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));

        Query query = new Query().addCriteria(where(UserProfileModel.FIELD_USER_ID).is(model.getUserId()));
        Update update = new Update();
        converter.buildUpdateNotNull(model, update, "");
        UpdateResult result = mongoTemplate.updateFirst(query, update, UserProfileModel.class);

        log.debug("UpdateResult: " + result.toString());

        return result.getModifiedCount() == 1;
    }

    @Override
    public Set<String> findAllUniquePhoneNumber() {
        List<String> list = mongoTemplate.findDistinct(new Query(), "contactInfo.mobilePhones", UserProfileModel.class, String.class);
        System.out.println("Unique Mobile Phone List:");
        list.forEach(s -> {
            System.out.println(s);
        });
        return Set.copyOf(list);
    }

    @Override
    public Set<String> findAllUniqueLanguage() {
        List<String> list = mongoTemplate.findDistinct(new Query(), "basicInfo.langs", UserProfileModel.class, String.class);
        System.out.println("Unique Lang List:");
        list.forEach(s -> {
            System.out.println(s);
        });
        return Set.copyOf(list);
    }

    @Override
    public List<UserProfileModel> findAllUserWithLanguages(String... lang) {
        Query query = new Query();
        query.addCriteria(where("basicInfo.langs").all(lang));
        List<UserProfileModel> foundProfiles = mongoTemplate.find(query, UserProfileModel.class);

        for (UserProfileModel p : foundProfiles) {
            System.out.println(p.getUserId() + ": Lang List: " + p.getBasicInfo().getLangs());
        }

        return foundProfiles;
    }
}
