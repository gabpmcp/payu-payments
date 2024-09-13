# Getting Started

## Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.3/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.3/gradle-plugin/packaging-oci-image.html)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/6.1.12/spring-framework-reference/languages.html#coroutines)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.3.3/reference/htmlsingle/index.html#web.reactive)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/3.3.3/reference/htmlsingle/index.html#web.spring-hateoas)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## Execution Steps

1. Install `gradle` and `kotlin` commands: `brew install kotlin && brew install gradle`.
2. Compile the project: `gradle build`.
3. Run using `gradle run` command, and enjoy it!
4. Use the folllowing cURLs to access to services:

```cURL
curl -X POST http://localhost:8080/payments/purchase \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "creditCard": "4111111111111111",
    "expirationDate": "12/25",
    "cvv": "123",
    "amount": 4000.0
}'
```

```cURL
curl -X POST http://localhost:8080/payments/refund/{id} \
  -H "Content-Type: application/json"
```
