package com.iamcrypticcoder.mongodbexample.persistence.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamcrypticcoder.mongodbexample.persistence.model.BaseModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserProfileModel;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Component
@WritingConverter
@Slf4j
public class UserProfileModelConverter implements Converter<UserProfileModel, Document> {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BasicInfoConverter basicInfoConverter;

    @Autowired
    private ContactInfoConverter contactInfoConverter;

    @Override
    public Document convert(UserProfileModel s) {
        Document doc = new Document();

        if (null != s.getUserId())
            doc.put(UserProfileModel.FIELD_USER_ID, s.getUserId());

        if (null != s.getBasicInfo())
            doc.put(UserProfileModel.FIELD_BASIC_INFO, basicInfoConverter.convert(s.getBasicInfo()));

        if (null != s.getContactInfo())
            doc.put(UserProfileModel.FIELD_CONTACT_INFO, contactInfoConverter.convert(s.getContactInfo()));

        if (null != s.getBirthCity())
            doc.put(UserProfileModel.FIELD_BIRTH_CITY, s.getBirthCity());

        if (null != s.getBirthCountry())
            doc.put(UserProfileModel.FIELD_BIRTH_COUNTRY, s.getBirthCountry());

        if (null != s.getCurrentCity())
            doc.put(UserProfileModel.FIELD_CURRENT_CITY, s.getCurrentCity());

        if (null != s.getCurrentCountry())
            doc.put(UserProfileModel.FIELD_CURRENT_COUNTRY, s.getCurrentCountry());

        if (null != s.getProfilePicUrl())
            doc.put(UserProfileModel.FIELD_PROFILE_PIC_URL, s.getProfilePicUrl());

        if (null != s.getCoverImageUrl())
            doc.put(UserProfileModel.FIELD_COVER_PIC_URL, s.getCoverImageUrl());

        if (null != s.getAboutMe())
            doc.put(UserProfileModel.FIELD_ABOUT_ME, s.getAboutMe());

        if (null != s.getCreatedDateTime())
            doc.put(BaseModel.FIELD_CREATED_DATE_TIME, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(s.getCreatedDateTime()));
            //doc.put(BaseModel.FIELD_CREATED_DATE_TIME, s.getCreatedDateTime());

        if (null != s.getUpdatedDateTime())
            doc.put(BaseModel.FIELD_UPDATED_DATE_TIME, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(s.getUpdatedDateTime()));
            //doc.put(BaseModel.FIELD_UPDATED_DATE_TIME, s.getUpdatedDateTime());

        doc.remove("_class");

        return doc;
    }

    public void buildUpdateNotNull(UserProfileModel m, Update u, String parentField) {
        final String prefixField = (null != parentField && !parentField.isEmpty()) ? parentField + "." : "";

        Map<String, Object> map = objectMapper.convertValue(m, Map.class);
        map.remove(BaseModel.FIELD_ID);
        map.remove(UserProfileModel.FIELD_USER_ID);

        for (Map.Entry entry : map.entrySet()) {
            log.debug(entry.getKey() + " -> " + entry.getValue());
            if (Objects.isNull(entry.getValue()))
                continue;

            if (entry.getKey().equals(UserProfileModel.FIELD_BASIC_INFO)) {
                basicInfoConverter.buildUpdateNotNull(m.getBasicInfo(), u, UserProfileModel.FIELD_BASIC_INFO);
                continue;
            }

            if (entry.getKey().equals(UserProfileModel.FIELD_CONTACT_INFO)) {
                contactInfoConverter.buildUpdateNotNull(m.getContactInfo(), u, UserProfileModel.FIELD_CONTACT_INFO);
                continue;
            }

            if (entry.getKey().equals(BaseModel.FIELD_UPDATED_DATE_TIME)) {
                u.set(prefixField + entry.getKey(), DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getUpdatedDateTime()));
                continue;
            }

            u.set(prefixField + entry.getKey(), entry.getValue());
        }

        //u.set(BaseModel.FIELD_UPDATED_DATE_TIME, m.getUpdatedDateTime());
    }
}
