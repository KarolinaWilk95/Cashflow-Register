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

### Example path to generate token ###

`/api/token?username=Karolina&roles=CONTROLLING`

#### Example to use PatchMapping:

```courseignore
[
    {
        "op":"replace", 
        "path":"/contractorName",
        "value":"XYZ"
    }
]
```

#### Available reports:

1. Overdue receivables report - show receivables that have not been paid on time

`/api/register/receivables/overdue`

2. Overdue receivables group by contactors report - show receivables that have not been paid on time but group by
   contactor name and sum up all unpaid invoices

`/api/register/receivables/overdue/grouped`

3. Aging report - sort receivables bu due date, helping to assess the risk of counterparty default.

`api/register/receivables/aging`


4. Overdue payables report - show payables that have not been paid on time

`/api/register/payables/overdue`

5. Overdue payables group by contactors report - show payables that have not been paid on time but group by
   contactor name and sum up all unpaid invoices

`/api/register/payables/overdue/grouped`

6. Aging report - sort payables by due date, helping to assess the risk of counterparty default.

`api/register/payables/aging`

7. Summary report -  displays information about the receivables and payables summary, the 3 highest-value documents, and the 3 most indebted or creditor contractors.

`api/register`


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