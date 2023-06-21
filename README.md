
 # SadnaTeam17A: trading system

## Introduction
Welcome to the trading system
A trading system (market) is a system that enables a trading infrastructure between sellers and buyers. The system is complex
From a collection of stores. A store contains identifying details and product inventory, where each product has different characteristics. my users
The system visits the market for buying, selling, and management purposes. System users play roles
Different in the system: sellers, buyers and managers. A user can play more than one role in the system at any moment
Given. A user can open a store, then he gets the role of a seller who is the owner of the store. A shop owner is allowed
define a purchase policy in the store (consisting of different purchase methods, and rules for their activation and integration), manage the
the product inventory and define a discount policy (that is, different types of discounts, and rules for applying discounts).
and to combine them. Also, a store owner may appoint additional store owners and store managers for his store. When the owner
store from which a manager, he is required to determine which management privileges are given to him. In addition, there are system administrators
whose job it is to address buyer and seller inquiries, maintain fair and legal trading through the system and take care
for proper operation of the system.
Users can get store information and store product information. You can search for products directly accordingly
to different criteria. The information about products includes the details of the purchase policy and the discount policy regarding them.
A user can register for the trading system, then he becomes a subscriber. A market visitor who has not identified himself as a subscriber is
guest.
The system can turn to external services for the purpose of performing targeted operations, such as collection of funds and delivery
products. Referral to external services is made by transferring transaction details to these services and receiving approval
or postponement of the performance of the service.
The trading system also provides real-time alerts to users in cases where their attention is required.
In addition, the system must meet various security and usability requirements to protect the users' information and take care
For a good user experience that will make users continue to use the system.

## Technologies Used

This project is implemented using a combination of Java, React, and the Spring Framework.

- Java: The backend logic and business rules are written in Java.
- React: The frontend user interface is built using React, a JavaScript library for building user interfaces.
- Spring Framework: The project utilizes the Spring Framework 


## Prerequisites

Before getting started, ensure that you have the following prerequisites:

- Node.js: You will need to have Node.js installed on your machine. If you don't have it, you can download it from [https://nodejs.org](https://nodejs.org).

## Getting Started

To initialize the system, follow the steps below:

1. Clone the project repository from https://github.com/Massader/SadnaTeam17A.git.
2. Open a terminal or command prompt and navigate to the project directory.
3. Run the following command to install the project dependencies:  npm install

## Configuration File: config.properties

The `config.properties` file contains the relevant parameters for the initialization of the system's external services in the Supply & payment module.


### Configuration Parameters

The `config.properties` file include the following configuration parameter:

- `url`: https://php-server-try.000webhostapp.com/
  
The `application.properties` file include the following configuration parameter:

Database Connection Settings
spring.datasource.url=jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7626875
spring.datasource.username=postgres
spring.datasource.password=okokokok
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

## Initial State File

The initial state file defines the initial state of the system. It sits in the resources folder and is written in json format . This file will be used to populate the system with the initial data.

To create the initial state file, create the json file state.json and populate it with a list of objects, each of which is a function to be run at startup.

{
    "name": "function-name",
    "arguments": ["u1", "Abc123"],
    "retVal": "$ignore"
}

Above is the format for the function, with the function-name being one of the following: login, register, logout, createStore, getUserByUsername, appointStoreOwner, appointStoreManager. Arguments is a list of string or integer arguments that will be accepted as arguments of the functions, in order, and without the clientCredentials. retVal is the name for the return value of the function, to be saved by the system. If the return value is "$ignore", the return value will be ignored by the start-up state file runner.


## Starting the System

To start the system, run the following command in the terminal or command prompt: npm run start

This command will open the trading system web for you.
