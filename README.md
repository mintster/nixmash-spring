spring-hibernate
==========================

This project demonstrates using Hibernate with Spring Boot. Details on the use of Hibernate and Spring in this application can be found on my blog at http://nixmash.com. 

The app uses MySQL backend. Since this is for purpose of demonstration, configure connection in AppConfig class. Use setup.sql in /install to create schema and populate tables.

The app is based on Chapter #7 of Pro Spring, Fourth Edition from Apress, "Using Hibernate." (An excellent book, btw.) Here is [the book's listing on Apress](http://goo.gl/q2w50H). For fellow Safari users, here is the online version of it [on Safari](http://goo.gl/TD6nuO).

![Image of Successful Output](http://nixmash.com/x/pics/github/spring-hibernate0411.png)

##Implemented##

*Implementations listed below by version are found in their corresponding branch, v0.0.1, v0.0.2, etc. The Master branch of this repo contains ongoing development and may not contain features in prior versions.*

##v0.0.1##

- Changed XML Spring Configuration to Annotations
- Changed datasource from embedded H2 to MySql
- Lazy Fetching
- Named Queries
- Logging Configuration
- Gradle Build Configuration
- Inserting data
- _**Note:** v0.0.1 was created with Spring Boot v1.1.4. Using v1.2.x generates a compile error regarding JPA Entity handling._

##Installation##

Run the setup.sql script in the /install directory to populate the database. Configure the connection in AppConfig Class.

