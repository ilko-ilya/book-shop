# "Online Book-Shop"


Welcome to the Online Book Shop project! This online platform provides a seamless experience for book enthusiasts, allowing users to explore a vast collection of books, manage their carts, and place orders.

## Functionality

### For Non-Authenticated Users
 - Registration: Create a new user account by providing essential details such as email, password, name, and shipping address.
   ``` 
   POST: /api/auth/register
   ``` 
####   Example JSON registration request body:
    ```
    {
      "email": "jamesbond@gmail.com",
      "password": "jamesbond007",
      "repeatPassword": "jamesbond007",
      "firstName": "James",
      "lastName": "Bond",
      "shippingAddress": "4 Privet Drive"
    }
- Login: Log in to the platform using your registered email and password.

   ```
   POST: /api/auth/login
   ```
####   Example JSON login request body:

    ```
    {
      "email": "jamesbond@gmail.com",
      "password": "jamesbond007"
    }
    ```

### For Users with Role USER
 - Browse Books: Explore a list of available books on the platform.
   ``` 
   GET /api/books
   ``` 

 - Get Book Details: View detailed information about a specific book.
   ``` 
   GET /api/books/{id}
   ``` 

 - Explore Categories: Discover different book categories offered by the shop.
   ``` 
   GET /api/categories
   ``` 

 - Get Category Details: Obtain details about a specific book category.
   ``` 
   GET /api/categories/{id}
   ``` 

 - Explore Books by Category: Find a list of books within a specific category.
   ``` 
   GET /api/categories/{id}/books
   ``` 

 - View Cart: See the contents of your shopping cart.
   ``` 
   GET /api/cart
   ``` 

 - Add to Cart: Add a book to your cart along with the desired quantity.
   ``` 
   POST /api/cart
   ``` 

 - Update Cart Item: Modify the quantity of a book in your cart.
   ``` 
   POST /api/cart/cart-items/{cartItemId}
   ``` 

 - Remove Cart Item: Remove a book from your cart.
   ``` 
   DELETE /api/cart/cart-items/{cartItemId}
   ``` 

 - View Orders: Check a list of your previous orders.
   ``` 
   GET /api/orders
   ``` 

 - Place Order: Make a new order for the selected books.
   ``` 
   POST /api/orders
   ``` 

 - View Order Items: Review the items in a specific order.
   ``` 
   GET /api/orders/{orderId}/items
   ``` 
 - View Specific Order Item: Get detailed information about a particular item in an order.
   ```
   GET /api/orders/{orderId}/items/{itemId}
   ```

### For Users with Role ADMIN
 - Add New Book: Add a new book to the store's collection with details like title, author, category, and price.
   ```
   POST /api/books
   ```

 - Update Book Details: Modify details of a specific book.
   ```
   PUT /api/books/{id}
   ```

 - Delete Book: Remove a book from the store's collection.
   ```
   DELETE /api/books/{id}
   ```

 - Add New Category: Introduce a new book category.
   ```
   POST /api/categories
   ```

 - Update Category Details: Modify details of a specific category.
   ```
   PUT /api/categories/{id}
   ```

 - Delete Category: Remove a category from the store.
   ```
   DELETE /api/categories/{id}
   ``` 

 - Update Order Status: Change the status of a specific order.
   ```
   PATCH /api/orders/{id}
   ```

Explore these functionalities to make the most of your Online Book Store experience! Happy reading! 📚🌟


# Technologies:
 - Java 17
 - Spring Boot, Spring Security, Spring data JPA
 - REST, Mapstruct
 - MySQL, Liquibase
 - Maven, Docker
 - Lombok, Swagger
 - Junit, Mockito, testcontainers

# Project Benefits
   - Wide Book Selection:Our project provides access to an extensive collection of books across various genres, authors, and themes, ensuring a diverse range of choices for users.
   - User-Friendly Interface: An intuitive interface and a straightforward registration and checkout process make our project easy to use, even for new users.
   - Data Security: The implementation of technologies such as Spring Security ensures robust protection of user data, making the platform a secure choice.
   - Flexibility and Scalability: The project's architecture allows for easy addition of new books and categories, updates to functionality, and scalability to meet evolving needs.

# Addressed Challenges
   - Convenient Online Bookstore: Our project addresses the need for a convenient online book shopping experience, eliminating the necessity of visiting physical stores.

   - Enhanced User Experience: The system offers an attractive user interface, making the book selection and purchasing process enjoyable and efficient.

   - Efficient Order Management: Users can easily track their orders, while administrators can efficiently manage the store's inventory and monitor order statuses.

   - Flexibility for Administrators: Administrators can effortlessly add and update books, manage categories, and monitor order statuses, ensuring a seamless operation.

# Why Choose "Online Book-Shop"
   - Modern Technologies: Utilizing Java 17, Spring Boot, and other contemporary technologies ensures high performance and reliability.

   - Documentation and Support: The project comes with comprehensive documentation, and our support team is always ready to assist with questions and issues.

   - Active Development: Our team is committed to continuous improvement and regularly introduces new features for an enhanced user experience.


 my_new_branch_book-shop



