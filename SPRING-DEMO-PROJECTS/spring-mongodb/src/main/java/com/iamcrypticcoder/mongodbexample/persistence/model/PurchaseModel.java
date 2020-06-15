package com.iamcrypticcoder.mongodbexample.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Document(collection = "PURCHASE")
public class PurchaseModel extends BaseModel<UserModel>  {

    private String name;

    private Integer age;

    private String gender;

    private String product;

    private String paymentMethod;

    private String amount;

    private Boolean isDelivered;

    private LocalDate date;
}
