package com.iamcrypticcoder.mongodbexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamcrypticcoder.mongodbexample.persistence.model.*;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.UserProfileModelConverter;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.Address;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.BasicInfo;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.ContactInfo;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.SocialLink;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.UserProfileRepository;
import com.iamcrypticcoder.mongodbexample.persistence.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SpringMongodbExampleApplication implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserProfileRepository userProfileRepo;

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(SpringMongodbExampleApplication.class, args);

        final AtomicInteger counter = new AtomicInteger(0);

        Arrays.asList(context.getBeanDefinitionNames())
                .forEach(beanName -> {
                    System.out.println(String.format("(%d) Bean Name: {%s} ", counter.incrementAndGet(), beanName));
                });

    }

//    @Bean
//    public UserProfileModelConverter converter() {
//        return new UserProfileModelConverter();
//    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }

    public void run(String... args) {
        //test();
        //userRepoTest();
        //userProfileRepoTest();
    }

    @SneakyThrows
    private void test() {
        BasicInfo info = new BasicInfo();
        info.setGender("Male").setBirthDate(LocalDateTime.now(UTC));
        Field[] fields = info.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.getModifiers();
            Class t = f.getType();
            Object v = f.get(info);

            System.out.println(t.toString());
            System.out.println(v.toString());

        }
        System.out.println(Arrays.toString(fields));
    }

    private void userProfileRepoTest() {
        BasicInfo bInfo = new BasicInfo();
        bInfo.setGender("Male");
        bInfo.setBirthDate(LocalDateTime.parse("2020-05-05"));
        bInfo.setLangs(Arrays.asList("Bangla", "English"));
        bInfo.setPoliticalView("N/A");
        bInfo.setReligiousView("Islam (Sunni)");

        ContactInfo cInfo = new ContactInfo();
        cInfo.setMobilePhones(Arrays.asList(new String[]{"01911276607", "01520084330"}));
        cInfo.setAddress(new Address("321 Zigatola", "Dhaka", "12345", "Etc"));
        cInfo.setSocialLinks(Arrays.asList(new SocialLink[]{
                new SocialLink("Facebook", "Facebook Link"),
                new SocialLink("Google", "Google Link")
        }));
        cInfo.setWebsites(Arrays.asList(new String[]{"www.web1.com", "www.web2.com"}));
        cInfo.setEmails(Arrays.asList(new String[]{"email1@gmail.com", "email2@gmail.com"}));

        UserProfileModel model = new UserProfileModel()
                .setUserId("5eaf258bb80a1f03cd97a123")
                .setBasicInfo(bInfo)
                .setContactInfo(cInfo)
                .setBirthCity("Dhaka")
                .setBirthCountry("Bangladesh")
                .setCurrentCity("Dhaka")
                .setCurrentCountry("Bangladesh")
                .setProfilePicUrl("Profile Pic Url")
                .setCoverImageUrl("Cover Pic Url")
                .setAboutMe("About me text goes here");

        try {
            UserProfileModel savedModel = userProfileRepo.save(model);
            System.out.println(savedModel.toString());

        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void userRepoTest() {
    /*
    UserModel savedModel = mongoTemplate.insert(user1);
    System.out.println("Saved user id: " + savedModel.getId());
    */

        List users = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(val -> {
            UserModel userModel = new UserModel()
                    .setEmail("user" + val + "@gmail.com")
                    .setPassword("123456")
                    .setFirstName("User " + val)
                    .setLastName("User " + val)
                    .setGender(new Random(System.currentTimeMillis()).nextInt(2) == 0 ? "Male" : "Female")
                    .setIsExpired(false)
                    .setIsLocked(false)
                    .setIsCredentialExpired(false)
                    .setIsEnabled(true);
            return userModel;
        }).collect(Collectors.toList());

        mongoTemplate.save(users.get(0));
/*
        try {
//            UserModel savedModel = userRepository.save(user1);
//            System.out.println("Saved user id: " + savedModel.getId());
            List<UserModel> savedModels = userRepo.saveAll(users);

//            savedModel.setEmail("change@gmail.com");
//            savedModel.setId("");
//            userRepository.insert(savedModel);
            savedModels.forEach(userModel -> System.out.println("Saved user id: " + userModel.getId()));
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        try {
            PageRequest firstPageRequest = PageRequest.of(0, 3);
            PageRequest secondPageRequest = PageRequest.of(0, 5, Sort.by("email").descending());

            //userRepository.findAll(secondPageRequest).get().forEach(userModel -> System.out.println(userModel.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Example userExample = Example.of(new UserModel().setEmail("user"));
            userRepo
                    .findAll(userExample)
                    .forEach(userModel -> System.out.println(userModel.toString()));
            ExampleMatcher matcher = ExampleMatcher
                    .matching()
                    .withMatcher("email", startsWith().ignoreCase());
            userRepo
                    .findAll(Example.of(new UserModel().setEmail("user"), matcher))
                    .forEach(userModel -> System.out.println(userModel.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }
}
