package com.iamcrypticcoder.mongodbexample.persistence.model.embedded;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SocialLink {

    private String platform;

    private String link;
}
