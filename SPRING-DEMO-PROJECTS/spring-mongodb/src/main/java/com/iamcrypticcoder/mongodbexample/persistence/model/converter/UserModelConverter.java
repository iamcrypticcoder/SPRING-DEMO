package com.iamcrypticcoder.mongodbexample.persistence.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamcrypticcoder.mongodbexample.persistence.model.BaseModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserProfileModel;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;

@Component
@WritingConverter
@Slf4j
public class UserModelConverter implements Converter<UserModel, Document> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Document convert(UserModel s) {
        Document doc = new Document();

        if (null != s.getUsername())
            doc.put(UserModel.FIELD_USERNAME, s.getUsername());

        if (null != s.getEmail())
            doc.put(UserModel.FIELD_EMAIL, s.getEmail());

        if (null != s.getPassword())
            doc.put(UserModel.FIELD_PASSWORD, s.getPassword());

        if (null != s.getFirstName())
            doc.put(UserModel.FIELD_FIRST_NAME, s.getFirstName());

        if (null != s.getLastName())
            doc.put(UserModel.FIELD_LAST_NAME, s.getLastName());

        if (null != s.getGender())
            doc.put(UserModel.FIELD_GENDER, s.getGender());

        if (null != s.getIsExpired())
            doc.put(UserModel.FIELD_IS_EXPIRED, s.getIsExpired());

        if (null != s.getIsLocked())
            doc.put(UserModel.FIELD_IS_LOCKED, s.getIsLocked());

        if (null != s.getIsCredentialExpired())
            doc.put(UserModel.FIELD_IS_CREDENTIAL_EXPIRED, s.getIsCredentialExpired());

        if (null != s.getIsEnabled())
            doc.put(UserModel.FIELD_IS_ENABLED, s.getIsEnabled());

        if (null != s.getCreatedDateTime())
            doc.put(BaseModel.FIELD_CREATED_DATE_TIME, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(s.getCreatedDateTime()));
        doc.put(BaseModel.FIELD_CREATED_DATE_TIME, s.getCreatedDateTime());

        if (null != s.getUpdatedDateTime())
            doc.put(BaseModel.FIELD_UPDATED_DATE_TIME, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(s.getUpdatedDateTime()));
        doc.put(BaseModel.FIELD_UPDATED_DATE_TIME, s.getUpdatedDateTime());

        doc.remove("_class");

        return doc;
    }

    public void buildUpdateNotNull(UserModel m, Update u, String parentField) {
        parentField = nullToEmpty(parentField);
        final String prefixField = parentField.isEmpty() ? "" : parentField + ".";

        Map<String, Object> map = objectMapper.convertValue(m, Map.class);
        map.remove(BaseModel.FIELD_ID);

        for (Map.Entry entry : map.entrySet()) {
            log.debug(entry.getKey() + " -> " + entry.getValue());
            if (Objects.isNull(entry.getValue()))
                continue;

            if (entry.getKey().equals(BaseModel.FIELD_UPDATED_DATE_TIME)) {
                u.set(prefixField + entry.getKey(), DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getUpdatedDateTime()));
                continue;
            }

            u.set(prefixField + entry.getKey(), entry.getValue());
        }
    }

}
