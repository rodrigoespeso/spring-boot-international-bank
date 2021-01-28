# Spring Boot Rest Exercise

This is a Spring Boot development exercise that I did (actually still working on it) encouraged by some recruiter seeking for new employees.  

### Development Guide & Notes

· Step 0. Analysis; Database model design; research about money transfers and currency conversion; write this guide.  
· Step 1: Create a Maven project with [Spring Initializr](https://start.spring.io/) adding the necessary dependencies  
    + Spring Boot DevTools  
    + Spring Web  
    + Spring Data JPA  
    + H2 Database  
· Step 2: Import to Eclipse IDE and run it.  
· Step 3: Configure the project with a new GitHub repository; first commit.  
· Step 4: Define the architecture creating the package structure (Controller, Service, Repository, VO).  
· Step 5: Create entity classes; include the embedded DB (H2) and 'data.sql'.    
· Step 6: Define REST services and necessary methods in all classes; also create the exception classes.  
· Step 7: Development of the 'transfer' service.  
· Step 8: Development of the 'craete', 'find' and 'access' services.  
· Step 9: Include REST validations.  
· Step 10: Development of JUnit tests.  
· Step 11: Development of component test; include a Postman project with all the requests.  
· Step 12: Feel Free :)

### Interesting info for development

* [ISO 4217 Currency Codes](https://www.xe.com/es/iso4217.php#Y)
* [JavaMoney 'Moneta' User Guide](https://github.com/JavaMoney/jsr354-ri/blob/master/moneta-core/src/main/asciidoc/userguide.adoc#monetary-amounts)
* [H2 documentation](https://www.h2database.com/html/commands.html)
