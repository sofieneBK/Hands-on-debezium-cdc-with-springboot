db = db.getSiblingDB('test');

db.phones.insertMany([
    {
        "name": "IPhone 15",
        "price": 999,
        "description": "Premium Apple smartphone with powerful features."
    },
    {
        "name": "Samsung Galaxy S23",
        "price": 899,
        "description": "Premium Android smartphone with powerful features."
    },
    {
        "name": "Google Pixel 6",
        "price": 799,
        "description": "Flagship phone with top-notch camera and performance."
    }
])

db.computers.insertMany([
    {
        "name": "MacBook Pro",
        "price": 1499,
        "description": "High-performance laptop for professionals."
    },
    {
        "name": "Dell XPS 15",
        "price": 1299,
        "description": "Powerful laptop with stunning display and long battery life."
    },
    {
        "name": "HP Spectre x360",
        "price": 1099,
        "description": "Versatile 2-in-1 laptop with impressive design and performance."
    }
])


db.books.insertMany([
    {
        "name": "To Kill a Mockingbird",
        "price": 12.99,
        "description": "Classic novel by Harper Lee exploring themes of racial injustice."
    },
    {
        "name": "1984",
        "price": 9.99,
        "description": "Dystopian novel by George Orwell depicting a totalitarian society."
    },
    {
        "name": "The Great Gatsby",
        "price": 14.99,
        "description": "F. Scott Fitzgerald's masterpiece capturing the Jazz Age in America."
    }
])