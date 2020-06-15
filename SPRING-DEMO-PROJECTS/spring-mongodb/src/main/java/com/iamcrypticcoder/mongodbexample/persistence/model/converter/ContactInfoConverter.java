package com.iamcrypticcoder.mongodbexample.persistence.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.BasicInfo;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.ContactInfo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class ContactInfoConverter implements Converter<ContactInfo, Document> {

    @Autowired
    private AddressConverter addressConverter;

    @Override
    public Document convert(ContactInfo s) {
        Document doc = new Document();

        if (null != s.getMobilePhones())
            doc.put(ContactInfo.FIELD_MOBILE_PHONES, s.getMobilePhones());

        if (null != s.getAddress())
            doc.put(ContactInfo.FIELD_ADDRESS, addressConverter.convert(s.getAddress()));

        if (null != s.getSocialLinks())
            doc.put(ContactInfo.FIELD_SOCIAL_LINKS, s.getSocialLinks());

        if (null != s.getWebsites())
            doc.put(ContactInfo.FIELD_WEBSITES, s.getWebsites());

        if (null != s.getEmails())
            doc.put(ContactInfo.FIELD_EMAILS, s.getEmails());

        return doc;
    }

    public void buildUpdateNotNull(ContactInfo m, Update u, String parentField) {
        final String pField = (null != parentField && !parentField.isEmpty()) ? parentField + "." : "";

        if (null != m.getMobilePhones())
            u.set(pField + ContactInfo.FIELD_MOBILE_PHONES, m.getMobilePhones());

        if (null != m.getAddress())
            addressConverter.buildUpdateNotNull(m.getAddress(), u, pField + ContactInfo.FIELD_ADDRESS);

        if (null != m.getSocialLinks())
            u.set(pField + ContactInfo.FIELD_SOCIAL_LINKS, m.getSocialLinks());

        if (null != m.getWebsites())
            u.set(pField + ContactInfo.FIELD_WEBSITES, m.getWebsites());

        if (null != m.getEmails())
            u.set(pField + ContactInfo.FIELD_EMAILS, m.getEmails());
    }
}
