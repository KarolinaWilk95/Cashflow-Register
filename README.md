# Receivables and Payables Register

The project was created with the aim of improving work efficiency at my previous position by developing a Receivables
and Payables Register that is automatically updated. Its purpose is to assist individuals responsible for entering
documents into the system and supervising their proper processing.

### The application includes several key features:

* Generating reports to support analytical work and enable better data management.

* Grouping payment due dates for improved monitoring of payables and receivables.

Automating this process will save time and increase work efficiency while ensuring greater accuracy in data processing.

In this application are available roles:
* DOCUMENT_CIRCULATION
* CONTROLLING
* MANAGEMENT

To work with hardcoded data in the project, I used currency purchase rate: 
* USD -> 3,78 PLN
* EUR -> 4,28 PLN

## Getting started: ##

*To properly use app on your computer, please follow instructions and commands in terminal.*
1. Clear the target directory, build the project, and package the resulting JAR file into the target directory.

```courseignore
.\mvnw.cmd clean package
```
2. Create, start, and attach a container to the service with a functioning connection to PostgreSQL. This command also build an image based on instructions in Dockerfile.

```courseignore
docker compose up
```

## Technology Stack ##

+ Java
+ Spring Boot Framework
+ PostgreSQL
+ Maven
+ Lombok
+ JUnit
+ Testcontainers
+ Mockito
+ FlyWay
+ Docker
+ Postman
+ MapStruct
+ OAuth2.0 
+ JWT