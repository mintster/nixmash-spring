spring-data
==========================

This project demonstrates configuration and various uses of Spring Data JPA with Hibernate. Blog posts covering various aspects of the application can be found at http://nixmash.com. 

##Implemented##

*Implementations listed below by version are found in their corresponding branch, v0.0.1, v0.0.2, etc. The Master branch of this repo contains ongoing development and may not contain features in prior versions.*

##v0.0.9##

- Several examples of Lazy Loading. See NixMash post [Approaches to JPA Lazy Loading](http://nixmash.com/java/approaches-to-jpa-lazy-loading/) for details
- First pass at creating schema DTO objects for use in updating and creating data
- Modified database schema to better adhere to database relationship best practices. Updates are reflected in Entities. 

##v0.0.8##

- h2database and mySQL JPA Data Configuration
- Profiles based on Enumerator value (ex: JpaDataConfig.MYSQL)

##v0.0.7##

- ApplicationContext and Data Retrieval Tests
- "Production" and "Dev" Spring Profiles
- Re-architected main() to load context, and a single bean to startup application 
- Broke out Production and Dev Profile Configurations, extend base SpringConfiguration class. Plan to redesign further
- Spring Profile set in Gradle.build bootRun task

##v0.0.6##

- Generated supplemental Contact, ContactTelDetail and Hobby Entities using the IntelliJ Persistence View GUI tools. (Models now contain "Entity" classname suffix.)
- Output based on new Entities, shown below
- Updated to _Spring 4.1.6, Spring-Boot 1.2.3_ 

![Image of Spring Data JPA Output v0.0.6](http://nixmash.com/x/pics/github/spring-data-0.0.6.png)

##v0.0.5##

- Broke-up the application into two separate IntelliJ Modules
- Root and child Gradle project configuration

##v0.0.4##

- Spring Data JPA Implementation
- Dual SpringJPAConfiguration and SpringHbnConfiguration classes
- Simultaneous use of both Spring Data JPA and Hibernate with Transaction Isolation
- Updated MySQL Schema with additional data
- Shared Model Entities between Hibernate and Spring Data JPA
- _Spring 4.0.6, Spring-Boot 1.1.4_

![Image of Spring Data JPA Output v0.0.4](http://nixmash.com/x/pics/github/spring-data-0.0.4.png)

##v0.0.3##

- Generic DAO Interface Implementation
- Added Service Facade
- Persistence Tier broken out to Domain Model, Data Access Object and Service Layers
- Data and Output identical to v0.0.1 
- _Spring 4.0.6, Spring-Boot 1.1.4_

##v0.0.2##

- Populating DataSource connection from Property File
- Using both @Value and @Autowired Environment properties
- Annotations with @ImportResource of app-context.xml file
- Custom SpringPropertiesUtil Class to retrieve overriden "username" System Property
- Populating @Autowired PropertyClass class properties in @Configuration class and retrieving the properties in another class
- _Spring 4.0.6, Spring-Boot 1.1.4_

![Image of Output v0.0.2](http://nixmash.com/x/pics/github/spring-hibernate-properties.png)
 
##v0.0.1##

- Changed XML Spring Configuration to Annotations
- Changed datasource from embedded H2 to MySql
- Lazy Fetching
- Named Queries
- Logging Configuration
- Gradle Build Configuration
- Inserting data
- _**Note:** v0.0.1 was created with Spring Boot v1.1.4. Using v1.2.x generates a compile error regarding JPA Entity handling._

![Image of Output v0.0.1](http://nixmash.com/x/pics/github/spring-hibernate0411.png)

##Installation##

Run the _setup.mysql_ script in the /doc/install directory to populate the database for using MySQL. Update Datasource connection properties in _/resources/META-INF/spring/mysql.properties_ file. Otherwise, the tests should fire up with a database.hbm2ddl.auto create-drop setting. The H2 create-data script is located in _/resources/db._  Build with gradle and run with gradle :bootRun or run tests with gradle :jpa:test.

##References##

**Version 0.0.1** of the app was based on Chapter #7 of Pro Spring, Fourth Edition from Apress, "Using Hibernate." (An excellent book, btw.) Here is [the book's listing on Apress](http://goo.gl/q2w50H). For Safari users, here is the online version of it [on Safari](http://goo.gl/TD6nuO).

The Generic DAO structure in **Version 0.0.3** was based on Chapter #2 of Spring in Practice by Willie Wheeler and Joshua White, "Data persistence, ORM, and transactions." Here is the [Chapter on Safari](http://goo.gl/Q9uoTl).

Multiple database JPA Configuration implemented in **Version 0.0.8** based largely on the work of Gordon Dickens' [Spring-Data-Demos "Profiles" project](https://goo.gl/IuaWoR). 

