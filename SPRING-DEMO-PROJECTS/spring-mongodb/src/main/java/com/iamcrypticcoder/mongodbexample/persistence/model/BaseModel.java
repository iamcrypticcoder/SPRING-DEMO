package com.iamcrypticcoder.mongodbexample.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BaseModel<T extends BaseModel> {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED_DATE_TIME = "createdDateTime";
    public static final String FIELD_UPDATED_DATE_TIME = "updatedDateTime";

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

    @CreatedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedDateTime;

    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public T setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return (T) this;
    }

    public T setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
        return (T) this;
    }
}
