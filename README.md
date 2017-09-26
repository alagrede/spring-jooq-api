# Import in Eclipse
```
mvn eclipse:eclipse
```
# Generate the database with Flywaydb
Configure your database settings in pom.xml
```
mvn flyway:migrate
```

# Build the application in generate Java JOOQ classes
```
mvn clean install -Dmaven.test.skip
```

# Start the application
Configure your database settings in application.properties
```
mvn spring-boot:run
```
