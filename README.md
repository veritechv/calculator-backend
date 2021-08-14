**Introduction**
Welcome to the backend piece for the Web Calculator project.

This is a Java project that offers a REST API that let us use a set of services, or
math operations, like addition and subtraction.

Among other things we can:
- Add new users
- Get a list of the executable services
- The logs showing the services executed by a user
- Manage the life-cycle of the services

----
**Requirements**
In order to run this projec we need:
- Java 1.8+
- Postgres 9.4+
- Maven 3+
- Docker. If we want to run an already compiled app.

----
**Project Structure**
This is how the code structure looks:
```
.
├── dockerfiles
├── images
└── src
    ├── main
    │         ├── java
    │         │         └── org
    │         │             └── challenge
    │         │                 └── calculator
    │         │                     ├── controller
    │         │                     │         └── exception
    │         │                     ├── entity
    │         │                     ├── enums
    │         │                     ├── exception
    │         │                     ├── interceptor
    │         │                     ├── model
    │         │                     ├── repository
    │         │                     ├── security
    │         │                     │         ├── jwt
    │         │                     │         └── utils
    │         │                     ├── services
    │         │                     └── utils
    │         └── resources
    │             ├── db
    │             │         └── migration
    │             ├── static
    │             └── templates
    └── test
        └── java
            └── org
                └── challenge
                    └── calculator
                        └── services
```
----
**Model**
To support the functionality of the application we use the following entities:
- User. The person using the application, usually executing the services.
- Role. Type of user. It helps us to determine the functionality a user can use.
- User Status - This tell if the user can log in to the application.
- Service. The representation of the math operations
- Service Type. This is a list of service categories. We can have multiple services for the same catetory.
- Service Status. The status let us know if a user can execute it.
- Record. The holder of the execution data, that is our log.
- Record Status. A record can have one of two statuses, Active or Deleted.

There are other more but those are used to interact with the REST api, like DTOs and credentials.

This is class diagram showing the relationships between them:

![Class Model](images/ClassesDiagram.png?raw=true)

----
**Database Model**
The database model is based on the entity model, so basically looks the same but shows
how the properties are mapped to database types.

![Database model](images/DatabaseModel.png?raw=true)

Because a user can have multiple roles, there is an intermediate table between `users` table and `role` table.

The system_configuration table is used to store initialization data, like the default user's balance.
It could be used to store other information like URLs or credentials to connect to third parties.

----
**Migrations**
The migrations mechanism is supported by [FlywayDB](https://flywaydb.org/documentation/v6/).
I choose the version 6 because it's the one supporting PostgreSQL 9.4, the one I'm using.

Flyway runs everytime we start the application checking if there any new migration files present. If the migration scripts execute successfully the application continues the startup, otherwise an exception is thrown and the application halts.

Flyway uses an special table in the database to keep track of the migrations already executed, so it can avoid running the same script more than once.
You can see the table in the database image above, the table name is `flyway_schema_history` and is under the `settings` schema.

New migration files should be placed in the `./src/main/resources/db/migration` directory.

The naming convention can be found [here](https://flywaydb.org/documentation/v6/migrations#sql-based-migrations).

_Note1:_
Flyway assumes that the database already exists.

_Note 2:_
We can run the migrations before starting the application executing this command;
`>mvn flyway:migrate`



-------
**Services**
Initially we have seven services;
1. Addition
2. Subtraction
3. Multiplication
4. Division
5. Square Root
6. Free Form
7. Random String

All of them extend an abstract base class with couple of common methods, and leave
to the subclasses the implementation of the method `execute(...)`

![Service hierarchy](images/CalculatorServicesHierarchy.png?raw=true)

This design lets expose a unified interface for any service, with only one entry point, and in order to know which service to call we use it's UUID.

The execute method receives a `ServiceRequest` object that let us know which service to call and the parameters it is going to need.

The execution result is encapsulated in a `ServiceResponse` object, which holds information regarding to the actual result of the math operation, date, parameters used, and user's balance after the execution.

**REST API**
The api is exposed by five controllers:
- Login
- Users
- Services
- Records
- Calculator

![Controllers ](images/ControllersClassDiagram.png?raw=true)

The Login controller, as you may expect, is in charge of validate a user's credentials(username + password) to let him log into the application.
Also it's used to sign up/register new users.

Users, Services and Records controllers are mainly in charge of the CRUD operations for their
corresponding entity models.

The Calculator controller is the one receiving the service execution requests.
As you can see in the diagram, the original design was to build an endpoint for every type of
service, but after some iterations I decided to use a strategy pattern to determine which service to call using it's type.

I left the original endpoints so we can see how this approach reduced the boilerplate code, and also for testing purposes.

_Security_
To let only registered users use the API the application asks for a token in every request, except for the login and the sign up endpoints.

The token was generated using a JSON Web Token library (`io.jsonwebtoken`).

So the first step before using the API is to call the login like this:
1. GET request to: http://localhost:8081/api/v1/login/authenticate
   1.1 As part of the request we pass the credentilas
```
{
    "username":"admin@test.com",
    "password":"password"
}
```

If the authentication is successful we should get a token back;
```
{
    "value": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsImlhdCI6MTYyODk2NjEwMywiZXhwIjoxNjI4OTY5NzAzfQ.n0OMB9_QN0MIKvCo4PUVmjwc7-94ME_K4NUORcuHUAUoaK3ZU4F4-HMbgaRhkPYIO_p7qxmENtLGAs6l_PPL0w"
}
```

This token should be used with every request using the `Authorization` header with the suffix `Bearer `.
![Example on how to use the token ](images/bearer-token.png?raw=true)


The token validation is done through Spring's Security mechanism. The `JwtTokenFilter` filter is called with every request and checks that token is present and that belongs to a registered user.

All the security related classes are in the package `org.challenge.calculator.security`.


_API Docs_
The api documentation was done using a java library for [Open Api 3](https://swagger.io/specification/).
After the application has started you can visit
this address `http://localhost:8081/swagger-ui/index.html?configUrl=/calculator/api-docs/swagger-config` to give it a try.

_API versioning_
For this purpose I take a simple approach about appending the version in url as prefix, so the endpoints look something like this:
`http://localhost:8081/api/v1/login/authenticate`

So every time we change version we need to update the prefix.

----
**Installation**
For local development we need installed:
- PostgreSQL 9.4
- Java 1.8
- Maven 3

The application expects a database named `code_challenge` already created, with access to the user `postgres` and no password.

We can change that in the `application-local.properties` file, located in `./src/main/java/resources` directory.

To build the application we the following from the root directory:
`>mvn clean install`

And then,
`>java -jar target/calculator-0.0.1-SNAPSHOT.jar`
to startup the application.

Once it started the rest endpoints are ready to be used.

_Important_
As part of the  migrations there is a initialization script that adds a user with
ADMIN privileges, the credentials are:
- user: admin@test.com
- pswd: password


----
**Docker**
If we don't want to install/setup a development environment we can use Docker to let us test the API.

But first you need to install [Docker](https://www.docker.com/products/docker-desktop).

Then, in the directory `./dockerfiles` we have the `docker-compose.yml` file that defines the docker images needed to startup the API, just run the following command:
`>docker compose up`
and after everything has started you can start testing the API as explained in the _API docs_ section.

----
**Future work**
- Make tests for all the services and controllers. (BIG FAIL)
- Include the roles in the security configuration to limit access to the api.
- Find a way to document Page, Pageable and Sort classes.