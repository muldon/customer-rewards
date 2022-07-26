# Customer Rewards
Simple Java back-end spring-boot web program requested by [siono](https://www.siono.io) as part of its hiring process.  

## About

### The Challenge
A retailer offers a rewards program to its customers awarding points based on each recorded purchase as follows:
 
For every dollar spent over $50 on the transaction, the customer receives one point.
In addition, for every dollar spent over $100, the customer receives another point.
Ex: for a $120 purchase, the customer receives
(120 - 50) x 1 + (120 - 100) x 1 = 90 points
 
Given a record of every transaction during a three-month period, calculate the reward points earned for each customer per month and total. 
- Make up a data set to best demonstrate your solution
- Check solution into GitHub

Write a REST API that calculates and returns the reward points in the language of your choice.
If you are writing in Java, Using Spring Boot is highly recommended but not mandatory.

The complete challenge description is in the [CodingChallenge file](https://github.com/muldon/customer-rewards/blob/master/CodingChallenge.docx). 

### Demo 
The app implementation can be found [here](http://161.97.114.171:8085/swagger-ui/index.html). This is a server hosted in [Contabo](https://contabo.com). [Swagger](https://swagger.io/) was used to provide the details about the endpoints. Basically the app provides two get methods:
- /customer/list: returns a list of active customer names (2 for this example). Example [here](http://161.97.114.171:8085/customer/list).
- /customer/statement/{customerId}/{lastNDays}: given a customer id (e.g. 1) and a number of days, e.g. 90 (last 3 months), returns the statement of the points of the user per month and a total. The app has support not only for the gain of points by user (e.g. when shopping) but also for their spending. For this example though, the focus is on the gain. Example [here](http://161.97.114.171:8085/customer/statement/1/90): get a user statement for the last 3 months. 

The endpoints do not require a *content-type* in the request. Thus, you can test it using not only the [Swagger](https://swagger.io/) interface, but also your browser (for the two available get requests) or any other REST API tool, such as the RESTED browser plugin (available for chrome and firefox) or [Postman](https://www.postman.com/). The image bellow shows how to make a REST request using the [firefox RESTED plugin](https://addons.mozilla.org/en-US/firefox/addon/rested/).

![DER](https://github.com/muldon/customer-rewards/blob/master/rested-plugin.png)


## Technologies involved
 
| Technology  | Version                                |
| -------------- | ---------------------------------------------------------------------- |
| [Java OpenJDK](https://jdk.java.net/11/) | 11     
| [Maven](https://maven.apache.org)  | 3.6.3 |
| [Spring Boot](https://spring.io/projects/spring-boot)  | 2.5.6 |
| [OpenAPI](https://swagger.io/specification/) | 3.0.3
| [Lombok](https://projectlombok.org/download) | 1.18.24
| [PostgreSQL](https://www.postgresql.org)   | 13.5 |
| [Docker](https://www.docker.com/)   | 20.10.7 |
| [Jenkins](https://www.jenkins.io/)   | 2.346.1 LTS |

obs. Java 11 was used instead of the 17th due to compatibitity issues with Jenkins. 


### Database  

The [Ondras](https://ondras.zarovi.cz/sql/demo/) tool was used to model the database as follows:

![DER](https://github.com/muldon/customer-rewards/blob/master/cr-der.png)

The app contains three tables: 
- cr_user: contains the users, that could have different roles (admin, customer, etc). The app focuses on customers (roleId = 2).
- cr_order: contains the orders (transactions). 
- customer_rewards: contains the points accumulated during the transactions acording to three parameters (explained ahead). Each row in this table could represent a gain of points or a expense indicated by the operation_id, though in this app example only the gains are showed. 

When the app is initialized, a configuration class automatically refresh the data, keeping two users and inserting orders with randon values and dates. As the orders are inserted, the reward points are calculated and inserted in the customer_rewards table. The images below show examples of these data:

- cr_user: 

![cr_users](https://github.com/muldon/customer-rewards/blob/master/cr_user.png)

- cr_order: 

![customer_rewards](https://github.com/muldon/customer-rewards/blob/master/cr_order.png)

- customer_rewards:

![customer_rewards](https://github.com/muldon/customer-rewards/blob/master/customer_rewards.png)

obs. in a more complete app, there could exist a product table containing products that the customer would purchase and an order_item table, containing the items of the order. This app abstracts the logics in the order table to make it simple and let us focus on the rewards feature. 


## Running the app 

### Running without containers

#### Prerequisites

1. [Java 11+](https://jdk.java.net/11/) 
2. [Maven 3+](https://maven.apache.org)
3. [pgAdmin 4](https://www.pgadmin.org/download/)
4. [PostgreSQL 13.5](https://www.postgresql.org)  

Check that your postgres is up and running at port 5432 (default), and Java, Maven and pgAdmin are correctly installed. Clone the project into a local folder (e.g. /home/jack/cr). Watch out that the features described are in the **master branch**. Then, open your pgAdmin4 and create a new databased called *sionodb*. Then, right click on the database -> Restore. Select the file *cr_db.backup* of this repo. Then check that three tables were built, along with their sequences. The tables contain records but we are more interested in their DDL, since everytime the app is run, the data is refreshed. Then, edit your *application.context* file and set your database password at *spring.datasource.password*. Then, on the console, go to the local folder where the project is located (e.g. /home/jack/cr) and type: 
```sh
$ mvn package spring-boot:run
```
This command will trigger the several JUnit tests and run the application. The tests are set to pass. You then can access the application in your browser: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
  

### Running with containers

#### Prerequisites

1. [Docker 20.10.7+](https://www.docker.com/) 
2. [Docker Compose 3.9+](https://www.docker.com/) 

Download the file `customer-rewards-all-local.yml`. Then, edit the file and change the path "/home/rodrigo/projects/customer-rewards/logs" to your volume path, such as "/home/jack/tmp" and make sure that your path has writing permissions. Also, make sure no other containers or processes are running at ports 5432 and 8085. Then, at the same folder as the file, run:

 ```sh
 $ docker-compose -f customer-rewards-all-local.yml up -d
 ```
Check that the containers are up and running: 

```sh
$ docker ps
```

You must be able to see the the *customer-rewards* container and the *cr_postgres_container* container running. But the database is not created yet, so despite the app is running, it can not connect to the database, thus it will fail to run. We need to create the database. Download the file `cr_db.backup` (e.g. to your home folder /home/jack) and copy it to the postgres container as follows: 

```sh
$ docker cp /home/jack/cr_db.backup cr_postgres_container:/cr_db.backup
```
then, connect to the container:
```sh
$ docker exec -it cr_postgres_container bash 
```
then, create the database: 
```sh
$ su - postgres
$ psql
$ CREATE DATABASE "sionodb" WITH OWNER = postgres ENCODING = 'UTF8' CONNECTION LIMIT = -1;
$ \q
```
and finally restore the database:
```sh
$ pg_restore -U postgres -h localhost -d sionodb --no-owner -1 /cr_db.backup
```
and with the credentials as in your yml file (e.g. "mypqdbpass"). 
```sh
$ mypqdbpass
```
After some time, the *customer-rewards* should be up and running. Check it: 
```sh
$ docker logs customer-rewards
```
If it presents an error, don't worry, the container did not restart yet. You just need to try again in a few seconds. After it restarts, the log shall present the message at the end: "Started CustomerRewardsApp in... seconds". 

Then, access the URL in your browser: [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html). Note that we run the app at port 8085 as in your yml file.  

### Changing the app parameters
You can change the app parameters by editing the file `application.context` under `src/main/resources`. If you are running it thought the containers, you can instead override them in your yml file. The specific parameters of the app, other than logging, spring framework, database and swagger are: 

- scoring.factor
- rewardBaseline1
- rewardBaseline2

These parameters are used in the formula to calculate the reward points as follows: *(purchaseValue - rewardBaseline1) x scoring.factor + (purchaseValue - rewardBaseline2) x scoring.factor = total points*


## JUnit tests
Two test classes are available. First, the *RewardsTest* class illustrate two purchase scenarios, where the first is the one given in the challenge. Second, the *OrdersTest* class contains several usual methods in a real app, such as CRUD methods, a search with filters containing pagination parameters (commonly used in tables), the change of an order status, etc. The tests are usually part of a CI/CD process. 


## CI/CD
The project contains a Jenkins file with a basic CI/CD pipeline. This pipeline is triggered by every commit to github. Basically the pipeline runs some maven commands (e.g. clean, install, test), which makes maven to execute de tests of the app. Besides, when it detects the token `#build` in the commit message it also builds a docker image with sequenced generated ID and then pushes the image to a [Docker Hub](https://hub.docker.com) repository. It also notifies by email in case the build fails to run. This pipeline assumes the host contains postgres up and running with the customer-rewards database.  

## Health Check
This projects uses [UptimeRobot](https://uptimerobot.com) to health check the application. A monitor was set to check on the [statement feature](http://161.97.114.171:8085/customer/statement/1/90) every 5 minutes. If the aplication stops running, an email alert is sent to estudantecomp@gmail.com.  



## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE.txt) file for details


[Java 1.8]: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
[Postgres 9.4]: https://www.postgresql.org/download/
[PgAdmin]: https://www.pgadmin.org/download/



