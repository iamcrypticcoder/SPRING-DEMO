package com.iamcrypticcoder.mongodbexample.persistence.model;

import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.BasicInfo;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.ContactInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.*;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.array;

@Accessors(chain = true)
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "USER_PROFILE")
public class UserProfileModel extends BaseModel<UserModel>  {

    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BASIC_INFO = "basicInfo";
    public static final String FIELD_CONTACT_INFO = "contactInfo";
    public static final String FIELD_BIRTH_CITY = "birthCity";
    public static final String FIELD_BIRTH_COUNTRY = "birthCountry";
    public static final String FIELD_CURRENT_CITY = "currentCity";
    public static final String FIELD_CURRENT_COUNTRY = "currentCountry";
    public static final String FIELD_PROFILE_PIC_URL = "profilePicUrl";
    public static final String FIELD_COVER_PIC_URL = "coverImageUrl";
    public static final String FIELD_ABOUT_ME = "aboutMe";

    private String userId;

    private BasicInfo basicInfo;

    private ContactInfo contactInfo;

    private String birthCity;

    private String birthCountry;

    private String currentCity;

    private String currentCountry;

    private String profilePicUrl;

    private String coverImageUrl;

    private String aboutMe;

    public static MongoJsonSchema buildMongoJsonSchema() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("userId")
                .properties(
                        string("userId"),
                        object("basicInfo").properties(
                                string("gender").possibleValues("Male", "Female", "Unspecified"),
                                date("birthDate"),
                                array("langs"),
                                string("religiousView"),
                                string("politicalView")
                        ),
                        object("contactInfo").properties(
                                array("mobilePhones"),
                                object("address").properties(
                                        string("street"),
                                        string("city"),
                                        string("zipCode"),
                                        string("neighborhood")
                                ),
                                array("socialLinks"),
                                array("websites"),
                                array("emails")
                        ),
                        string("birthCity"),
                        string("birthCountry"),
                        string("currentCity"),
                        string("currentCountry"),
                        string("profilePicUrl"),
                        string("coverImageUrl"),
                        string("aboutMe")
                ).build();

        return schema;
    }
}
