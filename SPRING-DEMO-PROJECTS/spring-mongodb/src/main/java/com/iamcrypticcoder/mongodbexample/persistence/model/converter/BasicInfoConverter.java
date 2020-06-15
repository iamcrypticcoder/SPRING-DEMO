package com.iamcrypticcoder.mongodbexample.persistence.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserProfileModel;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.BasicInfo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Component
@WritingConverter
public class BasicInfoConverter implements Converter<BasicInfo, Document> {

    @Override
    public Document convert(BasicInfo s) {
        Document doc = new Document();

        if (null != s.getGender())
            doc.put(BasicInfo.FIELD_GENDER, s.getGender());

        if (null != s.getBirthDate())
            doc.put(BasicInfo.FIELD_BIRTH_DATE, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(s.getBirthDate().with(LocalTime.MIDNIGHT)));

        if (null != s.getLangs())
            doc.put(BasicInfo.FIELD_LANGS, s.getLangs());

        if (null != s.getReligiousView())
            doc.put(BasicInfo.FIELD_RELIGIOUS_VIEW, s.getReligiousView());

        if (null != s.getPoliticalView())
            doc.put(BasicInfo.FIELD_POLITICAL_VIEW, s.getPoliticalView());

        return doc;
    }

    public void buildUpdateNotNull(BasicInfo m, Update u, String parentField) {
        final String pField = (null != parentField && !parentField.isEmpty()) ? parentField + "." : "";

        if (null != m.getGender())
            u.set(pField + BasicInfo.FIELD_GENDER, m.getGender());

        if (null != m.getBirthDate())
            u.set(pField + BasicInfo.FIELD_BIRTH_DATE, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getBirthDate().with(LocalTime.MIDNIGHT)));

        if (null != m.getLangs())
            u.set(pField + BasicInfo.FIELD_LANGS, m.getLangs());

        if (null != m.getReligiousView())
            u.set(pField + BasicInfo.FIELD_RELIGIOUS_VIEW, m.getReligiousView());

        if (null != m.getPoliticalView())
            u.set(pField + BasicInfo.FIELD_RELIGIOUS_VIEW, m.getPoliticalView());

        /*
        Map<String, Object> map = objectMapper.convertValue(model, Map.class);

        map.entrySet().forEach(entry -> {
            if (Objects.isNull(entry.getValue())) return;

            if (entry.getKey().equals(BasicInfo.FIELD_BIRTH_DATE)) {
                update.set(prefixField + entry.getKey(),
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(model.getBirthDate().with(LocalTime.MIDNIGHT)));
                return;
            }

            update.set(prefixField + entry.getKey(), entry.getValue());
        });
         */
    }
}
