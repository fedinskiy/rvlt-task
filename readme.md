Simple implementation of money transfer API.

## Build&Run
#### Run for testing purposes
`./gradlew clean run`

#### Build(requires java 1.8+)
`./gradlew clean assemble`

#### Run(requires java 1.8+)
`java -jar build/libs/transfers-*-all.jar`

### Building and running native image
(requires GraalVM with installed Native Image)

##### Use GraalVM
(optional, requires SDKMAN):
`sdk use java 19.2.1-grl`

##### Build:
`./gradlew clean assemble`

##### Generate native image:
`native-image --no-server -O0 -jar build/libs/transfers-*-all.jar`

##### Run:
`./transfers-*-all*`

##API consuming
By default, port 8080 is used. For following instructions host is assumed to be *localhost*.
#### Heartbeat check
`curl -X GET http://localhost:8080/is_alive`

If the application was started successfully then the status code of the response would be 200.

####Account creation
`curl -X POST http://localhost:8080/accounts/${id} -H "Content-Type: text/plain" -d "${amount}"`, where `${id}` and `${amount}` are positive integers.

Creates account with the given id and (optional) amount of money. The response code would be 201 on success or 202 if the account already exists.

#### Getting account info
`curl -X GET http://localhost:8080/accounts/${id}`

Returns code 200 and the current amount of money on the account with the given id as body, if the account exists, or code 404 otherwise.

#### Money transfer
`curl -X POST http://localhost:8080/transfer/${from id}/${to id} -H "Content-Type: text/plain" -d "${amount}"`, where both ids are existing account ids and `${amount}` is positive integer

Transfers the specified amount of money from the first account to the second. Returns code 200 if the transfer was successful and 202 if the transfer failed due to not-user-related issues.

## TODOs:
- Add the history of transactions.
- Use some web router instead of framework.
- Tune up generation of the native image.
