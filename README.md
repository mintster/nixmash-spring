NixMash Spring
==========================

NixMash Spring is a Web Bootstrap 3 application that demonstrates the Spring Framework. Some of the technologies covered in NixMash Spring are:

*Spring Boot, Spring Data JPA, Spring Solr, Spring Web MVC, Spring Security, Spring Social, Hibernate, Mockito, Json, Gradle, Spring Mail, Jsoup, Multi-Profile Configurations, JPA Auditing, Webjars, Dandelion, Velocity Templating, @PreAuthorize Method Security, Custom Validators, @Controller Advice, Custom Exception Handling and Thymeleaf*

The slogan on the [NixMash Spring Demo Site](http://nixmashspring.daveburkevt.com/) is "Becoming Spring Masters Together" which is our goal for the application.

Accompanying nearly every new application feature is an accompanying blog post at [NixMash.com](http://nixmash.com) explaining the feature in detail. All NixMash Spring feature posts are listed below in their accompanying implementation version.

See the [Installation](#installation) section below on how to configure application settings to run NixMash Spring. Once configured you can run the Web Application with

`$ gradle mvc:bootRun`

A working demo of the site is online at **http://nixmashspring.daveburkevt.com.**

![Home Page in  Release 0.3.1](http://nixmash.com/x/pics/github/spring-home-0.3.1.1.png)

##Implemented##

*Implementations listed in the Release History below are found in their corresponding branch, v0.0.1, v0.0.2, etc. Newer branches may not contain features found in prior versions.*

##Release History##

- **[v0.3.2 -- Jsoup Play (In Progress)](#v032----jsoup-play-in-progress)** : *Jsoup Annotations Framework, Generic Type as Beans*
- **[v0.3.1 -- SiteOptions Properties, GitHub Buttons, Google Analytics](#v031----siteoptions-properties-github-buttons-google-analytics)** : *Key-Value to Class Properties, Admin entry of Google Analytics Tracking Id*
- **[v0.3.0 -- Spring Boot 1.3.4, NavBar Submenus](#v030----spring-boot-134-navbar-submenus)** : *"Demos" menu with Spring JPA and Solr Submenus, Spring Boot 1.3.4*
- **[v0.2.9 -- Site Administration Start, Profile Image Uploads, Email Services](#v029----site-administration-start-profile-image-uploads-email-services)** : *Administrative Dashboard based on [Gentella Admin](https://github.com/puikinsh/gentelella) Bootstrap 3 Template. Velocity Java Template Engine for Email formatting*
- **[v0.2.8 -- Spring Social ](#v028----spring-social)** : *Integrated Facebook and Twitter Authentication with existing site Spring Security Accounts*
- **[v0.2.7 -- Solr MVC](#v027----solr-mvc)** : *Web Solr Search, Autocomplete, Web Solr Facet, Solr Location Searching with Google Maps, Highlighting*
- **[v0.2.6 -- Solr Query Samplings](#v026----solr-query-samplings)** : *Solr Annotated, Criteria, Method Name Queries, Facet Queries*
- **[v0.2.5 -- Spring Boot 1.3](#v025----spring-boot-13)** : *Update all libraries to support v1.3*
- **[v0.2.4 -- Multiple Solr Repository Configurations](#v024----multiple-solr-repository-configurations)** : *Four different examples of configuring Solr Repositories*
- **[v0.2.3 -- Initial Solr Release](#v023----initial-solr-release)** : *Embedded and HTTP Solr Server profiles, SimpleSolrRepository CRUD Queries, Spring Annotation Solr Context Configuration*
- **[v0.2.2 -- Eclipse-Friendly NixMash Spring](#v022----eclipse-friendly-nixmash-spring)** : *Project Gradle build and configuration updated to support Eclipse IDE*
- **[v0.2.1 -- Spring MVC and JavaScript Samplings](#v021----spring-mvc-and-javascript-samplings)** : *Bootstrap Dialog Box supporting Spring User Authentication, Spring MVC JSON-Populated Multiselect plugin, SpringLoaded for Hot Reload development*
- **[v0.2.0 -- Spring Data JPA Auditing](#v020----spring-data-jpa-auditing)** : *Spring JPA Auditing with ZonedDateTime and MySQL support*
- **[v0.1.9 -- More Child Object Handling in MVC and Thymeleaf](#v019----more-child-object-handling-in-mvc-and-thymeleaf)** : *Hobbies added to Contact Forms, Custom Data Validation, CommandLineRunner example, Form JUnit Tests*
- **[v0.1.8 -- Child Object Processing (Phones), MVC Method Level Security](#v018----child-object-processing-phones-mvc-method-level-security)** : *PreAuthorize Annotation and Sp-EL MVC Method Security, Thymeleaf Forms*
- **[v0.1.7 -- Full MySQL Support, WAR Deployment](#v017----full-mysql-support-war-deployment)** : *Application deployed as WAR with deployment script, @ControllerAdvice*
- **[v0.1.6 -- Initial Release w/ Spring Security](#v016----initial-release-w-spring-security)** : *Spring Security with User Login and Registration, Role-based Contact Details viewing, External Properties file configuration*
- **[v0.1.5 -- Contact Add/Update Form, Dandelion Asset Bundling](#v015----contact-addupdate-form-dandelion-asset-bundling)** : *Dandelion Asset Bundling, Custom Favicon, Custom Exception Handling and all supporting Tests*
- **[v0.1.4 -- Contact Display, List and Search](#v014----contact-display-list-and-search)** : *jQuery, Bootstrap and Bootswatch Webjars with Spring, Contact web display with Thymeleaf*
- **[v0.1.3 -- Initial release w/ Thymeleaf](#v013----initial-release-w-thymeleaf)**
- **[v0.1.2 -- Multi-Module with JPA and MVC Modules](#v012----multi-module-with-jpa-and-mvc-modules)** : *MVC Module added to project, initial HomeController and ContactControllers, Pretty Formatted JSON with Jayway*
- **[v0.1.1 -- @ManyToMany Hobbies added to Contact Model](#v011----manytomany-hobbies-added-to-contact-model)**
- **[v0.1.0 -- @OneToMany Contact Phones added to Contact Model](#v010----onetomany-contact-phones-added-to-contact-model)**
- **[v0.0.9 -- Lazy Loading examples](#v009----lazy-loading-examples)**
- **[v0.0.8 -- Multi-database configuration w/ H2Console and MySQL](#v008----multi-database-configuration-w-h2console-and-mysql)**
- **[v0.0.7 -- Multi-profile configuration](#v007----multi-profile-configuration)** : *PROD and DEV Spring Profiles*
- **[v0.0.6 -- GUI Generated Entities and Diagram Examples](#v006----gui-generated-entities-and-diagram-examples)** : *IntelliJ IDE Persistence Tool Demos*
- **[v0.0.5 -- Hibernate and JPA Module structure](#v005----hibernate-and-jpa-module-structure)**
- **[v0.0.4 -- Initial Release with JPA](#v004----initial-release-with-jpa)**
- **[v0.0.3 -- DAO Implementation](#v003----dao-implementation)**
- **[v0.0.2 -- Using Spring Properties](#v002----using-spring-properties)** : *Using Property Files, @Value and @Autowired Environment properties, @ImportResource of app-context.xml demo*
- **[v0.0.1 -- Base Configuration in Spring and Hibernate](#v001----base-configuration-in-spring-and-hibernate)** : *Spring Annotation Configuration, Lazy Fetching, Named Queries, Logging, Gradle Build Configuration*

##v0.3.2 -- Jsoup Play (In Progress)##

- Jsoup Annotations Framework
- Classes with Generic Types as Beans
- [Post: Application and Testing Context with Multi-Project Dependencies in Spring](http://nixmash.com/java/application-and-testing-context-for-multi-project-dependencies-in-spring/)
- [Post: Jsoup Annotations: Introduction](http://nixmash.com/java/jsoup-annotations-introduction/)
- [Post: Jsoup Annotations: Examples](http://nixmash.com/java/jsoup-annotations-examples/)
- [Post: Configuring a Generic Type Class as a Spring Bean](http://nixmash.com/java/configuring-a-generic-type-class-as-a-spring-bean/)
- [Post: Variations on JSON Key-Value Pairs in Spring MVC](http://nixmash.com/java/variations-on-json-key-value-pairs-in-spring-mvc/)

[[back to top](#nixmash-spring)]

##v0.3.1 -- SiteOptions Properties, GitHub Buttons, Google Analytics##

- Configurable Google Analytics script and robots.txt file
- GitHub Repository Statistics with Spring RestTemplate, MVC and Thymeleaf. HTML and CSS from Mark Otto's [GitHub Buttons](https://github.com/mdo/github-buttons)
- "SiteOptions" Class Bean with properties generated from Apache Commons BeanUtils reflection
- Demonstrating @PostConstruct and @DependsOn in SiteOptions Bean
- Upgrade to Spring Boot 1.3.5
- *DATA SCHEMA UPDATE!* Added Site_Options table. See /install/mysql.setup for update scripts
- [Post: Serving Up a robots.txt File with Spring MVC](http://nixmash.com/java/serving-up-a-robots-txt-file-with-spring-mvc/)
- [Post: Serving Up Google Analytics Tracking in Spring and Thymeleaf](http://nixmash.com/java/serving-up-google-analytics-tracking-in-spring-and-thymeleaf/)
- [Post: Using Spring Security 4.x Testing Features](http://nixmash.com/java/using-spring-security-4-x-testing-features/)
- [Post: Updating Spring Component Properties at Runtime](http://nixmash.com/java/updating-spring-component-properties-at-runtime/)
- [Post: NixMash Spring Administration Area](http://nixmash.com/java/nixmash-spring-administration-area/)
- [Post: Displaying GitHub Stats with Spring RestTemplate and Thymeleaf](http://nixmash.com/java/displaying-github-stats-with-spring-resttemplate-and-thymeleaf/)
- [Post: Using @PostConstruct and @DependsOn in Spring](http://nixmash.com/java/using-postconstruct-and-dependson-in-spring/)
- [Post: Working With a Key-Value DataSource in Spring](http://nixmash.com/java/working-with-a-key-value-datasource-in-spring/)

![v0.3.1 GitHub Buttons](http://nixmash.com/x/pics/github/spring-data-0.3.1.png)

[[back to top](#nixmash-spring)]

##v0.3.0 -- Spring Boot 1.3.4, NavBar Submenus##

- Bootstrap3 NavBar Submenus
- Upgrade to Spring Boot 1.3.4, primarily to re-activate DevTools which had a bug in 1.3.3 stopping MySql services on app shutdown

[[back to top](#nixmash-spring)]

##v0.2.9 -- Site Administration Start, Profile Image Uploads, Email Services##

- Email Services with Contact Form page
- Mail Spring Boot Module
- Apache Velocity Template Engine for HTML-formatted MimeMessage Content
- Profile Image Uploading
- Gentelella Bootstrap 3 Admin Template [Github](https://github.com/puikinsh/gentelella)
- User Administration and Role Creation, Role Assignment, Role Deletion and removal from User Assignments
- Locked Authentication property to prevent removal and update of essential roles like ADMIN and USER
- Dandelion Datatables in Administration
- [Post: SendMail, Velocity and Spring MVC Contact Form JUnit Highlights](http://nixmash.com/java/sendmail-velocity-and-spring-mvc-contact-form-junit-highlights/)
- [Post: The Spring Dependency Project’s Application.Properties File](http://nixmash.com/java/the-spring-dependency-projects-application-properties-file/)
- [Post: Apache Velocity Email Templates with Spring](http://nixmash.com/java/apache-velocity-email-templates-with-spring/)
- [Post: A Spring MVC Contact Form](http://nixmash.com/java/a-spring-mvc-contact-form/)
- [Post: Getting Started with Spring Email Services](http://nixmash.com/java/getting-started-with-spring-email-services/)
- [Post: Exim4 Configuration Notes for Developers](http://nixmash.com/linux/exim4-configuration-notes-for-developers/)
- [Post: Profile Image Uploads: On Image File Storage](http://nixmash.com/java/profile-image-uploads-on-image-file-storage/)
- [Post: Profile Image Uploads: Thymeleaf Tricks](http://nixmash.com/java/profile-image-uploads-thymeleaf-tricks/)
- [Post: Profile Image Uploads: The Spring Parts](http://nixmash.com/java/profile-image-uploads-the-spring-parts/)
- [Post: Profile Image Uploads: Intro](http://nixmash.com/java/profile-image-uploads-intro/)
- [Post: Tracking Down a Bundle NullPointerException in Dandelion](http://nixmash.com/java/tracking-down-a-bundle-nullpointerexception-in-dandelion/)
- [Post: Using JavaScript with Dandelion Datatables](http://nixmash.com/java/using-javascript-with-dandelion-datatables/)
- [Post: Applying User Role Locking](http://nixmash.com/java/role-locking-in-nixmash-spring-administration/)
- [Post: Querying Object Children in Hibernate](http://nixmash.com/java/querying-object-children-in-hibernate/)
- [Post: Using Thymeleaf with JavaScript for Menu Highlighting](http://nixmash.com/java/using-thymeleaf-with-javascript-for-menu-highlighting/)
- [Post: Minimal Dandelion Datatables Spring Boot Configuration](http://nixmash.com/java/minimal-dandelion-datatables-spring-boot-configuration/)

![v0.2.9 Administration](http://nixmash.com/x/pics/github/spring-data-0.2.9.png)

[[back to top](#nixmash-spring)]

##v0.2.8 -- Spring Social##

- Google, Facebook and Twitter Sign-In
- Social Services app-id and secret keys configured in `external.properties` file
- Social ProviderSignInController SetApplicationUrl() supporting multiple environments.<br/>SetApplicationUrl() set with `--appurl` commandline argument. See post [Passing Arguments to Spring Boot](http://nixmash.com/java/passing-arguments-to-spring-boot/) for details.
- SignIn form with @InitBinder and custom validation for all fields
- Removed /webapp and reconfigured for Thymeleaf templates and static content in /resources
- [Post: Adding Google Sign-In to Spring Social](http://nixmash.com/java/adding-google-sign-in-to-spring-social/)
- [Post: Revisiting a Custom UserConnection JPA Object in Spring Social](http://nixmash.com/java/revisiting-a-custom-userconnection-jpa-object-in-spring-social/)
- [Post: The Case of the Spring Boot CSS Folder 404](http://nixmash.com/java/the-case-of-the-spring-boot-css-folder-404/)
- [Post: Resolving an H2 Deadlock in Spring Social](http://nixmash.com/java/resolving-an-h2-deadlock-in-spring-social/)
- [Post: Spring Social Additions to a Spring Security App](http://nixmash.com/java/spring-social-additions-to-a-spring-security-app/)
- [Post: NixMash Spring Social Web Flow Overview, First Pass](http://nixmash.com/java/nixmash-spring-social-web-flow-overview-first-pass/)
- [Post: Using External Properties for Spring Social Connection Keys](http://nixmash.com/java/using-external-properties-for-spring-social-connection-keys/)
- [Post: Passing Arguments to Spring Boot](http://nixmash.com/java/passing-arguments-to-spring-boot/)
- [Post: Spring Boot Social Showcase Starting Points](http://nixmash.com/java/spring-boot-social-showcase-starting-points/)
- [Post: Facebook Email Access in Spring Boot Social Showcase](http://nixmash.com/java/facebook-email-access-in-spring-boot-social-showcase/)

![v0.2.8 Social Sign-In Buttons](http://nixmash.com/x/pics/github/spring-data-0.2.8.png)

[[back to top](#nixmash-spring)]

##v0.2.7 -- Solr MVC##

- Replaced application-dev.properties/application-prod.properties logic with External Properties file `solr.properties`
- Configured for local and public Solr Server Url of http://solr/nixmashspring or a public url
- Updated Solr Index Installation "refreshSolr.sh" script to support new Url logic
- Updated Solr Server to 4.10.4
- Web display of Solr Facets
- Web Solr Search
- Solr Highlighting
- Solr Spatial Searching and Location Data handling
- Display Map with Google Map Javascript API, Thymeleaf and Solr Product Location data 
- [Post: Pagination with Spring MVC, Solr, Thymeleaf and Bootstrap](http://nixmash.com/java/pagination-with-spring-mvc-solr-thymeleaf-and-bootstrap/)
- [Post: Testing for Malformed Solr Query Strings](http://nixmash.com/java/testing-for-bad-solr-query-strings/)
- [Post: Solr Faceted Search Example in Spring and Thymeleaf](http://nixmash.com/java/solr-faceted-search-example-in-spring-and-thymeleaf/)
- [Post: Solr Search Term Autocomplete with Spring MVC](http://nixmash.com/java/solr-search-term-autocomplete-with-spring-mvc/)
- [Post: Retrieving Location Data with Spring Solr](http://nixmash.com/java/retrieving-location-data-with-spring-solr/)
- [Post: Location Queries with Spring Data Solr](http://nixmash.com/java/location-queries-with-spring-data-solr/)
- [Post: Thymeleaf Conditions, Comma Control and Glyphicons as List Styles](http://nixmash.com/java/thymeleaf-conditions-comma-control-and-glyphicons-as-list-styles/)
- [Post: Using Google Maps API with Thymeleaf and Spring](http://nixmash.com/java/using-google-maps-api-with-thymeleaf-and-spring/)
- [Post: A Google Multi-Marker Map Example with Solr, Spring MVC and Thymeleaf](http://nixmash.com/java/a-google-multi-marker-map-example-with-solr-spring-mvc-and-thymeleaf/)
- [Post: Highlighted Search Results with Spring Solr](http://nixmash.com/java/highlighted-search-results-with-spring-solr/)

![Solr Search in v0.2.7](http://nixmash.com/x/pics/github/spring-data-0.2.7.png)

[[back to top](#nixmash-spring)]

##v0.2.6 -- Solr Query Samplings##

- Tests for CRUD and Custom Solr Queries
- Console demonstrations of Named Method Queries, Java Criteria API, Annotated Queries, Named Queries and Solr Facets
- [Post: Simple CRUD Examples With Spring Data Solr](http://nixmash.com/java/simple-crud-examples-with-spring-data-solr/)
- [Post: A Spring Solr Criteria API Query Example](http://nixmash.com/java/a-spring-solr-criteria-api-query-example/)
- [Post: Spring Solr Named and Annotated Query Examples](http://nixmash.com/java/spring-solr-named-and-annotated-query-examples/)
- [Post: A Named Method Query With A Solr Twist](http://nixmash.com/java/a-named-method-query-with-a-solr-twist/)
- [Post: Changing the Size of Solr Facet Pages](http://nixmash.com/java/changing-the-size-of-solr-facet-pages/)
- [Post: Spring Solr Facet Query Examples](http://nixmash.com/java/spring-solr-facet-query-examples/)

![Solr Facet Query in v0.2.6](http://nixmash.com/x/pics/github/spring-data-0.2.6.png)

[[back to top](#nixmash-spring)]

##v0.2.5 -- Spring Boot 1.3##

- Spring-Boot-1.3.0.RELEASE

[[back to top](#nixmash-spring)]

##v0.2.4 -- Multiple Solr Repository Configurations##

- Four different examples of configuring Solr Repositories
- 1) Simple @NoRepositoryBean, 2) Derived @NoRepositoryBean, 3) Custom w/ @Repository, 4) SolrFactoryBean Shared Model Repository
- [Post: Four Spring Solr Repository Configurations](http://nixmash.com/java/four-spring-solr-repository-configurations/)

![Four Solr Repository Options in v0.2.4](http://nixmash.com/x/pics/github/spring-data-0.2.4.png)

[[back to top](#nixmash-spring)]

##v0.2.3 -- Initial Solr Release##

- New Solr Project based on Solr TechProducts Demo Collection in v5.3.2
- Embedded and HTTP Solr Server configuration (Profiles "dev" and "prod" respectively)
- Solr Data files and script for populating, refreshing Http and Embedded Solr indexes
- Method Name and SimpleSolrRepository base CRUD Queries
- Java Annotation Solr Context Configuration
- [Post: On Embedded Solr Paths and Http Solr Server Urls](http://nixmash.com/java/on-embedded-solr-paths-and-http-solr-server-urls/)
- [Post: Profile-Specific Application.Properties in Spring](http://nixmash.com/java/profile-specific-application-properties-in-spring/)
- [Post: Solr Http and Embedded Data Refresh for Development](http://nixmash.com/java/solr-http-and-embedded-data-refresh-for-development/)
- [Post: Spring Solr Minimum Daily Configuration Requirements](http://nixmash.com/java/spring-solr-minimum-daily-requirements/)

![Simple Solr Query results in v0.2.3](http://nixmash.com/x/pics/github/spring-data-0.2.3.png)

[[back to top](#nixmash-spring)]

##v0.2.2 -- Eclipse-Friendly NixMash Spring##

- Modified Gradle build.gradle files to eliminate Eclipse Build errors
- Added JDK JAVA_HOME properties to root Gradle .properties files
- Updated Gradle Wrapper to v2.7
- [Post: Prepping Eclipse Mars for Spring and Gradle](http://nixmash.com/java/prepping-eclipse-mars-for-spring-and-gradle/)
- [Post: Loading a Multi-Project Gradle Application in Eclipse](http://nixmash.com/java/loading-a-multi-project-gradle-application-in-eclipse/)

![NixMash Spring in Eclipse Project Explorer for v0.2.2](http://nixmash.com/x/pics/github/spring-data-0.2.2.png)

[[back to top](#nixmash-spring)]

##v0.2.1 -- Spring MVC and JavaScript Samplings##

- Bootstrap Dialog Box with requirement to be logged-in to view
- JQuery Bootstrap Multiselect Plugin with Spring MVC JSON-populated Options and postback
- SpringLoaded to Hot Reload Java Class updates w/o app restart
- [Post: BootRun Hot Deploy with Spring Loaded in IntelliJ](http://nixmash.com/java/bootrun-hot-deploy-with-spring-loaded-in-intellij/)
- [Post: JavaScript, Thymeleaf, Spring MVC and a Dialog Box](http://nixmash.com/java/javascript-thymeleaf-and-spring-mvc/)
- [Post: Spring MVC, JSON, CSRF, and a Bootstrap Multiselect Plugin](http://nixmash.com/java/spring-mvc-json-csrf-and-a-bootstrap-multiselect-plugin/)
- [Post: Populate Bootstrap Multiselect Options with JSON and Spring MVC](http://nixmash.com/java/populate-bootstrap-multiselect-plugin-with-json-and-spring-mvc/)

![Bootstrap Multiselect Plugin in v0.2.1](http://nixmash.com/x/pics/github/spring-data-0.2.1.png)

[[back to top](#nixmash-spring)]

##v0.2.0 -- Spring Data JPA Auditing##

- Spring Data JPA Auditing
- [Post: Spring JPA Auditing with ZonedDateTime and MySQL](http://nixmash.com/java/spring-jpa-auditing-with-zoneddatetime-and-mysql/)
- [Post: A Spring War Deployment Bash Script](http://nixmash.com/java/a-spring-war-deployment-bash-script/)

![MySQL JPA Audit Data in v0.2.0](http://nixmash.com/x/pics/github/spring-data-0.2.0.png)

[[back to top](#nixmash-spring)]

##v0.1.9 -- More Child Object Handling in MVC and Thymeleaf##

- Hobbies added to Contact Update Form w/ Custom Data Validation
- Demo of Spring Boot CommandLineRunner
- Contact Form tests added
- [Post: Using Spring Boot CommandLineRunner](http://nixmash.com/java/using-spring-boot-commandlinerunner/)
- [Post: Testing Custom Exception Handling Classes in Spring MVC](http://nixmash.com/java/testing-custom-exception-handling-classes-in-spring-mvc/)
- [Post: Handling Null Radio Button Data in Thymeleaf and Spring MVC](http://nixmash.com/java/handling-null-radio-button-data-in-thymeleaf-and-spring-mvc/)

![Radio Button Child Hobby Objects in v0.1.9](http://nixmash.com/x/pics/github/spring-data-0.1.9.png)

[[back to top](#nixmash-spring)]

##v0.1.8 -- Child Object Processing (Phones), MVC Method Level Security##

- Add, Update, Remove Contact Phones added to Contact Update form
- User Profile Page with @PreAuthorize to enforce owner-only plus administrator profile view
- [Post: Spring MVC Method Security with @PreAuthorize and Sp-EL](http://nixmash.com/java/spring-mvc-method-security-with-preauthorize-and-sp-el/)
- [Post: Consistent Fonts with Bootstrap Glyphicon and Thymeleaf](http://nixmash.com/java/consistent-fonts-with-bootstrap-glyphicon-and-thymeleaf/)
- [Post: Object Child Lists in Thymeleaf Forms and Spring MVC](http://nixmash.com/java/object-child-lists-in-thymeleaf-forms-and-spring-mvc/)

![Contact Phone Children in v0.1.8](http://nixmash.com/x/pics/github/spring-data-0.1.8.png)

[[back to top](#nixmash-spring)]

##v0.1.7 -- Full MySQL Support, WAR Deployment##

- MySQL Spring Security Support (mysql.setup file now located in /install folder)
- Deployed as a WAR at http://nixmashspring.daveburkevt.com
- @ControllerAdvice firing issue with AddViewControllers() fix
- User Login and Registration Tests
- [Post: Why Your @ControllerAdvice May Not Be Firing](http://nixmash.com/java/why-your-controlleradvice-may-not-be-firing/)
- [Post: Deploying Your Spring Boot WAR Application](http://nixmash.com/java/deploying-your-spring-boot-war-application/)
- [Post: Adding MySQL Spring Security to Existing H2 App](http://nixmash.com/java/adding-mysql-spring-security-to-existing-h2-app/)

[[back to top](#nixmash-spring)]

##v0.1.6 -- Initial Release w/ Spring Security##

- Spring Security with Login and User Registration. Supports Multiple Role Assignment with USERS, USER`AUTHORITIES and AUTHORITIES table storage
- H2 Console support at http://site/console. USER Role Access Denied to Console
- User must be authenticated to view Contact Details and Search pages
- External Properties for configuring options based on site public status (is demo site) _**To Configure Location of Properties File:** Set in Jpa/ApplicationSettings.class annotation_
- Create Contact Update/Create Submit restricted to ADMIN Role
- Tests for New User Registration and Form Validation
- _**Note:** v0.1.6 supports H2Database only. MySql support to be added._
- [Post: Using the H2 Console in Spring and IntelliJ](http://nixmash.com/java/using-the-h2-console-in-spring-and-intellij/)
- [Post: Beware Your Spring Security Principal’s Authorities Format](http://nixmash.com/java/beware-your-spring-security-principals-granted-authorities-list-format/)
- [Post: Spring Security in NixMash Spring App: The Parts You Can See](http://nixmash.com/java/spring-security-in-nixmash-spring-app-the-parts-you-can-see/)
- [Post: Welcome, User! in Thymeleaf and Spring](http://nixmash.com/java/welcome-user-in-thymeleaf-and-spring/)
- [Post: Enabling Submit on User Role in Thymeleaf and Spring](http://nixmash.com/java/enabling-submit-on-user-role-in-thymeleaf-and-spring/)
- [Post: The Old InstanceAlreadyExistsException JUnit Trick](http://nixmash.com/java/the-old-instancealreadyexistsexception-junit-trick/)
- [Post: External Properties File for Spring Web Site Configuration](http://nixmash.com/java/external-properties-file-for-spring-web-site-configuration/)
- [Post: Saving Multiple Authorities to Database on New User in Spring](http://nixmash.com/java/saving-multiple-authorities-to-database-on-new-user-in-spring/)
- [Post: User Registration Validation in Spring](http://nixmash.com/java/user-registration-validation-in-spring/)

![Registration form validation in v0.1.6](http://nixmash.com/x/pics/github/spring-data-0.1.6.png)

[[back to top](#nixmash-spring)]

##v0.1.5 -- Contact Add/Update Form, Dandelion Asset Bundling##

- Multi-use Contact Add/Update Form
- Fadeout Feedback Messages on Contact Add/Update with jQuery and Handlebars
- Using Dandelion with Asset Bundling to load jQuery-UI datePicker and theme
- Custom Favicon
- Custom Exception Handling and all supporting Tests
- [Post: Fadeout Feedback Messages in Spring MVC](http://nixmash.com/java/fadeout-feedback-messages-in-spring-mvc/)
- [Post: Java Dates, Dandelion, Thymeleaf, Hibernate and Spring](http://nixmash.com/java/java-dates-dandelion-thymeleaf-hibernate-and-spring/)
- [Post: Using Dandelion with Spring Boot and Thymeleaf](http://nixmash.com/java/using-dandelion-with-spring-boot-and-thymeleaf/)
- [Post: Testing for 404 and Custom Exceptions in Spring MVC](http://nixmash.com/java/testing-for-404-and-custom-exceptions-in-spring-mvc/)
- [Post: Custom 404 Exception Handling in Spring MVC](http://nixmash.com/java/custom-404-exception-handling-in-spring-mvc/)
- [Post: Including Webjars in IntelliJ Tomcat WAR Artifact](http://nixmash.com/java/including-webjars-in-intellij-tomcat-war-artifact/)
- [Post: Using Your Favorite Favicon in Spring MVC](http://nixmash.com/java/using-for-favorite-favicon-in-spring-mvc/)

![Using jQuery-UI with Dandelion in v0.1.5](http://nixmash.com/x/pics/github/spring-data-0.1.5.png)

[[back to top](#nixmash-spring)]

##v0.1.4 -- Contact Display, List and Search##

- jQuery, Bootstrap and Bootswatch Webjars
- Using Bootswatch Webjar with Spring
- Contact Display, List, and Search Form with all supporting Tests
- [Post: Adding Bootswatch Webjar in Spring and Thymeleaf](http://nixmash.com/java/adding-bootswatch-webjar-in-spring-and-thymeleaf/)
- [Post: Bootstrap Navbar Highlighting in Thymeleaf](http://nixmash.com/java/bootstrap-navbar-highlighting-in-thymeleaf/)
- [Post: Testing a Spring MVC Search Form](http://nixmash.com/java/testing-a-spring-mvc-search-form/)

![Bootstrap with All Contacts Display in Spring-Data MVC with Thymeleaf v0.1.4](http://nixmash.com/x/pics/github/spring-data-0.1.4.png)

[[back to top](#nixmash-spring)]

##v0.1.3 -- Initial release w/ Thymeleaf##

- Thymeleaf support added
- [Post: Thymeleaf Configuration with Spring Boot](http://nixmash.com/java/thymeleaf-configuration-with-spring-boot/)

![Spring-Data MVC with Thymeleaf in v0.1.3](http://nixmash.com/x/pics/github/spring-data-0.1.3.png)

[[back to top](#nixmash-spring)]

##v0.1.2 -- Multi-Module with JPA and MVC Modules##

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

[[back to top](#nixmash-spring)]

##v0.1.1 -- @ManyToMany Hobbies added to Contact Model##

- Addition of Hobbies, a @ManyToMany Entity with Contacts
- Adding Hobbies dynamically on new and updated Contacts, and as separate process
- Adding and removing Hobbies from Contact
- ContactDTO with Set<HobbyDTO>
- Tests covering above Hobby processes
- [Post: Working with the Many in a JPA @ManyToMany](http://nixmash.com/java/working-with-the-many-in-a-jpa-manytomany/)

[[back to top](#nixmash-spring)]

##v0.1.0 -- @OneToMany Contact Phones added to Contact Model##

- Examples of Updating, Deleting and Adding records with focus on @OneToMany relationship handling
- ContactDTO now containing Contact and ContactPhone Set
- Builder pattern added to ContactPhone Entity class
- Tests covering Contact updates, deletions and additions, on both contact and multiple contact phones
- [Post: Flexible H2 Database Persistence For Testing With Gradle](http://nixmash.com/java/flexible-h2-database-persistence-for-testing-with-gradle/)
- [Post: Adding a JPA Entity and its @OneToMany Children](http://nixmash.com/java/adding-a-jpa-entity-and-its-onetomany-children/)
- [Post: Updating the Many in a JPA @OneToMany](http://nixmash.com/java/updating-the-many-in-a-jpa-onetomany/)

[[back to top](#nixmash-spring)]

##v0.0.9 -- Lazy Loading examples##

- Several examples of Lazy Loading
- First pass at creating schema DTO objects for use in updating and creating data
- Modified database schema to better adhere to database relationship best practices. Updates are reflected in Entities. 
- [Post: Approaches to JPA Lazy Loading](http://nixmash.com/java/approaches-to-jpa-lazy-loading/)

[[back to top](#nixmash-spring)]

##v0.0.8 -- Multi-database configuration w/ H2Console and MySQL##

- h2database and mySQL JPA Data Configuration
- Profiles based on Enumerator value (ex: JpaDataConfig.MYSQL)
- [Post: A Spring JPA Configuration for Multiple Profiles](http://nixmash.com/java/a-spring-jpa-configuration-for-multiple-profiles/)

[[back to top](#nixmash-spring)]

##v0.0.7 -- Multi-profile configuration##

- ApplicationContext and Data Retrieval Tests
- "Production" and "Dev" Spring Profiles
- Re-architected main() to load context, and a single bean to startup application 
- Broke out Production and Dev Profile Configurations, extend base SpringConfiguration class. Plan to redesign further
- Spring Profile set in Gradle.build bootRun task
- [Post: Spring-Data GitHub App at v0.0.7 with Profiles](http://nixmash.com/java/spring-data-github-app-at-v0-0-7-with-profiles/)
- [Post: GetBeans in a Spring Main Method, A Better Way](http://nixmash.com/java/getbeans-in-a-spring-main-method-a-better-way/)
- [Post: Setting Spring Profiles in Gradle](http://nixmash.com/java/setting-spring-profiles-in-gradle/)

[[back to top](#nixmash-spring)]

##v0.0.6 -- GUI Generated Entities and Diagram Examples##

- Generated supplemental Contact, ContactTelDetail and Hobby Entities using the IntelliJ Persistence View GUI tools. (Models now contain "Entity" classname suffix.)
- Output based on new Entities, shown below
- Updated to _Spring 4.1.6, Spring-Boot 1.2.3_
- [Post: GUI Generated Entities for Spring-Data GitHub App v0.0.6](http://nixmash.com/java/gui-generated-entities-for-spring-data-github-app-v0-0-6/)
- [Post: IntelliJ ER Diagram Relationship Dialogs and the Code They Create](http://nixmash.com/java/intellij-er-diagram-relationship-dialogs-and-the-code-they-create/)
- [Post: Using IntelliJ Persistence View and ER Diagram Mapping Tools](http://nixmash.com/java/using-intellij-persistence-view-and-er-diagram-mapping-tools/)

![Image of Spring Data JPA Output v0.0.6](http://nixmash.com/x/pics/github/spring-data-0.0.6.png)

[[back to top](#nixmash-spring)]

##v0.0.5 -- Hibernate and JPA Module structure##

- Broke-up the application into two separate IntelliJ Modules
- Root and child Gradle project configuration
- [Post: Supporting Multiple Transaction Managers in Spring](http://nixmash.com/java/supporting-two-transaction-managers-in-spring/)

[[back to top](#nixmash-spring)]

##v0.0.4 -- Initial Release with JPA##

- Spring Data JPA Implementation
- Dual SpringJPAConfiguration and SpringHbnConfiguration classes
- Simultaneous use of both Spring Data JPA and Hibernate with Transaction Isolation
- Updated MySQL Schema with additional data
- Shared Model Entities between Hibernate and Spring Data JPA
- _Spring 4.0.6, Spring-Boot 1.1.4_
- [Post: JPA Added to Spring Data GitHub App v0.0.4](http://nixmash.com/java/jpa-added-to-spring-data-github-app-v0-0-4/)

![Image of Spring Data JPA Output v0.0.4](http://nixmash.com/x/pics/github/spring-data-0.0.4.png)

[[back to top](#nixmash-spring)]

##v0.0.3 -- DAO Implementation##

- Generic DAO Interface Implementation
- Added Service Facade
- Persistence Tier broken out to Domain Model, Data Access Object and Service Layers
- Data and Output identical to v0.0.1 
- _Spring 4.0.6, Spring-Boot 1.1.4_
- [Post: Spring-Data GitHub App at v.0.0.3 with Generic DAO Interface](http://nixmash.com/java/spring-hibernate-at-v-0-0-3-with-generic-dao-interface/)

[[back to top](#nixmash-spring)]

##v0.0.2 -- Using Spring Properties##

- Populating DataSource connection from Property File
- Using both @Value and @Autowired Environment properties
- Annotations with @ImportResource of app-context.xml file
- Custom SpringPropertiesUtil Class to retrieve overriden "username" System Property
- Populating @Autowired PropertyClass class properties in @Configuration class and retrieving the properties in another class
- _Spring 4.0.6, Spring-Boot 1.1.4_
- [Post: A Spring Property Handling Exercise](http://nixmash.com/java/a-spring-property-handling-exercise/)
- [Post: Spring-Data Demo App v0.0.2 on GitHub](http://nixmash.com/java/spring-hibernate-demo-app-v0-0-2-on-github/)

![Image of Output v0.0.2](http://nixmash.com/x/pics/github/spring-hibernate-properties.png)
 
[[back to top](#nixmash-spring)]

##v0.0.1 -- Base Configuration in Spring and Hibernate##

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

The application supports an H2 Profile (default) and a MySQL Profile. To run JPA Console app use **$gradle jpa:bootRun.** To run MVC Web app use **$gradle mvc:bootRun.** The Tomcat Server Port is set for **9000** (set in the MVC `application.properties` file) so go to **http://localhost:9000** to view the app in your browser. 

##Installation - Database Configuration##

To use MySQL run `setup.mysql` script in the `/install` directory to populate the database. Update Datasource connection properties in `/resources/META-INF/spring/mysql.properties` file. The H2 create-data script for the tests is located in `/resources/db.`

##Installation - External Property File Settings##

The JPA Project demonstrates using an external Property File. To Configure Location of Properties File, change the `@PropertySource` annotation setting in `Jpa/ApplicationSettings.class`. An example of `external.properties` is found in the `/install/samples` folder.

```java
@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/external.properties")
@ConfigurationProperties(prefix="external")
public class ApplicationSettings {
```
##Installation - Email Services##

The `Mail` Module contains the mail functionality. Like the JPA project above, an External `mail.properties` file defines SMTP HostName and other mail server properties. That file pathname is defined in the `Mail` Module `MailSettings.java` Class. An example of `mail.properties` is found in the `/install/samples` folder.

```java
@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/mail.properties")
@ConfigurationProperties(prefix = "mail")
public class MailSettings {
```

##Installation - Solr##

Configure Solr as normally on your development machine. Documents are included in `/dev/solr/docs` and scripts to populate the Solr Url and Embedded Servers located in `/dev/solr`. Script name: `refreshSolr.sh`. It contains additional installation instructions.

The Solr Project demonstrates both Embedded Solr and Http Solr ("dev" and "prod" Profiles respectively.) Configure these in an external `solr.properties` file. Same configuration as **external.properties** and **mail.properties** files discussed above. Set `solr.properties` file location in **Solr** project `common/SolrSettings.java` `@PropertySource` value.

##Installation - File Uploads##

You will need to create a physical `/files`  storage area to upload Profile Images. On a WAR deployment (like at [http://nixmashspring.daveburkevt.com](http://nixmashspring.daveburkevt.com)) you could use Apache2 mod_proxy to support those physical locations.  For development at `http://localhosty:9000` you could create a soft-link to the `/files` location. See [Profile Image Uploads: On Image File Storage](http://nixmash.com/java/profile-image-uploads-on-image-file-storage/) for details on creating a soft-link. See [Deploying Your Spring Boot WAR Application](http://nixmash.com/java/deploying-your-spring-boot-war-application/) on adding a `/files` alias in Apache2.

##References##

**Version 0.0.1** of the app was based on Chapter #7 of Pro Spring, Fourth Edition from Apress, "Using Hibernate." (An excellent book, btw.) Here is [the book's listing on Apress](http://goo.gl/q2w50H). For Safari users, here is the online version of it [on Safari](http://goo.gl/TD6nuO).

The Generic DAO structure in **Version 0.0.3** was based on Chapter #2 of Spring in Practice by Willie Wheeler and Joshua White, "Data persistence, ORM, and transactions." Here is the [Chapter on Safari](http://goo.gl/Q9uoTl).

Multiple database JPA Configuration implemented in **Version 0.0.8** based largely on the work of Gordon Dickens' [Spring-Data-Demos "Profiles" project](https://goo.gl/IuaWoR). 

Several components related to Thymeleaf and Security in **Version 0.1.x** from Arnaldo Piccnelli's [Enhanced Pet Clinic.](https://github.com/arnaldop/enhanced-pet-clinic)

Petri Kainulainen’s excellent book [Spring Data](https://www.packtpub.com/application-development/spring-data) from Packt Publishing and accompanying source code was a reference for Model Attribute handling and feedback messaging in **Version 0.1.5** as well as other features.

Two excellent source references for Spring Security which initially appeared in **Version 0.1.6** were Rob Winch's [gs-spring-security-3.2](https://github.com/rwinch/gs-spring-security-3.2) something and Bartosz Kielczewski's [example-spring-boot-security](https://github.com/bkielczewski/example-spring-boot-security).

Petri Kainulainen also served as a great reference on Solr in **Version 0.2.3.** See his excellent Spring Data Solr Tutorial(http://www.petrikainulainen.net/spring-data-solr-tutorial/) on his blog. Another good reference on Solr was Christoph Strobl's [spring-data-solr-examples](https://github.com/spring-projects/spring-data-solr-examples) on GitHub.

Spring Framework contributor Christoph Strobl's Spring Data Solr Showcase(https://github.com/christophstrobl/spring-data-solr-showcase) served as the basis of Solr Autocomplete found in **Version 0.2.7.**

The [Social Showcase for Spring Boot Sample Project](https://github.com/spring-projects/spring-social-samples/tree/master/spring-social-showcase-boot) was helpful in becoming oriented with Spring Social in **Version 0.2.8.** Petri Kainulainen remains my Go-To Spring Guru, here for Spring Social in his [4-Part Tutorial](http://www.petrikainulainen.net/spring-social-tutorial/).

The Administrative Dashboard introduced in **Version 0.2.9** is based on the [Gentelella Bootstrap Dashboard](https://github.com/puikinsh/gentelella) on GitHub.
