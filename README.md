# spring-cloud-config-java-client

## Usage
See [sample](https://github.com/pivotalservices/spring-cloud-config-java-client/tree/master/sample/src/main/java/io/pivotal/config/client/sample) and [tomcat-launcher](https://github.com/pivotalservices/tomcat-launcher)


#### run clean build

    ./gradlew clean build

#### run unit tests

    ./gradlew clean test

#### run integration tests

    ./gradlew clean integrationTest

#### run clean build and exclude integration tests

    ./gradlew clean build -x integrationTest

#### run clean build and exclude unit tests

    ./gradlew clean build -x test

#### run unit and integration tests

    ./gradlew clean test integrationTest
