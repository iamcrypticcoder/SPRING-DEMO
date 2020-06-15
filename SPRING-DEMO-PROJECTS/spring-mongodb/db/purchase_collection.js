db.purchase.insertMany([
    {
        name: "Alison Farmer",
        age: 35,
        gender: "Male",
        product: "Cheese",
        paymentMethod: "Credit Card",
        amount: 15.00,
        isDelivered: true,
        date: ISODate("2018-01-22T10:05:45.000+0000")
    },
    {
        name: "Alison Farmer",
        age: 35,
        gender: "Male",
        product: "Bacon",
        paymentMethod: "Credit Card",
        amount: 30.00,
        isDelivered: true,
        date: ISODate("2018-01-22T10:05:45.000+0000")
    },
    {
        name: "Kitty Snow",
        age: 30,
        gender: "Female",
        product: "Face Cream",
        paymentMethod: "Credit Card",
        amount: 15.00,
        isDelivered: true,
        date: ISODate("2018-01-22T10:05:45.000+0000")
    },
    {
        name: "Santana Preston",
        age: 28,
        gender: "Female",
        product: "Lotion",
        paymentMethod: "Bank Transfer",
        amount: 17.00,
        isDelivered: false,
        date: ISODate("2018-01-22T10:05:45.000+0000")
    },
    {
        name: "Merle Hall",
        age: 39,
        gender: "Female",
        product: "Biscuits",
        paymentMethod: "Credit Card",
        amount: 12.00,
        isDelivered: true,
        date: ISODate("2019-01-22T10:05:45.000+0000")
    },
    {
        name: "Elliott Phelps",
        age: 40,
        gender: "Male",
        product: "Blub",
        paymentMethod: "Paypal",
        amount: 10.00,
        isDelivered: false,
        date: ISODate("2020-01-22T10:05:45.000+0000")
    },
    {
        name: "Sheila Lynch",
        age: 32,
        gender: "Female",
        product: "Baby Milk",
        paymentMethod: "Paypal",
        amount: 20.00,
        isDelivered: false,
        date: ISODate("2019-01-22T10:05:45.000+0000")
    },
    {
        name: "Wong Weber",
        age: 37,
        gender: "Male",
        product: "Shampoo",
        paymentMethod: "Credit Card",
        amount: 19.00,
        isDelivered: true,
        date: ISODate("2018-01-22T10:05:45.000+0000")
    },
    {
        name: "Leonardo Bruce",
        age: 45,
        gender: "Male",
        product: "Handwash",
        paymentMethod: "Cash",
        amount: 5.00,
        isDelivered: false,
        date: ISODate("2019-01-22T10:05:45.000+0000")
    },
    {
        name: "Leonardo Bruce",
        age: 45,
        gender: "Male",
        product: "Blub",
        paymentMethod: "Cash",
        amount: 10.00,
        isDelivered: false,
        date: ISODate("2019-01-22T10:05:45.000+0000")
    },
    {
        name: "Wong Weber",
        age: 37,
        gender: "Male",
        product: "Lotion",
        paymentMethod: "Debit Card",
        amount: 17.00,
        isDelivered: false,
        date: ISODate("2019-05-22T10:05:45.000+0000")
    },
    {
        name: "Wong Weber",
        age: 37,
        gender: "Male",
        product: "Face Cream",
        paymentMethod: "Debit Card",
        amount: 15.00,
        isDelivered: false,
        date: ISODate("2019-05-22T10:05:45.000+0000")
    }
]);

// Find total spent by each customer sorted by descending order
