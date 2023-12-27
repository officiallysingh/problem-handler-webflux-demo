# Problem Handler Webflux Demo

## Getting started

### Docker compose
If you have docker installed, then simply run the application in `docker` profile by passing `spring.profiles.active=docker` 
as run time argument from your IDE.
Depending on your current working directory in IDE, you may need to change `spring.docker.compose.file=problem-handler-webflux-demo/compose.yml`
to `spring.docker.compose.file=compose.yml` in [**`application-docker.properties`**](src/main/resources/config/application-docker.properties)

Make sure the host ports mapped in [**`Docker compose file`**](compose.yml) are available or change the ports and 
do the respective changes in database configurations [**`application-docker.properties`**](src/main/resources/config/application-docker.properties)

### Explicit Database configs
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
## Run
Run the main class [`ProblemWebFluxDemoApplication`](src/main/java/com/ksoot/problem/demo/ProblemWebFluxDemoApplication.java)
and access Swagger [`Swagger`](http://localhost:8080/swagger-ui.html) at http://localhost:8080/swagger-ui.html

Select `Application` from dropdown **Select a definition**

* **State** management APIs are using MongoDB, to test database constraint violations.
* **Employee** management APIs are using PostgresDB, to test database constraint violations.
* **Problem Demo** APIs throws exceptions explicitly.
  Have a look at [`DemoProblemController`](src/main/java/com/ksoot/problem/demo/controller/DemoProblemController.java)
* **State** and **Employee** management APIs are secured, so need to pass a JWT token in `Authorization` header. 
See the lock symbol against the API in Swagger

**Click on Authorize button** to pass the JWT Token. Use any valid JWT Token.
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
* Follow http://localhost:8080/problems/help.html to see the description of errors.


## Examples
**Following are example error responses in different scenarios.**
The error response attributes `code`, `title` and `detail` can be customized for each error by specifying
the same in `errors.properties` file for different error keys which you can get by setting `problem.debug-enabled=true` in `application.properties` file

### Constraint violations
Most common type of errors an application must handle

#### Jakarta Constraint violations error

Code
```java
@Valid
@Getter
@Setter
public static final class UserRequest {

    @Size(min = 3, max = 10)
    private String name;

    @Size(min = 2, max = 5)
    private String designation;

    @NotNull
    @Valid
    private Address address;
}

@Getter
@Setter
@Valid
public static final class Address {

  @NotEmpty
  private String city;

  @NotEmpty
  private String state;
}
```

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/problems/handler-constraint-violation' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "a",
  "designation": "aaaaaaaaaaaaaaaaa",
  "address": {
    "city": "string"
  }
}'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#constraint-violations",
  "title": "Bad Request",
  "status": 400,
  "detail": "Constraint violations has happened, please correct the request and try again",
  "instance": "/problems/handler-constraint-violation",
  "method": "POST",
  "timestamp": "2023-10-29T16:41:59.876471+05:30",
  "code": "constraint-violations",
  "violations": [
    {
      "code": "400",
      "detail": "User name length should be between 3 and 10",
      "propertyPath": "name"
    },
    {
      "code": "400",
      "detail": "Address state name is required",
      "propertyPath": "address.state"
    },
    {
      "code": "400",
      "detail": "User designation length should be between 2 and 5",
      "propertyPath": "designation"
    }
  ]
}
```

#### PostgresDB Unique constraint violation error

Make following request two time, 2nd time the exception will be thrown.

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/api/employees' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "John Rambo",
  "dob": "1983-06-06"
}'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#500",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "Employee name must be unique, a record with given name already exists",
  "instance": "/api/employees",
  "method": "POST",
  "timestamp": "2023-10-29T16:44:10.917194+05:30",
  "code": "500"
}
```

#### MongoDB Unique constraint violation error

Make following request two time, 2nd time the exception will be thrown.

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/api/states' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "HR",
  "name": "Haryana",
  "gstCode": "6"
}'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#500",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "State name must be unique",
  "instance": "/api/states",
  "method": "POST",
  "timestamp": "2023-10-29T16:44:44.806613+05:30",
  "code": "500"
}
```

#### Invalid Query parameters

Request
```curl
curl -X 'GET' \
  'http://localhost:8080/problems/handler-invalid-query-strings?page=-1&size=1' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#constraint-violations",
  "title": "Bad Request",
  "status": 400,
  "detail": "Constraint violations has happened, please correct the request and try again",
  "instance": "/problems/handler-invalid-query-strings",
  "method": "GET",
  "timestamp": "2023-10-29T14:51:37.889537+05:30",
  "code": "constraint-violations",
  "violations": [
    {
      "code": "400",
      "detail": "must be greater than or equal to 0",
      "propertyPath": "page"
    }
  ]
}
```

#### Invalid format error

Request
```curl
curl -X 'GET' \
  'http://localhost:8080/problems/handler-datetime-conversion?dateTime=2023-13-18T10%3A12%3A12Z' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#400",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid date time value or format. Expected a valid date time in ISO format",
  "instance": "/problems/handler-datetime-conversion",
  "method": "GET",
  "timestamp": "2023-10-29T16:05:09.953099+05:30",
  "code": "400",
  "propertyPath": "dateTime"
}
```

#### File upload max size exceeds error

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/problems/uploadfile' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@Large_File.pdf;type=application/pdf'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#400",
  "title": "Bad Request",
  "status": 400,
  "detail": "Part exceeded the disk usage limit of 1024 bytes",
  "instance": "/problems/uploadfile",
  "method": "POST",
  "timestamp": "2023-10-29T20:50:07.366581+05:30",
  "code": "400"
}
```

