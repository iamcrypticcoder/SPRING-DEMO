package com.iamcrypticcoder.mongodbexample.persistence.model;

import lombok.*;
import lombok.experimental.Accessors;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Document(collection = "POST")
public class PostModel extends BaseModel<PostModel> {

    private String userId;

    private String text;

    private String privacy;

    public static MongoJsonSchema buildMongoJsonSchema() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("userId", "text", "privacy")
                .properties(
                        string("userId"),
                        string("text"),
                        string("gender").possibleValues("Public", "Friends", "Private")
                ).build();
        return schema;
    }
}
