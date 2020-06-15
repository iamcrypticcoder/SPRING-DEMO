db.createCollection("POST_COMMENT", {
    validator: {
        $jsonSchema: {
            required: ["postId", "bucketIndex", "bucket"],
            properties: {
                postId: {
                    bsonType: "objectId"
                },
                bucketIndex: {
                    bsonType: "int"
                },
                bucket: {
                    bsonType: "array",
                    minItems: 0,
                    maxItems: 1000,
                    items: {
                        bsonType: "object",
                        required: ["userId", "text", "createdDate"],
                        properties: {
                            commentId: {
                                bsonType: "string",
                                description: "Format: [bucketId]_[randomString]"
                            },
                            userId: {
                                bsonType: "objectId"
                            },
                            text: {
                                bsonType: "string",
                                minLength: 1,
                                maxLength: 8000
                            },
                            createdDate: {
                                bsonType: "date"
                            }
                        }
                    }
                }
            }
        }
    }
});

// Create Index
db.POST_COMMENT.createIndex( {postId: 1, bucketIndex: 1}, { unique: true} );


// Create a bucket
db.POST_COMMENT.insertOne({
    postId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
    bucketIndex: NumberInt(0),
    bucket: []
})

// Add a comment into a bucket
db.POST_COMMENT.update(
    { bucketIndex: 0 },
    {
        $push: {
            bucket: {
                commentId: "5eaf258bb80a1f03cd97a3ad_lepf4f",
                userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                text: "This is comment one",
                createdDate: ISODate("2014-12-11T14:12:00Z")
            }
        }
    }
)

// Modify a comment in a bucket
db.POST_COMMENT.update(
    {
        "_id": ObjectId("5ec424a1ed1af85a50855964"),
        "bucket.commentId": "5eaf258bb80a1f03cd97a3ad_lepf4f"
    },
    {
        $set: {
            "bucket.$.text": "Comment text changed",
            "bucket.$.createdDate": ISODate("2015-12-11T14:12:00.000+0000")
        }
    }
)


// Remove a comment from a bucket
db.POST_COMMENT.update(
    {
        "_id": ObjectId("5ec424a1ed1af85a50855964")
    },
    {
        $pull: {
            "bucket": { commentId: "5eaf258bb80a1f03cd97a3ad_lepf4f"}
        }
    }
)

// 1. Find latest comment bucket of the post
db.POST_COMMENT.find(
    { postId: ObjectId("5eaf258bb80a1f03cd97a3ad") }
).sort({bucketIndex: -1}).limit(1);


/** Add a comment from user **/

/** Modify a comment from user **/

/** Remove a comment from user **/

/** Count comments of a post **/
db.getCollection("POST_COMMENT_BUCKET").aggregate([
    { $match: { postId: "5ec978b390d16f102cd2a0b8"} },
    { $group: { _id: null, count: { $sum: {$size: "$bucket"} } } }
])


// Sample Document
db.POST_COMMENT_BUCKET.insertMany([
    {
        postId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
        bucketIndex: NumberInt(0),
        bucket: []
    },
    {
        postId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
        bucketIndex: NumberInt(1),
        bucket: []
    }
])

db.POST_COMMENT.update(
    {
        "_id": ObjectId("5ec8defdd13b6054c172a734"),
    },
    {
        $push: {
            bucket: {
                $each: [
                    {
                        commentId: "5ec8defdd13b6054c172a734_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 1",
                        createdDate: ISODate("2014-12-11T00:00:00Z"),
                        updateDate: ISODate("2014-12-11T00:00:00Z")
                    },
                    {
                        commentId: "5ec8defdd13b6054c172a734_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 2",
                        createdDate: ISODate("2014-12-11T00:30:00Z"),
                        updateDate: ISODate("2014-12-11T00:30:00Z")
                    },
                    {
                        commentId: "5ec8defdd13b6054c172a734_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 3",
                        createdDate: ISODate("2014-12-11T01:00:00Z"),
                        updateDate: ISODate("2014-12-11T01:00:00Z")
                    },
                    {
                        commentId: "5ec8defdd13b6054c172a734_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 4",
                        createdDate: ISODate("2014-12-11T01:30:00Z"),
                        updateDate: ISODate("2014-12-11T01:30:00Z")
                    },
                    {
                        commentId: "5ec8defdd13b6054c172a734_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 5",
                        createdDate: ISODate("2014-12-11T02:00:00Z"),
                        updateDate: ISODate("2014-12-11T02:00:00Z")
                    },
                ]
            }
        }
    }
)

db.POST_COMMENT.update(
    {
        "_id": ObjectId("BUCKET_ID"),
    },
    {
        $push: {
            bucket: {
                $each: [
                    {
                        commentId: "BUCKET_ID_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 6",
                        createdDate: ISODate("2014-12-11T02:30:00Z"),
                        updateDate: ISODate("2014-12-11T02:30:00Z")
                    },
                    {
                        commentId: "BUCKET_ID_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 7",
                        createdDate: ISODate("2014-12-11T03:00:00Z"),
                        updateDate: ISODate("2014-12-11T03:00:00Z")
                    },
                    {
                        commentId: "BUCKET_ID_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 8",
                        createdDate: ISODate("2014-12-11T03:30:00Z"),
                        updateDate: ISODate("2014-12-11T03:30:00Z")
                    },
                    {
                        commentId: "BUCKET_ID_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 9",
                        createdDate: ISODate("2014-12-11T04:00:00Z"),
                        updateDate: ISODate("2014-12-11T04:00:00Z")
                    },
                    {
                        commentId: "BUCKET_ID_lepf4f",
                        userId: ObjectId("5eaf258bb80a1f03cd97a3ad"),
                        text: "This is comment 10",
                        createdDate: ISODate("2014-12-11T04:30:00Z"),
                        updateDate: ISODate("2014-12-11T04:30:00Z")
                    },
                ]
            }
        }
    }
)