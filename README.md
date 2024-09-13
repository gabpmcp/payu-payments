# payu-payments

1. Install `gradle` and `kotlin` commands: `brew install kotlin && brew install gradle`.
2. Compile the project: `gradle build`.
3. Build and compose the images using `docker-compose up --build`.
4. Use the folllowing cURLs to access to services:
5. Run using `gradle run` command, and enjoy it!

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
