db.createCollection("POST", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId", "text", "privacy"],
            properties: {
                userId: {
                    bsonType: "string"
                },
                text: {
                    bsonType: "string"
                },
                privacy: {
                    bsonType: "string",
                    enum: ["Public", "Friends", "Private"]
                }
            }
        }
    }
});
