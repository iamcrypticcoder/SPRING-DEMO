package com.iamcrypticcoder.mongodbexample.persistence.repositories;

import com.iamcrypticcoder.mongodbexample.config.MongoDBConfig;
import com.iamcrypticcoder.mongodbexample.persistence.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@ContextConfiguration(classes = MongoDBConfig.class)
public class UserRepositoryIntegrationTest {

    // https://osherove.com/blog/2005/4/3/naming-standards-for-unit-tests.html

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepo;

    @Before
    public void setUp() {
        System.out.println("Before Test");
        mongoTemplate.dropCollection(UserModel.class);
        mongoTemplate.createCollection(UserModel.class,
        CollectionOptions.empty().schema(UserModel.buildMongoJsonSchema()));
        //mongoTemplate.createCollection(UserModel.class);
    }

    @Test
    public void should_Find_By_Id_When_Save() {
        UserModel userModel = generateUserModel();
        userModel.setCreatedDateTime(LocalDateTime.now());
        userModel.setUpdatedDateTime(LocalDateTime.now());

        //mongoTemplate.save(userModel);


        String savedId = userRepo.save(userModel).getId();

        Optional<UserModel> found = userRepo.findById(savedId);

        assert found.isPresent();
        assertThat(userModel.getEmail()).isEqualTo(found.get().getEmail());
    }

    @Test
    public void should_Find_By_Email_When_Save() {
        UserModel userModel = generateUserModel();
        userRepo.save(userModel);

        Optional<UserModel> found = userRepo.findByEmail("user1@gmail.com");
        assert found.isPresent();
        assertThat(found.get().getPassword()).isEqualTo(userModel.getPassword());
    }

    @Test
    public void should_Find_All_Users_When_Save_All() {
        List<UserModel> users = generate_Ten_UserModel_List();
        List<UserModel> savedModels = userRepo.saveAll(users);

        users.forEach(user -> {
            Optional<UserModel> found = userRepo.findByEmail(user.getEmail());
            assert found.isPresent();
            assertThat(found.get().getPassword()).isEqualTo(user.getPassword());
        });
    }

    @Test
    public void should_Find_All_By_PageRequest_When_Save_All() {
        List<UserModel> users = generate_Ten_UserModel_List();
        List<UserModel> savedModels = userRepo.saveAll(users);

        PageRequest firstPageRequest = PageRequest.of(0, 3);

        List<UserModel> foundUsers = userRepo.findAll(firstPageRequest).get().collect(Collectors.toList());

        assertThat(foundUsers.size() == 3);
        Iterator<UserModel> it1 = savedModels.iterator();
        Iterator<UserModel> it2 = foundUsers.iterator();
        while (it1.hasNext() && it2.hasNext())
            assertThat(it1.next().getId()).isEqualTo(it2.next().getId());
    }

    @Test
    public void should_Find_All_By_PageRequest_Sorted_By_Email_When_Save_All() {
        List<UserModel> users = generate_Ten_UserModel_List();
        List<UserModel> savedModels = userRepo.saveAll(users);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("email").descending());

        List<UserModel> foundUsers = userRepo.findAll(pageRequest).get().collect(Collectors.toList());

        assertThat(foundUsers.size() == 5);
        ListIterator<UserModel> it1 = savedModels.listIterator(savedModels.size());
        Iterator<UserModel> it2 = foundUsers.iterator();
        while (it1.hasPrevious() && it2.hasNext()) {
            assertThat(it1.previous().getId()).isEqualTo(it2.next().getId());
        }
    }

    @Test
    public void should_Find_All_By_Example_Matcher_When_Save_All() {
        List<UserModel> users = generate_Ten_UserModel_List();
        List<UserModel> savedModels = userRepo.saveAll(users);

        Example userExample = Example.of(new UserModel().setEmail("user9@gmail.com"));
        userRepo.findAll(userExample).forEach(u -> {
            assertThat(((UserModel)u).getId()).isEqualTo(savedModels.get(9).getId());
        });
    }

    @Test
    public void should_Find_All_Email_Starts_With_String_When_Save_All() {
        List<UserModel> users = generate_Ten_UserModel_List();
        List<UserModel> savedModels = userRepo.saveAll(users);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("email", startsWith().ignoreCase());

        userRepo.findAll(Example.of(new UserModel().setEmail("user0"), matcher)).forEach(u -> {
            assertThat(u.getId()).isEqualTo(savedModels.get(0).getId());
        });
    }

    @Test
    public void should_Update_By_Id_When_Save() throws InterruptedException {
        UserModel userModel = generateUserModel();
        userModel.setCreatedDateTime(LocalDateTime.now());
        //userModel.setUpdatedDateTime(LocalDateTime.now());

        String savedId = userRepo.save(userModel).getId();
        Thread.sleep(3000);

        String newFirstName = "First Name Changed";
        UserModel model = new UserModel()
                .setId(savedId)
                .setFirstName(newFirstName)
                .setIsEnabled(false)
                .setUpdatedDateTime(LocalDateTime.now());


        boolean isUpdated = userRepo.updateNotNull(model);

        Optional<UserModel> newFound = userRepo.findById(savedId);

        assertThat(newFound.get().getFirstName()).isEqualTo(newFirstName);
    }

    @Test
    public void should_Throw_DataIntegrityViolationException_When_Save_Username_With_Length_Below_6_Or_Above_32() {
        UserModel userModel = generateUserModel();
        userModel.setUsername(generateRandomString(5));

        assertThatThrownBy(() -> {
            userRepo.save(userModel);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Document failed validation");

        userModel.setUsername(generateRandomString(33));

        assertThatThrownBy(() -> {
            userRepo.save(userModel);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Document failed validation");
    }

    private UserModel generateUserModel() {
        return new UserModel()
                .setEmail("user1@gmail.com")
                .setPassword("123456")
                .setFirstName("User1")
                .setLastName("User1")
                .setGender(new Random(System.currentTimeMillis()).nextInt(2) == 0 ? "Male" : "Female")
                .setIsExpired(false)
                .setIsLocked(false)
                .setIsCredentialExpired(false)
                .setIsEnabled(true);
    }

    private List<UserModel> generate_Ten_UserModel_List() {
        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).map(val -> {
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
    }

    private String generateRandomString(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}