# Spring Boot Customer API

A modern, modular Spring Boot application for managing customer and order data via RESTful APIs. Includes an optional JavaFX client and Dockerized deployment support.

---

## ✨ Features

- CRUD operations for customer records (Create, Read, Update, Delete)
- REST API with input validation and error handling
- JavaFX client interface for managing customers
- Dockerfile for easy containerization
- Docker Compose support for running with PostgreSQL

---

## 🧰 Technologies

- Java 17  
- Spring Boot  
- JavaFX (for desktop client)  
- Docker  
- PostgreSQL (for persistence layer)

---

## 🚀 Getting Started

### 1. Build the Project

```bash
./gradlew clean build
````

### 2. Run the Application (Locally)

```bash
java -jar build/libs/your-app-name.jar
```

### 3. Build Docker Image

```bash
docker build -t customer-api .
```

### 4. Start via Docker Compose

```bash
docker-compose up
```

> 📝 **Note:** Replace `your-app-name.jar` with the actual JAR file name from `build/libs/`.

---

## 📦 Project Structure

```
/model       -> Domain models (Customer, Order, etc.)
/service     -> Business logic services
/rest        -> REST controllers (CustomerResource, OrderResource)
/client      -> JavaFX interface logic (optional)
Dockerfile   -> Container definition
start.sh     -> Container entrypoint script
docker-compose.yml -> Multi-container orchestration
```

---

## 📚 Future Improvements

* Add authentication/authorization (Spring Security)
* Include Swagger/OpenAPI documentation
* Add persistent storage integration for the JavaFX client
* Modularize into microservices

---

## 📄 License

MIT – free to use for personal or commercial purposes.
