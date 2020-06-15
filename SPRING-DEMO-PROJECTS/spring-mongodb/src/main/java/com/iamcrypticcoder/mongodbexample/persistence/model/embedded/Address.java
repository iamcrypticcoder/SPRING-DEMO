package com.iamcrypticcoder.mongodbexample.persistence.model.embedded;


import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Address {

    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String ZIP_CODE = "zipCode";
    public static final String NEIGHBORHOOD = "neighborhood";

    private String street;

    private String city;

    private String zipCode;

    private String neighborhood;
}
