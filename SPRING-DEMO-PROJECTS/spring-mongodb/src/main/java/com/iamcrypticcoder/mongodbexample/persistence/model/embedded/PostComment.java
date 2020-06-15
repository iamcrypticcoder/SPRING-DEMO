package com.iamcrypticcoder.mongodbexample.persistence.model.embedded;

import com.iamcrypticcoder.mongodbexample.utils.StringUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PostComment {

    private String commentId;

    private String userId;

    private String text;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public PostComment createCommentId(String bucketId) {
        commentId = bucketId + "_" + StringUtil.generateRandomString(6);
        return this;
    }

}