### Spring framework thrown exceptions
#### Invalid Media type error

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/problems/handler-json-body' \
  -H 'accept: */*' \
  -H 'Content-Type: application/xml' \
  -d '{
  "test": "string"
}'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#415",
  "title": "Unsupported Media Type",
  "status": 415,
  "detail": "Media Type: application/xml Not Acceptable, Supported Media Types are: application/json",
  "instance": "/problems/handler-json-body",
  "method": "POST",
  "timestamp": "2023-10-29T14:45:47.467268+05:30",
  "code": "415"
}
```

#### Method not allowed error

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/problems/handler-datetime-conversion?dateTime=2023-04-18T10%3A12%3A12Z' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#405",
  "title": "Method Not Allowed",
  "status": 405,
  "detail": "Requested Method: POST not allowed, allowed methods are: GET",
  "instance": "/problems/handler-datetime-conversion",
  "method": "POST",
  "timestamp": "2023-10-29T16:15:08.916369+05:30",
  "code": "405"
}
```

### Programmatically thrown exceptions

#### Any unhandled Throwable

Code
```java
throw new IllegalArgumentException("Expected argument invalid", new IllegalStateException("Dummy cause"));
```

Request
```curl
curl -X 'GET' \
  'http://localhost:8080/problems/handler-throwable' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#500",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "Expected argument invalid",
  "instance": "/problems/handler-throwable",
  "method": "GET",
  "timestamp": "2023-10-29T14:49:40.998497+05:30",
  "code": "500"
}
```

#### Error with dynamic additional attributes

Code
```java
Problem problem = Problems.newInstance("3456", "Bad Request", "Invalid request received, Please retry with correct input")
                .parameter("additional-attribute", "Some additional attribute").build();
throw Problems.throwAble(HttpStatus.BAD_REQUEST, problem);
```

Request
```curl
curl -X 'GET' \
  'http://localhost:8080/problems/throw-problem-with-additional-attribute' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#3456",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request received, Please retry with correct input",
  "instance": "/problems/throw-problem-with-additional-attribute",
  "method": "GET",
  "timestamp": "2023-10-29T16:24:37.976724+05:30",
  "code": "3456",
  "additional-attribute": "Some additional attribute"
}
```

#### Multiple errors

Code
```java
ApplicationException problemOne = Problems.newInstance("sample.problem.one").throwAbleChecked();
ApplicationProblem problemTwo = Problems.newInstance(AppErrors.REMOTE_HOST_NOT_AVAILABLE).detailArgs("http://some.remote.host.com").throwAble();

MultiProblem problems = Problems.ofExceptions(HttpStatus.MULTI_STATUS, problemOne, problemTwo);

Problem problemThree = Problems.newInstance("3456", "Bad Request", "Invalid request received, Please retry with correct input")
    .parameter("additional-attribute", "Some additional attribute").build();
problems.add(problemThree);
Exception exception = new IllegalStateException("Just for testing exception");
problems.add(exception);

Problem problem = Problems.newInstance("111", "Dummy", "Hardcode attributes broblem").build();
problems.add(problem);
throw problems;
```

Request
```curl
curl -X 'GET' \
  'http://localhost:8080/problems/throw-multiple-problems' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#207",
  "title": "Multi-Status",
  "status": 207,
  "detail": "Multi-Status",
  "instance": "/problems/throw-multiple-problems",
  "method": "GET",
  "timestamp": "2023-10-29T16:22:53.363785+05:30",
  "code": "207",
  "errors": [
    {
      "code": "500",
      "title": "Internal Server Error",
      "detail": "Sample error message defined in 'errors.properties'"
    },
    {
      "code": "503",
      "title": "Service Unavailable",
      "detail": "Looks like something wrong with remote host: http://some.remote.host.com"
    },
    {
      "code": "3456",
      "title": "Bad Request",
      "detail": "Invalid request received, Please retry with correct input",
      "additional-attribute": "Some additional attribute"
    },
    {
      "code": "500",
      "title": "Internal Server Error",
      "detail": "Just for testing exception"
    },
    {
      "code": "111",
      "title": "Dummy",
      "detail": "Hardcode attributes broblem"
    }
  ]
}
```

### OpenAPI Specification violation error

Request
```curl
curl -X 'POST' \
  'http://localhost:8080/api/pets' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": 0,
  "name": "string",
  "category": "string",
  "tags": [
    "string"
  ],
  "status": "AVAILABLE"
}'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#constraint-violations",
  "title": "Bad Request",
  "status": 400,
  "detail": "Constraint violations has happened, please correct the request and try again",
  "instance": "/api/pets",
  "method": "POST",
  "timestamp": "2023-10-29T16:06:18.335463+05:30",
  "code": "constraint-violations",
  "violations": [
    {
      "code": "400",
      "detail": "[Path '/id'] Numeric instance is lower than the required minimum (minimum: 1, found: 0)"
    }
  ]
}
```

### Security error

Request
```curl
curl -X 'GET' \
  'http://localhost:8080/api/employees/1' \
  -H 'accept: */*'
```

Response
```json
{
  "type": "http://localhost:8080/problems/help.html#401",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Either Authorization header bearer token is missing or invalid",
  "instance": "/api/employees/1",
  "method": "GET",
  "timestamp": "2023-10-29T16:08:40.466566+05:30",
  "code": "401"
}
```
