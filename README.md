# Problem Handler Webflux Demo

## Getting started

Update following properties with your PostgresDB configurations

```properties
spring.r2dbc.url=${POSTGRES_URL:r2dbc:postgresql://localhost:5432/problem_webflux_db}
spring.r2dbc.username=${POSTGRES_USER:postgres}
spring.r2dbc.password=${POSTGRES_USER:admin}

spring.flyway.url=${POSTGRES_URL:jdbc:postgresql://localhost:5432/problem_webflux_db}
spring.flyway.user=${POSTGRES_USER:postgres}
spring.flyway.password=${POSTGRES_PASSWORD:admin}
```

Update following properties with your MongoDB configurations
```properties
spring.data.mongodb.uri=${MONGODB_URL:mongodb://localhost:27017/problem_web_db}
```

Run the main class [`ProblemWebFluxDemoApplication`](src/main/java/com/ksoot/problem/demo/ProblemWebFluxDemoApplication.java)
and access Swagger [`Swagger`](http://localhost:8080/swagger-ui.html) at http://localhost:8080/swagger-ui.html

Select `Application` from dropdown **Select a definition**

* **State** management APIs are using MongoDB, to test database constraint violations.
* **Employee** management APIs are using PostgresDB, to test database constraint violations.
* **Problem Demo** APIs throws exceptions explicitly.
  Have a look at [`DemoProblemController`](src/main/java/com/ksoot/problem/demo/controller/DemoProblemController.java)
* **State** and **Employee** management APIs are secured, so need to pass a JWT token in `Authorization` header. 
See the lock symbol against the API in Swagger

**Click on Authorize button** to pass the JWT Token. Use following as JWT Token.
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

* Call the APIs providing invalid inputs to make it throw exception and have a look at response.
* Set `problem.debug-enabled=true` in [`application.properties`](src/main/resources/config/application.properties) to get the message resolvers
  and set the messages in [`errors.properties`](src/main/resources/i18n/errors.properties) to customize the error response attributes in response.
* Test with setting `problem.stacktrace-enabled=true` and `problem.cause-chains-enabled=true`
  in [`application.properties`](src/main/resources/config/application.properties) to get Stacktrace and Cause in response.
* Update [`help.html`](src/main/resources/static/problems/help.html) with any custom error description and follow the `type` url 
in error response to see the error description on help page.