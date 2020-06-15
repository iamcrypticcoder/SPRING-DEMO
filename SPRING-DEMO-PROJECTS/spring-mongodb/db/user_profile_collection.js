db.createCollection("USER_PROFILE", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId"],
            properties: {
                userId: {
                    bsonType: "string"
                },
                basicInfo: {
                    bsonType: "object",
                    properties: {
                        gender: {
                            enum: ["Male", "Female", "Unspecified"]
                        },
                        birthDate: {
                            bsonType: "date"
                        },
                        langs: {
                            bsonType: "array"
                        },
                        religiousView: {
                            bsonType: "string"
                        },
                        politicalView: {
                            bsonType: "string"
                        }
                    }
                },
                contactInfo: {
                    bsonType: "object",
                    properties: {
                        mobilePhones: {
                            bsonType: "array"
                        },
                        address: {
                            bsonType: "object",
                            properties: {
                                street: { bsonType: "string"},
                                city: { bsonType: "string"},
                                zipCode: { bsonType: "string"},
                                neighborhood: { bsonType: "string"}
                            }
                        },
                        socialLinks: {
                            bsonType: "array"
                        },
                        websites: {
                            bsonType: "array"
                        },
                        emails: {
                            bsonType: "array"
                        }
                    }
                },
                birthCity: {
                    bsonType: "string"
                },
                birthCountry: {
                    bsonType: "string"
                },
                currentCity: {
                    bsonType: "string"
                },
                currentCountry: {
                    bsonType: "string"
                },
                profilePicUrl: {
                    bsonType: "string"
                },
                coverImageUrl: {
                    bsonType: "string"
                },
                aboutMe: {
                    bsonType: "string"
                }
            }
        }
    }
});

// Create Index
db.USER_PROFILE.createIndex( { "userId": 1 }, {unique:true} );

// Sample Document
db.USER_PROFILE.insertOne({
    userId: "5eaf258bb80a1f03cd97a3ad",
    basicInfo: {
        gender: "Male",
        birthDate: "2020-05-05", //new Date(2019, 03, 29, 13, 12),
        langs: ["Bangla", "English"],
        religiousView: "Islam (Sunni)",
        politicalView: "N/A"
    },
    contactInfo: {
        mobilePhones: ["01911276607", "01520084330"],
        address: {
            street: "321 Zigatola",
            city: "Dhaka",
            zipCode: "12345",
            neighborhood: "Etc"
        },
        socialLinks: [
            {
                platform: "Facebook",
                link: "Link of Facebook"
            },
            {
                platform: "Twitter",
                link: "Link of Twitter"
            }
        ],
        websites: ["www.site1.com", "www.site2.com"],
        emails: ["email1@gmail.com", "email2@gmail.com"]
    },
    birthCity: "Dhaka",
    birthCountry: "Bangladesh",
    currentCity: "Dhaka",
    currentCountry: "Bangladesh",
    profilePicUrl: "profilePicUrl",
    coverImageUrl: "coverImageUrl"
});