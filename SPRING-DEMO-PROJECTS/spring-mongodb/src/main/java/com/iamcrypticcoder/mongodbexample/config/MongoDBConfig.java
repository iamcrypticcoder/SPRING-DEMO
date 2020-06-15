package com.iamcrypticcoder.mongodbexample.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.BasicInfoConverter;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.ContactInfoConverter;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.UserModelConverter;
import com.iamcrypticcoder.mongodbexample.persistence.model.converter.UserProfileModelConverter;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.iamcrypticcoder.mongodbexample.persistence.model.converter")
@EnableMongoRepositories(basePackages="com.iamcrypticcoder.mongodbexample.persistence.repositories")
@Slf4j
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Autowired
    private UserModelConverter userModelConverter;

    @Autowired
    private UserProfileModelConverter profileModelConverter;

    @Override
    public MongoClient mongoClient() {
        /*
        MongoClientOptions.Builder builder =  new MongoClientOptions.Builder();
        builder.connectionsPerHost(50);
        builder.writeConcern(WriteConcern.JOURNALED);
        builder.readPreference(ReadPreference.secondaryPreferred());
        MongoClientOptions options = builder.build();
        */

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://" + host + ":" + port))
                .writeConcern(WriteConcern.JOURNALED)
                .readPreference(ReadPreference.secondaryPreferred())
                .codecRegistry(codecRegistry).build();

        return MongoClients.create(settings);
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(userModelConverter);
        converterList.add(profileModelConverter);
        return new MongoCustomConversions(converterList);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    /*
    @Bean
    public BasicInfoConverter basicInfoConverter() {
        return new BasicInfoConverter();
    }

    @Bean
    public ContactInfoConverter contactInfoConverter() {
        return new ContactInfoConverter();
    }

     */
}
