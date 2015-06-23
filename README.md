spring-data
==========================

This project demonstrates configuration and various uses of Spring Data JPA, Hibernate and MVC. It accompanies blog posts at http://nixmash.com on various aspects of the application and are listed below in their accompanying implementation version. 

##Implemented##

*Implementations listed below by version are found in their corresponding branch, v0.0.1, v0.0.2, etc. Recent branches may not contain features found in prior versions.*

##v0.1.4##

- jQuery, Bootstrap and Bootswatch Webjars
- Using Bootswatch Webjar with Spring
- Contact Display, List, and Search Form with all supporting Tests
- [Post: Adding Bootswatch Webjar in Spring and Thymeleaf](http://nixmash.com/java/adding-bootswatch-webjar-in-spring-and-thymeleaf/)
- [Post: Bootstrap Navbar Highlighting in Thymeleaf](http://nixmash.com/java/bootstrap-navbar-highlighting-in-thymeleaf/)
- [Post: Testing a Spring MVC Search Form](http://nixmash.com/java/testing-a-spring-mvc-search-form/)

![Bootstrap with All Contacts Display in Spring-Data MVC with Thymeleaf v0.1.4](http://nixmash.com/x/pics/github/spring-data-0.1.4.png)

##v0.1.3##

- Thymeleaf support added
- [Post: Thymeleaf Configuration with Spring Boot](http://nixmash.com/java/thymeleaf-configuration-with-spring-boot/)

![Spring-Data MVC with Thymeleaf in v0.1.3](http://nixmash.com/x/pics/github/spring-data-0.1.3.png)

##v0.1.2##

- MVC Module Added
- JPA and MVC Modules wired as single Spring Application
- Referencing Test Source from JPA module in MVC module tests
- Initial HomeController and ContactController classes
- Pretty Formatted JSON output in Web Browser
- [Post: Deployable WARs in Spring Boot, IntelliJ and Gradle](http://nixmash.com/java/deployable-wars-in-spring-boot-intellij-and-gradle/)
- [Post: An IntelliJ Multi Module Spring Boot MVC Configuration](http://nixmash.com/java/an-intellij-multi-module-spring-boot-mvc-configuration/)
- [Post: Referencing Test Classes in Another IntelliJ Module](http://nixmash.com/java/referencing-test-classes-in-another-intellij-module/)
- [Post: Pretty Formatted JSON in Spring Web MVC](http://nixmash.com/java/pretty-formatted-json-in-spring-web-mvc/)

![Spring-Data Does MVC in v0.1.2](http://nixmash.com/x/pics/github/spring-data-0.1.2.png)

##v0.1.1##

- Addition of Hobbies, a @ManyToMany Entity with Contacts
- Adding Hobbies dynamically on new and updated Contacts, and as separate process
- Adding and removing Hobbies from Contact
- ContactDTO with Set<HobbyDTO>
- Tests covering above Hobby processes
- [Post: Working with the Many in a JPA @ManyToMany](http://nixmash.com/java/working-with-the-many-in-a-jpa-manytomany/)

##v0.1.0##

- Examples of Updating, Deleting and Adding records with focus on @OneToMany relationship handling
- ContactDTO now containing Contact and ContactPhone Set
- Builder pattern added to ContactPhone Entity class
- Tests covering Contact updates, deletions and additions, on both contact and multiple contact phones
- [Post: Flexible H2 Database Persistence For Testing With Gradle](http://nixmash.com/java/flexible-h2-database-persistence-for-testing-with-gradle/)
- [Post: Adding a JPA Entity and its @OneToMany Children](http://nixmash.com/java/adding-a-jpa-entity-and-its-onetomany-children/)
- [Post: Updating the Many in a JPA @OneToMany](http://nixmash.com/java/updating-the-many-in-a-jpa-onetomany/)


##v0.0.9##

- Several examples of Lazy Loading
- First pass at creating schema DTO objects for use in updating and creating data
- Modified database schema to better adhere to database relationship best practices. Updates are reflected in Entities. 
- [Post: Approaches to JPA Lazy Loading](http://nixmash.com/java/approaches-to-jpa-lazy-loading/)

##v0.0.8##

- h2database and mySQL JPA Data Configuration
- Profiles based on Enumerator value (ex: JpaDataConfig.MYSQL)
- [Post: A Spring JPA Configuration for Multiple Profiles](http://nixmash.com/java/a-spring-jpa-configuration-for-multiple-profiles/)

##v0.0.7##

- ApplicationContext and Data Retrieval Tests
- "Production" and "Dev" Spring Profiles
- Re-architected main() to load context, and a single bean to startup application 
- Broke out Production and Dev Profile Configurations, extend base SpringConfiguration class. Plan to redesign further
- Spring Profile set in Gradle.build bootRun task
- [Post: Spring-Data GitHub App at v0.0.7 with Profiles](http://nixmash.com/java/spring-data-github-app-at-v0-0-7-with-profiles/)
- [Post: GetBeans in a Spring Main Method, A Better Way](http://nixmash.com/java/getbeans-in-a-spring-main-method-a-better-way/)
- [Post: Setting Spring Profiles in Gradle](http://nixmash.com/java/setting-spring-profiles-in-gradle/)

##v0.0.6##

- Generated supplemental Contact, ContactTelDetail and Hobby Entities using the IntelliJ Persistence View GUI tools. (Models now contain "Entity" classname suffix.)
- Output based on new Entities, shown below
- Updated to _Spring 4.1.6, Spring-Boot 1.2.3_ 
- [Post: GUI Generated Entities for Spring-Data GitHub App v0.0.6](http://nixmash.com/java/gui-generated-entities-for-spring-data-github-app-v0-0-6/)
- [Post: IntelliJ ER Diagram Relationship Dialogs and the Code They Create](http://nixmash.com/java/intellij-er-diagram-relationship-dialogs-and-the-code-they-create/)
- [Post: Using IntelliJ Persistence View and ER Diagram Mapping Tools](http://nixmash.com/java/using-intellij-persistence-view-and-er-diagram-mapping-tools/)

![Image of Spring Data JPA Output v0.0.6](http://nixmash.com/x/pics/github/spring-data-0.0.6.png)

##v0.0.5##

- Broke-up the application into two separate IntelliJ Modules
- Root and child Gradle project configuration
- [Post: Supporting Multiple Transaction Managers in Spring](http://nixmash.com/java/supporting-two-transaction-managers-in-spring/)

##v0.0.4##

- Spring Data JPA Implementation
- Dual SpringJPAConfiguration and SpringHbnConfiguration classes
- Simultaneous use of both Spring Data JPA and Hibernate with Transaction Isolation
- Updated MySQL Schema with additional data
- Shared Model Entities between Hibernate and Spring Data JPA
- _Spring 4.0.6, Spring-Boot 1.1.4_
- [Post: JPA Added to Spring Data GitHub App v0.0.4](http://nixmash.com/java/jpa-added-to-spring-data-github-app-v0-0-4/)

![Image of Spring Data JPA Output v0.0.4](http://nixmash.com/x/pics/github/spring-data-0.0.4.png)

##v0.0.3##

- Generic DAO Interface Implementation
- Added Service Facade
- Persistence Tier broken out to Domain Model, Data Access Object and Service Layers
- Data and Output identical to v0.0.1 
- _Spring 4.0.6, Spring-Boot 1.1.4_
- [Post: Spring-Data GitHub App at v.0.0.3 with Generic DAO Interface](http://nixmash.com/java/spring-hibernate-at-v-0-0-3-with-generic-dao-interface/)

##v0.0.2##

- Populating DataSource connection from Property File
- Using both @Value and @Autowired Environment properties
- Annotations with @ImportResource of app-context.xml file
- Custom SpringPropertiesUtil Class to retrieve overriden "username" System Property
- Populating @Autowired PropertyClass class properties in @Configuration class and retrieving the properties in another class
- _Spring 4.0.6, Spring-Boot 1.1.4_
- [Post: A Spring Property Handling Exercise](http://nixmash.com/java/a-spring-property-handling-exercise/)
- [Post: Spring-Data Demo App v0.0.2 on GitHub](http://nixmash.com/java/spring-hibernate-demo-app-v0-0-2-on-github/)

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
- [Post: New Spring-Data Demo on GitHub](http://nixmash.com/java/new-spring-hibernate-demo-on-github/)

![Image of Output v0.0.1](http://nixmash.com/x/pics/github/spring-hibernate0411.png)

##Installation##

The application supports an H2 Profile (default) and a MySQL Profile. To run JPA Console app use **$gradle jpa:bootRun.** To run MVC Web app use **$gradle mvc:bootRun.** The Tomcat Server Port is set for **8084** (set in the MVC _application.properties_ file) so go to **http://localhost:8084** to view the app in your browser. 

##Database Configuration##

To use MySQL run _setup.mysql_ script in the /doc/install directory to populate the database. Update Datasource connection properties in _/resources/META-INF/spring/mysql.properties_ file. The H2 create-data script for the tests is located in _/resources/db._

##References##

**Version 0.0.1** of the app was based on Chapter #7 of Pro Spring, Fourth Edition from Apress, "Using Hibernate." (An excellent book, btw.) Here is [the book's listing on Apress](http://goo.gl/q2w50H). For Safari users, here is the online version of it [on Safari](http://goo.gl/TD6nuO).

The Generic DAO structure in **Version 0.0.3** was based on Chapter #2 of Spring in Practice by Willie Wheeler and Joshua White, "Data persistence, ORM, and transactions." Here is the [Chapter on Safari](http://goo.gl/Q9uoTl).

Multiple database JPA Configuration implemented in **Version 0.0.8** based largely on the work of Gordon Dickens' [Spring-Data-Demos "Profiles" project](https://goo.gl/IuaWoR). 

