package com.iamcrypticcoder.mongodbexample.persistence.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.bool;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

@Accessors(chain = true)
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "USER")
public class UserModel extends BaseModel<UserModel> {

    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_LAST_NAME = "lastName";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_IS_EXPIRED = "isExpired";
    public static final String FIELD_IS_LOCKED = "isLocked";
    public static final String FIELD_IS_CREDENTIAL_EXPIRED = "isCredentialExpired";
    public static final String FIELD_IS_ENABLED = "isEnabled";

    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String gender;

    private Boolean isExpired;

    private Boolean isLocked;

    private Boolean isCredentialExpired;

    private Boolean isEnabled;

    public static MongoJsonSchema buildMongoJsonSchema() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("email", "password", "firstName", "lastName", "gender",
                        "isExpired", "isLocked", "isCredentialExpired", "isEnabled")
                .properties(
                        string("username").minLength(6).maxLength(32),
                        string("email").matching("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+).([a-zA-Z]{2,5})$"),
                        string("firstName").maxLength(32),
                        string("lastName").maxLength(32),
                        string("gender").possibleValues("Male", "Female", "Unspecified"),
                        bool("isExpired"),
                        bool("isLocked"),
                        bool("isCredentialExpired"),
                        bool("isEnabled")
                ).build();

        return schema;
    }
}
