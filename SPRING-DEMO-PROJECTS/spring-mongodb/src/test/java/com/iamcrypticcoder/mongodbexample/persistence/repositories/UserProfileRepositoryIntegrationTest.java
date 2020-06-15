package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.*;
import com.iamcrypticcoder.mongodbexample.config.MongoDBConfig;
import com.iamcrypticcoder.mongodbexample.persistence.model.*;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.Address;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.BasicInfo;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.ContactInfo;
import com.iamcrypticcoder.mongodbexample.persistence.model.embedded.SocialLink;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@ContextConfiguration(classes = MongoDBConfig.class)
@Slf4j
public class UserProfileRepositoryIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserProfileRepository repo;

    @Before
    public void setUp() {
        log.debug("Before Test");
        mongoTemplate.dropCollection(UserProfileModel.class);
        mongoTemplate.createCollection(UserProfileModel.class);
                //CollectionOptions.empty().schema(UserProfileModel.buildMongoJsonSchema()));
    }

    @Test
    public void should_Find_By_Id_When_Save() {
        UserProfileModel profileModel = generateUserProfileModel();
        String savedId = repo.save(profileModel).getId();

        Optional<UserProfileModel> found = repo.findById(savedId);

        assert found.isPresent();
        assertThat(found.get().getUserId()).isEqualTo(profileModel.getUserId());
    }

    @Test
    public void should_Find_By_User_Id_When_Save() {
        UserProfileModel profileModel = generateUserProfileModel();
        String savedId = repo.save(profileModel).getId();

        Optional<UserProfileModel> found = repo.findByUserId(profileModel.getUserId());

        assert found.isPresent();
        assertThat(found.get().getBasicInfo()).isEqualToComparingFieldByField(profileModel.getBasicInfo());
    }

    @Test
    public void should_Find_All_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();
        repo.saveAll(profiles);

        profiles.forEach(profile -> {
            Optional<UserProfileModel> found = repo.findByUserId(profile.getUserId());
            assert found.isPresent();
            assertThat(found.get()).isEqualTo(profile);
        });
    }

    @Test
    public void should_Find_All_By_Current_City_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();
        repo.saveAll(profiles);

        List<UserProfileModel> foundList = repo.findAllByCurrentCity("Dhaka");
        System.out.println("Found profile count: " + foundList.size());

    }

    @Test
    public void should_Find_All_By_Gender_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();
        repo.saveAll(profiles);

        List<UserProfileModel> foundProfiles = repo.findAllByBasicInfoGender("Male");

        int actualCount = 0;
        for (UserProfileModel p : profiles)
            if (p.getBasicInfo().getGender().equals("Male")) actualCount++;

        System.out.println("Found profile count: " + foundProfiles.size());

        assertThat(actualCount).isEqualTo(foundProfiles.size());
    }

    @Test
    public void should_Find_All_By_BirthDate_Range_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();
        repo.saveAll(profiles);

        LocalDateTime from = LocalDateTime.of(2011, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2019, 12, 31, 0, 0);
        List<UserProfileModel> foundProfiles =
                repo.findAllByBasicInfoBirthDateBetween(from, to);

        int actualCount = 0;
        for (UserProfileModel p : profiles) {
            LocalDateTime date = p.getBasicInfo().getBirthDate();
            if (date.isAfter(from) && date.isBefore(to))
                actualCount++;
        }

        System.out.println("Found profile count: " + foundProfiles.size());

        assertThat(actualCount).isEqualTo(foundProfiles.size());
    }

    @Test
    public void should_Find_All_Users_With_Language_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();
        repo.saveAll(profiles);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("basicInfo", startsWith());
        List<UserProfileModel> savedProfiles = repo.findAll(
                Example.of(new UserProfileModel().setBasicInfo(new BasicInfo().setGender("Male"))));

    }

    @Test
    public void should_Find_All_Emails_Of_All_Profiles_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();

    }

    @Test
    public void should_Find_All_PhoneNumbers_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();

        int actualCount = 0;
        for (UserProfileModel p : profiles)
            actualCount += p.getContactInfo().getMobilePhones().size();

        repo.saveAll(profiles);

        Set<String> numbers = repo.findAllUniquePhoneNumber();
        System.out.println("Total phone number found : " + numbers.size());

        assertThat(actualCount).isEqualTo(numbers.size());
    }

    @Test
    public void should_Find_All_Unique_Langs_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();

        Set<String> actualLangs = new HashSet<>();
        for (UserProfileModel p : profiles)
            actualLangs.addAll(p.getBasicInfo().getLangs());

        repo.saveAll(profiles);

        Set<String> langs = repo.findAllUniqueLanguage();
        System.out.println("Total unique language found : " + langs.size());

        assertThat(actualLangs).isEqualTo(langs);
    }

    @Test
    public void should_Find_All_UserProfile_With_Lang_When_Save_All() {
        List<UserProfileModel> profiles = generate_Ten_UserProfileModel_List();

        repo.saveAll(profiles);

        List<UserProfileModel> foundProfiles = repo.findAllUserWithLanguages("English");


    }

    @Test
    public void should_Update_By_User_Id_When_Save() throws InterruptedException {
        UserProfileModel profileModel = generateUserProfileModel();
        profileModel.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
        profileModel.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));

        String savedUserId = repo.save(profileModel).getUserId();

        Thread.sleep(3000);

        UserProfileModel newModel = new UserProfileModel().setUserId(savedUserId);
        newModel.setAboutMe("About Me Changed");
        newModel.setBasicInfo(new BasicInfo().setGender("Male").setBirthDate(LocalDateTime.of(1991, 01, 01, 00, 00)));
        newModel.setContactInfo(new ContactInfo().setAddress(new Address().setNeighborhood("Evan")));

        //repo.save(newModel);
        repo.updateNotNull(newModel);
