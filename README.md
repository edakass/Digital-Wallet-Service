# Digital-Wallet-Service

## Overview
💰 Digital Wallet API – Spring Boot Project

A secure and role-based backend wallet API built for a digital payment company challenge. The system enables customers and employees to:

- Create and manage wallets

- Deposit or withdraw funds

- Approve or deny transactions

- Track transaction history

JWT-based authentication and Spring Security are used for secure access control.

🔐 [Try out JWT tokens online](https://jwt.tplant.com.au)

## 🚀 Tech Stack
- Java 17

- Spring Boot 3.5.4

- Spring Web

- Spring Data JPA

- Spring Security

- JWT (JJWT)

- MySQL

- Bean Validation (Jakarta)

- Swagger (springdoc-openapi)

- JUnit

- Spring Security Test

- Spring Boot DevTools

- Maven


## 🧾 Features
✅ Wallet creation & listing

✅ Deposit / Withdraw operations

✅ Transaction approval flow

✅ Role-based access (CUSTOMER vs EMPLOYEE)

✅ JWT authentication & authorization

✅ Swagger API documentation


## 🔑 Roles & Permissions

| Operation           | CUSTOMER (self) | EMPLOYEE (all users) |
|---------------------| --------------- | -------------------- |
| Register/Login      | ✅               | ✅                    |
| Create Wallet       | ✅               | ✅                    |
| Deposit             | ✅               | ✅                    |
| Withdraw            | ✅               | ✅                    |
| Approve Transaction | ❌               | ✅                    |


## 📌 Business Rules
- Amount ≤ 1000 → _APPROVED_

- Amount > 1000 → _PENDING_ (requires approval)

- **CUSTOMER** can only act on their own wallets

- **EMPLOYEE** can act on all wallets


## Getting Started
### 1. Clone & Build
```
git clone <repository-url>
cd digital-wallet-api
mvn clean install
```
### 2. Run the App
```
mvn spring-boot:run
```
### 3. Port Configuration
The application runs on the port specified in `application.properties`.  
By default, Spring Boot uses port `8080`, but in this project, we set:

_application.properties_
```
server.port=1007
```

App will start at: http://localhost:1007 



## 🗄️ Database Configuration (MySQL)

Update your application.properties file:
```
# ---------------------------
# Database Configuration (MySQL)
# ---------------------------
spring.datasource.url=jdbc:mysql://localhost:3306/digital_wallet
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

📌 Make sure that the digital_wallet database exists in your MySQL instance.
You can create it manually or use the provided init.sql file.

If you want to use a different database name, feel free to change the digital_wallet part in the spring.datasource.url, and update the corresponding SQL file accordingly.


📄 Swagger UI
Interactive API documentation is available at:

👉 http://localhost:1007/swagger-ui/index.html#/

>  ℹ️ Port 1007 is used by default in this project. If you change the port in application.properties, update the URL accordingly.

## 🔐 JWT Auth
All protected endpoints require a JWT token.

After logging in via /api/auth/login, you will receive a token like this:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
Include this token in the _Authorization_ header of every authenticated request:

```http
Authorization: Bearer <your_token>
```
> ℹ️ In Postman, go to the "Authorization" tab, choose "Bearer Token", and paste the token there.

### 🧷 Example (Postman)
| Key           | Value                 |
| ------------- | --------------------- |
| Authorization | Bearer `<your_token>` |


![Postman JWT Auth Example](assets/screenshots/postman-jwt-auth.png)


### 🔐 How to generate a JWT secret key

See: [🔐 JWT Secret Instructions](https://www.jwt.io/introduction)



## 🧱 Data Model

### Customer

| Field   | Type   |
| ------- | ------ |
| id      | Long   |
| name    | String |
| surname | String |
| tckn    | String |


### Wallet

| Field             | Type        |
| ----------------- | ----------- |
| walletName        | String      |
| currency          | TRY/USD/EUR |
| activeForShopping | Boolean     |
| activeForWithdraw | Boolean     |
| balance           | BigDecimal  |
| usableBalance     | BigDecimal  |


### Transaction

| Field             | Type                        |
| ----------------- | --------------------------- |
| type              | DEPOSIT / WITHDRAW          |
| amount            | BigDecimal                  |
| status            | PENDING / APPROVED / DENIED |
| oppositeParty     | String                      |
| oppositePartyType | IBAN / PAYMENT              |


------

## 🔧 Sample API Usage

### ✅ Register
```http
POST /api/auth/register
```

```json
{
  "name": "Janice",
  "surname": "Albright",
  "tckn": "12345678900",
  "password": "janice123",
  "role": "CUSTOMER"
}
```

### ✅ Login

```http
POST /api/auth/login
```

```json
{
  "tckn": "12345678900",
  "password": "janice123",
  "role": "CUSTOMER"
}
```


### ✅ Create Wallet


```http
POST /api/wallets
```

```json
{
  "walletName": "My Main Wallet",
  "currency": "USD",
  "activeForShopping": true,
  "activeForWithdraw": true
}
```

### ✅ Deposit Money

```http
POST /api/transactions/deposit
```

```json
{
  "walletId": 1,
  "amount": 500,
  "oppositeParty": "TR11110000",
  "oppositePartyType": "IBAN"
}
```


## 🧪 Test Scenarios

### 👩 Janice (CUSTOMER)
| Step                  | Description        | Expected  |
|-----------------------| ------------------ | --------- |
| ✅ Register            | `CUSTOMER`         | JWT token |
| ✅ Login               | With TCKN/password | JWT token |
| ✅ Create Wallet       | `POST /wallets`    | 200 OK    |
| ✅ Deposit ≤1000       | Status `APPROVED`  | ✅         |
| ✅ Deposit >1000       | Status `PENDING`   | ✅         |
| ❌ Approve Transaction | Forbidden          | 403       |


### 👨 Jack (EMPLOYEE)

| Step                             | Description | Expected  |
| -------------------------------- | ----------- | --------- |
| ✅ Register/Login                 | EMPLOYEE    | JWT token |
| ✅ Create Wallet for another      | OK          | ✅         |
| ✅ Approve pending txn            | OK          | ✅         |
| ✅ Withdraw on behalf of customer | OK          | ✅         |



## 🔐 JWT Secret <a name="jwt-secret"></a>
Generate a secure JWT secret:

### Java Code

```java
Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
String base64Key = Encoders.BASE64.encode(key.getEncoded());
```

### Terminal (OpenSSL)
```bash
openssl rand -base64 64
```
Paste the result into:

```properties
jwt.secret=your_generated_secret
```

## 📚 API Endpoint Summary

### 🔐 Authentication
| Method | Endpoint             | Description                              |
| ------ | -------------------- | ---------------------------------------- |
| POST   | `/api/auth/register` | Register new user (customer or employee) |
| POST   | `/api/auth/login`    | Login and receive JWT token              |

### 💼 Wallets
| Method | Endpoint       | Description                                                                                          |
| ------ | -------------- | ---------------------------------------------------------------------------------------------------- |
| POST   | `/api/wallets` | Create wallet                                                                                        |
| GET    | `/api/wallets` | List wallets:<br>• `CUSTOMER`: sees **only their own wallets**<br>• `EMPLOYEE`: sees **all wallets** |


### 💸 Transactions
| Method | Endpoint                       | Description                    |
| ------ | ------------------------------ | ------------------------------ |
| POST   | `/api/transactions/deposit`    | Deposit money to a wallet      |
| POST   | `/api/transactions/withdraw`   | Withdraw money from a wallet   |
| GET    | `/api/transactions/{walletId}` | List transactions for a wallet |
| POST   | `/api/transactions/approve`    | Approve or deny a transaction  |


> ℹ️ You can see detailed examples and sample request bodies in the 🧪 Test Scenarios section above.
---

## 👩‍💻 Developer Info

**👩 Author:** Eda Kaş  
**📧 Contact:** eda.kas60@gmail.com  
**🔗 LinkedIn:** [linkedin.com/in/edakas](https://www.linkedin.com/in/eda-ka%C5%9F-289943180/)  
**💻 GitHub:** [github.com/edakas](https://github.com/edakass)  
**✍️ Medium:** [medium.com/@edakas](https://medium.com/@edakas)