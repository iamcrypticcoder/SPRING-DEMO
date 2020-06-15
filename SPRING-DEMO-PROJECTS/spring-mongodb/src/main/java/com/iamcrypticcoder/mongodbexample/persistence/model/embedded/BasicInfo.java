package com.iamcrypticcoder.mongodbexample.persistence.model.embedded;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BasicInfo {

    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_BIRTH_DATE = "birthDate";
    public static final String FIELD_LANGS = "langs";
    public static final String FIELD_RELIGIOUS_VIEW = "religiousView";
    public static final String FIELD_POLITICAL_VIEW = "politicalView";

    private String gender;

    private LocalDateTime birthDate;

    private List<String> langs;

    private String religiousView;

    private String politicalView;
}
