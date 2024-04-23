# Yoga App !

to run the app on your computer (needed for e2e tests):
* verify that you have mysql server running on port 3306
* create database test
* populate it with the sql script under ```~/ressource/sql/```
* change db credentials in both spring config files
```application.properties``` and ```application-test.yml```
* install dependencies
> mvn clean install

* run the project
> mvn spring-boot:run


For launch and generate the jacoco code coverage:
> mvn clean test

* report will be generated at ```~/back/target/site/jacoco/index.html```

GL
