package com.iamcrypticcoder.mongodbexample.persistence.model;

import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.PostComment;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.*;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "POST_COMMENT_BUCKET")
public class PostCommentBucketModel extends BaseModel<UserModel>  {

    public static int MAX_BUCKET_SIZE = 5;

    public PostCommentBucketModel(String postId, Long bucketIndex) {
        this.postId = postId;
        this.bucketIndex = bucketIndex;
        this.bucket = new ArrayList<>();
    }

    private String postId;

    private Long bucketIndex;

    private List<PostComment> bucket;

    public static MongoJsonSchema buildMongoJsonSchema() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("postId", "bucketIndex", "bucket")
                .properties(
                        string("postId"),
                        int64("bucketIndex"),
                        array("bucket").minItems(0).maxItems(1000)
                ).build();
        return schema;
    }
}
