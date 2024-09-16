# payu-payments

1. Install `gradle` and `kotlin` commands: `brew install kotlin && brew install gradle`.
2. Compile the project: `./gradlew clean build jibDockerBuild`.
3. Build and compose the images using `docker-compose --env-file src/main/resources/.env up --force-recreate --remove-orphans`.
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

5. By accessing to DB, use: `docker-compose exec database psql -U postgres -d mydb`
