package com.iamcrypticcoder.mongodbexample.persistence.model.embedded;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ContactInfo {

    public static final String FIELD_MOBILE_PHONES = "mobilePhones";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_SOCIAL_LINKS = "socialLinks";
    public static final String FIELD_WEBSITES = "websites";
    public static final String FIELD_EMAILS = "emails";

    private List<String> mobilePhones;

    private Address address;

    private List<SocialLink> socialLinks;

    private List<String> websites;

    private List<String> emails;
}
