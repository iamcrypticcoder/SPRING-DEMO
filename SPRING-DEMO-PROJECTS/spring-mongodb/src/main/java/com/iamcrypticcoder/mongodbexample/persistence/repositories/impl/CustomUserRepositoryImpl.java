package com.iamcrypticcoder.mongodbexample.persistence.repositories.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iamcrypticcoder.mongodbexample.persistence.model.BaseModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.UserModelConverter;
import  com.iamcrypticcoder.mongodbexample.persistence.repositories.CustomUserRepository;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.UserRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.datetime.DateFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserModelConverter userModelConverter;

    @Override
    public boolean updateNotNull(UserModel model) {
        if (null == model.getId())
            return false;

        // Set updated time if not set yet
        if (null == model.getUpdatedDateTime())
            model.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));

        Query query = new Query().addCriteria(where("id").is(model.getId()));
        Update update = new Update();
        userModelConverter.buildUpdateNotNull(model, update, null);

        UpdateResult result = mongoTemplate.updateFirst(query, update, UserModel.class);

        log.info("UpdateResult: " + result.toString());

        return result.getModifiedCount() == 1;
    }
}
