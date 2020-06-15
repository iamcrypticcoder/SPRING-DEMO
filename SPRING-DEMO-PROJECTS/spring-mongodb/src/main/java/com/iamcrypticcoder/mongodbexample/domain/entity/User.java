package com.iamcrypticcoder.mongodbexample.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    // This entity will represent UserModel of persistence layer
    // Using mapper class this conversion happened
}
