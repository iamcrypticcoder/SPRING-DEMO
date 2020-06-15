db.createCollection("USER", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["email", "password", "firstName", "lastName", "gender",
                "isExpired", "isLocked", "isCredentialExpired", "isEnabled"],
            properties: {
                username: {
                    bsonType: "string",
                    minLength: 6,
                    maxLength: 32,
                },
                email: {
                    bsonType: "string",
                    pattern: "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\.([a-zA-Z]{2,5})$"
                },
                firstName: {
                    bsonType: "string",
                    maxLength: 32
                },
                lastName: {
                    bsonType: "string",
                    maxLength: 32
                },
                gender: {
                    enum: ["Male", "Female", "Unspecified"]
                },
                isExpired: {
                    bsonType: "bool"
                },
                isLocked: {
                    bsonType: "bool"
                },
                isCredentialExpired: {
                    bsonType: "bool"
                },
                isEnabled: {
                    bsonType: "bool"
                }
            }
        }
    }
});

// Create Index
db.USER.createIndex( {"email": 1, "username": 1}, {unique:true} );
