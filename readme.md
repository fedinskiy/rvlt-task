Simple implementation of money transfer API.

By default, port 8080 is used.

## Build&Run
### Run for testing purposes
`gradlew clean run`

### Build(requires java 1.8+)
`./gradlew clean assemble`

### Run(requires java 1.8+)
`java -jar build/libs/transfers-*-all.jar`

### Buildig and running native image
(requires GraalVM with installed Native Image)

#### Use GraalVM
(optional, requires SDKMAN):
			`sdk use java 19.2.1-grl`

#### Build:
		`./gradlew clean assemble`

#### Generate native image:
		`native-image --no-server -O0 -jar build/libs/transfers-*-all.jar`

#### Run:
		`./transfers-*-all*`

## TODOs:
- Add account creation API and remove database 'loading'
- Add thread-safety
- Update account in database woth ACID transactions
- Tune up native image generation
- Use some web router instead of framework
- Add error handling
- Add history of transactions
- Add validation of input parameters