//
        Optional<UserProfileModel> foundModel = repo.findByUserId(savedUserId);
        log.debug("Found Profile: " + foundModel.get().toString());

        //System.out.println("Found Profile: " + foundModel.get().toString());
//
//        //assertThat()

    }

    private UserProfileModel generateUserProfileModel() {
        Random random = new Random(System.currentTimeMillis());

        BasicInfo bInfo = new BasicInfo()
                .setGender(random.nextBoolean() ? "Male": "Female")
                .setBirthDate(LocalDateTime.now(ZoneOffset.UTC))
                .setLangs(Arrays.asList("Lang 1", "Lang 2"))
                .setPoliticalView("N/A").setReligiousView("Islam (Sunni)");

        ContactInfo cInfo = new ContactInfo();
        cInfo.setMobilePhones(Arrays.asList(new String[]{"8801712000000", "8801712000111", "8801712000333"}));
        cInfo.setAddress(new Address("Street 1", "Dhaka", "12345", "Etc"));
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

        return model;
    }

    private List<UserProfileModel> generate_Ten_UserProfileModel_List() {
        Random random = new Random(System.currentTimeMillis());
        RandomDateTime rd = new RandomDateTime();
        RandomCountryName rCountry = new RandomCountryName();
        RandomCityName rCity = new RandomCityName();
        RandomPhoneNumber rPhone = new RandomPhoneNumber();
        RandomLanguage rLang = new RandomLanguage();

        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).map(val -> {
            BasicInfo bInfo = new BasicInfo()
                    .setGender(random.nextBoolean() ? "Male": "Female")
                    .setBirthDate(rd.nextDateTime())
                    .setLangs(Arrays.asList(rLang.nextLangArray()))
                    .setPoliticalView("N/A").setReligiousView("Islam (Sunni)");

            ContactInfo cInfo = new ContactInfo()
                    .setMobilePhones(Arrays.asList(rPhone.nextListPhoneNumberArray()))
                    .setAddress(new Address("Street 1", "Dhaka", "12345", "Etc"))
                    .setSocialLinks(Arrays.asList(new SocialLink[]{
                            new SocialLink("Facebook", "Facebook Link"),
                            new SocialLink("Google", "Google Link")
                    }))
                    .setWebsites(Arrays.asList(new String[]{"www.web1.com", "www.web2.com"}))
                    .setEmails(Arrays.asList(new String[]{"email1@gmail.com", "email2@gmail.com"}));

            UserProfileModel userProfile = new UserProfileModel()
                    .setUserId("5eaf258bb80a1f03cd97a12" + val)
                    .setBasicInfo(bInfo)
                    .setContactInfo(cInfo)
                    .setBirthCity(rCity.nextCity())
                    .setBirthCountry(rCountry.nextCountry())
                    .setCurrentCity(rCity.nextCity())
                    .setCurrentCountry(rCountry.nextCountry())
                    .setProfilePicUrl("Profile Pic Url")
                    .setCoverImageUrl("Cover Pic Url")
                    .setAboutMe("About me text goes here");
            return userProfile;
        }).collect(Collectors.toList());
    }
}
