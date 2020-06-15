package com.iamcrypticcoder.mongodbexample.persistence.model.converter;

import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.Address;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.ContactInfo;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;

@Component
@WritingConverter
public class AddressConverter implements Converter<Address, Document> {

    @Override
    public Document convert(Address s) {
        Document doc = new Document();

        if (null != s.getStreet())
            doc.put(Address.STREET, s.getStreet());

        if (null != s.getCity())
            doc.put(Address.CITY, s.getCity());

        if (null != s.getZipCode())
            doc.put(Address.ZIP_CODE, s.getZipCode());

        if (null != s.getNeighborhood())
            doc.put(Address.NEIGHBORHOOD, s.getNeighborhood());


        return doc;
    }

    public void buildUpdateNotNull(Address m, Update u, String parentField) {
        final String pField = (null != parentField && !parentField.isEmpty()) ? parentField + "." : "";

        if (null != m.getStreet())
            u.set(pField + ContactInfo.FIELD_MOBILE_PHONES, m.getStreet());

        if (null != m.getCity())
            u.set(pField + ContactInfo.FIELD_ADDRESS, m.getCity());

        if (null != m.getZipCode())
            u.set(pField + ContactInfo.FIELD_SOCIAL_LINKS, m.getZipCode());

        if (null != m.getNeighborhood())
            u.set(pField + ContactInfo.FIELD_WEBSITES, m.getNeighborhood());
    }
}
