package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.persistence.model.UserProfileModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfileModel, String>, CustomUserProfileRepository {

    Optional<UserProfileModel> findByUserId(String userId);

    List<UserProfileModel> findAllByBirthCity(String str);
    List<UserProfileModel> findAllByBirthCityLike(String str);

    List<UserProfileModel> findAllByBirthCountry(String str);
    List<UserProfileModel> findAllByBirthCountryLike(String str);

    List<UserProfileModel> findAllByCurrentCity(String str);
    List<UserProfileModel> findAllByCurrentCityLike(String str);

    List<UserProfileModel> findAllByCurrentCountry(String str);
    List<UserProfileModel> findAllByCurrentCountryLike(String str);

    List<UserProfileModel> findAllByBasicInfoGender(@Param("basicInfo.gender") String gender);
    List<UserProfileModel> findAllByBasicInfoBirthDateBetween(
            @Param("basicInfo.birthDate") LocalDateTime from,
            @Param("basicInfo.birthDate") LocalDateTime to);


}
