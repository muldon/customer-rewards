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
- /customer/list: returns a list of active customer names (2 for this example)
- /customer/statement/{customerId}/{lastNDays}: given a customer id (e.g. 1) and a number of previous days (e.g. 90), returns the statement of the points the user per month. The app has supports not only for the gain of points by user (e.g. when shopping) but also for their spending. For this example though, the focus is on the gain. A complete URL example to get a user statement for the last 90 days is [here](http://161.97.114.171:8085/customer/statement/1/90).

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
- cr_user: contains the users, that could be admin users or customers. In this example 
- cr_order: contains the orders (transactions). 
- customer_rewards: contains the points accumulated during the transactions acording to three parameters (explained ahead). Each row in this table could represent a gain of points or a expense indicated by the operation_id, though in this app example only the gains are showed. 

When the app is initialized, a configuration class automatically refresh the data, keeping two users and inserting orders with randon values and dates. As the orders are inserted, the reward points are calculated and inserted in the customer_rewards table. The images below show examples of these data:

- cr_user: 

![cr_users](https://github.com/muldon/customer-rewards/blob/master/cr_user.png)

- cr_order: 

![customer_rewards](https://github.com/muldon/customer-rewards/blob/master/cr_order.png)

- customer_rewards:

![customer_rewards](https://github.com/muldon/customer-rewards/blob/master/customer_rewards.png)

obs. in a more complete app, there could exist a product table containing products that the customer would purchase and an order_item table, containing the items of the order. This app abstract the logics in order to make it simple and focus on the rewards feature. 

parameters ...




## Prerequisites

Note: all the experiments were conducted over a server equipped with 32 GB RAM, 3.1 GHz on four cores and 64-bit Linux Mint Cinnamon operating system. We strongly recommend a similar or better hardware environment. The operating system however could be changed. 

 

## Running the tool mode 1 - replication package

### Downloading files:
Download CROKAGE files [here](http://lascam.facom.ufu.br/companion/crokage/crokage-replication-package.zip) and place in a folder preferable in your home folder, ex /home/user/crokage-replication-package. 

Check your instalation. Make sure your crokage folder (ex /home/user/crokage-replication-package) contains this structure:

```.
..
./data 
./tmp (4 files)   
   ./soContentWordVec.txt
   ./bigMap.txt
   ./soIDFVocabulary.txt
   ./soUpvotedPostsWithCodeAPIsMap.txt
crokage.jar
main.properties
```
Note: for now we only provide the replication package. The complete source code will be released soon. 

### Configuring the dataset
1. Download the Dump of SO [here](http://lascam.facom.ufu.br/companion/crokage/dump2018crokagereplicationpackage.backup) (Dump of June 2018). This is a preprocessed dump, downloaded from the [official web site](https://archive.org/details/stackexchange) containing the main tables we use (we only consider Java posts in this initial version). **Postsmin** table (representing **posts** table) has extra columns with the preprocessed data used by CROKAGE (**processedtitle, processedbody, code, processedcode**). 

2. On your DB tool, create a new database named stackoverflow2018crokagereplicationpackage. This is a query example:
```
CREATE DATABASE stackoverflow2018crokagereplicationpackage
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;
```
3. Restore the downloaded dump to the created database. PgAdmin has this feature. Right click on the created database -> Restore... select the dump (dump2018crokagereplicationpackage.backup).

Obs: restoring this dump would require at least 10 Gb of free space. If your operating system runs in a partition with insufficient free space, create a tablespace pointing to a larger partition and associate the database to it by replacing the "TABLESPACE" value to the new tablespace name: `TABLESPACE = tablespacename`. 

4. Assert the database is sound. Execute the following SQL command: `select id, title,body,processedtitle,processedbody,code, processedcode from postsmin po limit 10`. The return should list the main fields for 10 posts. 




### Setting Parameters

Edit `main.properties` and set the **required** following parameters: 

`CROKAGE_HOME` = the root folder of the project (ex /home/user/CROKAGE-replication-package) where you must place the jar and `main.properties`.

`TMP_DIR`      = the folder location of the models. This folder is the tmp folder from previous step (ex /home/user/crokage-replication-package/tmp).

`spring.datasource.username` = your db user

`spring.datasource.password=` = your db password

`action` = you have 2 main options. `action=runApproach`: run the retrieval mechanism and output metrics for our ground truth. CROKAGE will show metrics for the chosen data set (below). `action=extractAnswers`: build the summaries containing the solutions. 

`dataSet` = You also have 2 main options. `dataSet=selectedqueries-test48`: test (48 queries). `dataSet=selectedqueries-training49`: training (49 queries). 


The other parameters are **optional**:

`subAction` = The set of the API extractors separated by '|'. Ex: `rack|biker|NLP2Api|`.

`numberOfComposedAnswers`= number of answers used to compose summaries. (Default=3)

If you want to reproduce other baselines except CROKAGE, please refer to our paper to more details about how to set the weights.


### Running the jar 
Open a terminal, go to the project folder and run the following command: `java -Xms1024M -Xmx32g -jar crokage.jar --spring.config.location=./main.properties` . This command uses the file `main.properties` to overwrite the default parameters which must be set as described above.


### Results

The results are displayed in the terminal/console but also stored in the database in tables **metricsresults**. The following query should return the results:  
```
select * from metricsresults
```



## Running the tool mode 2 - Obtaining the solutions via REST interface
We provide a REST interface to enable other researchers to use CROKAGE as a baseline or repeat, improve or refute our results. If you are interested in obtaining the solutions for your programming tasks, you can call this interface from within your applications. For this, make a POST request to http://isel.ufu.br:8080/crokage/query/getsolutions, set in the header the "Content-Type" to "application/json" and pass the parameters in JSON format as follows:

```

{
 "numberOfComposedAnswers":10,
 "queryText":"how to insert an element array in a given position" 
}
```



### Example 1 
This is an example of making a REST call to CROKAGE using the [RESTED](https://chrome.google.com/webstore/detail/rested/eelcnbccaccipfolokglfhhmapdchbfg) plugin for Chrome. 

![Example of REST call to CROKAGE](https://github.com/muldon/CROKAGE-replication-package/blob/master/RESTED-POST.png)

The result is a JSON containing the answers with explanations. 

### Example 2
We provide a Java implementation for invoking the rest interface. For this, follow the following steps:

1. Clone this project into your local machine: git clone https://github.com/muldon/CROKAGE-replication-package.git

2. Import the maven project into your IDE (i.e., Eclipse).

3. Right click on CrokageInitializer.java on Package Explorer-> Run As -> Java Application. 

If you desire to change the parameters, access `application.properties` file under `src/main/resources/config/`. Once you run the application, the demo client will invoke the remove REST api and obtain the solutions for the queries in the list. 



## Tool
We implemented our approach in form of a [tool](http://isel.ufu.br:9000/) to assist developers with their daily programming issues. The figure below shows the tool architecture. We follow a REST (Representational State Transfer) architecture. The tool is in beta version and only provide solutions for Java language, but we expect to release the full version soon.  

![CROKAGE's architecture](https://github.com/muldon/CROKAGE-replication-package/blob/master/tool-architecture.png)




## Citation

If you intend to use this work, please cite us:


```
@inproceedings{silva2019recommending,
  title={Recommending comprehensive solutions for programming tasks by mining crowd knowledge},
  author={Silva, Rodrigo FG and Roy, Chanchal K and Rahman, Mohammad Masudur and Schneider, Kevin A and Paixao, Klerisson and de Almeida Maia, Marcelo},
  booktitle={Proceedings of the 27th International Conference on Program Comprehension},
  pages={358--368},
  year={2019},
  organization={IEEE Press}
}
```



## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.txt) file for details


[Java 1.8]: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
[Postgres 9.4]: https://www.postgresql.org/download/
[PgAdmin]: https://www.pgadmin.org/download/



