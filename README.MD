# 🏪 Franchises API (Reactive)

A **reactive REST API** built with **Spring Boot WebFlux**, following **Clean Architecture (Bancolombia Scaffold)** principles. The API allows managing **franchises, branches, and products**, including advanced queries such as retrieving the product with the highest stock per branch.

This project was designed as a **technical assessment**, focusing on:

* Reactive programming
* Clean Architecture
* Functional endpoints (RouterFunctions & Handlers)
* Proper error handling
* OpenAPI / Swagger documentation
* Docker packaging

---

## 🚀 Tech Stack

* **Java 21**
* **Spring Boot 3**
* **Spring WebFlux** (Reactive)
* **Project Reactor (Mono / Flux)**
* **Gradle**
* **Springdoc OpenAPI (Swagger)**
* **Docker (Multi-stage build)**

---

## 🧱 Architecture

The project follows **Clean Architecture** as defined by Bancolombia:

```
├── domain
│   ├── model
│   ├── usecase
│   └── exception
│
├── application
│   └── config
│
├── infrastructure
│   ├── adapter
│   ├── entrypoints
│   │   ├── reactive
│   │   │   ├── handler
│   │   │   ├── router
│   │   │   └── openapi
│   │   └── dto
│   └── config
```

### 🔑 Key Principles

* **Domain is framework-agnostic**
* **Use cases contain business rules**
* **Handlers adapt HTTP requests to domain use cases**
* **Routers define endpoints (no @RestController)**
* **Infrastructure handles external concerns**

---

## 📌 API Endpoints

### 1️⃣ Create a Franchise

```
POST /franchises
```

Creates a new franchise.

---

### 2️⃣ Add Product to a Branch

```
POST /branches/{branchId}/products
```

Adds a product to a specific branch.

---

### 3️⃣ Delete Product from a Branch

```
DELETE /branches/{branchId}/products/{productId}
```

Removes a product from a branch.

---

### 4️⃣ Update Product Stock

```
PATCH /products/{productId}/stock
```

Updates the stock of a product.

---

### 5️⃣ Get Product with Highest Stock per Branch

```
GET /franchises/{franchiseId}/branches/products/max-stock
```

Returns, for a given franchise, the product with the highest stock for each branch, including branch information.

---

## 📖 API Documentation (Swagger)

The API is fully documented using **OpenAPI / Swagger**, including functional endpoints.

Once the application is running, access:

```
http://localhost:8080/swagger-ui.html
```

> ⚠️ Since the project uses **RouterFunctions**, endpoints are documented using `@RouterOperation` instead of annotations like `@RestController`.

---

## ❗ Error Handling

* Errors are handled **at the handler and domain level**
* Technical details are **never exposed to the client**
* Only **business-friendly messages** are returned

Example error response:

```json
{
  "message": "Franchise not found"
}
```

---

## 🐳 Docker Support

The application can be packaged and run using Docker.

### Build the image

```bash
docker build -t franchises-api .
```

### Run the container

```bash
docker run -p 8080:8080 franchises-api
```

The API and Swagger UI will be available at:

```
http://localhost:8080
```

---

## ▶️ Run Locally (Without Docker)

### Prerequisites

* Java 17
* Gradle

### Build & Run

```bash
gradle clean build
gradle bootRun
```

---

## 🧪 Testing

Tests can be executed with:

```bash
gradle test
```

> ⚠️ Some builds may skip tests when running inside Docker for faster packaging.

---

## 🧠 Design Decisions

* **WebFlux + Reactive Streams** to handle asynchronous flows efficiently
* **Functional endpoints** to avoid MVC coupling
* **Clean Architecture** for maintainability and scalability
* **Explicit OpenAPI documentation** for functional routes
* **Multi-stage Docker build** for lightweight images

---

## 👤 Author

**Andrés Felipe Puello Osorio**
Backend Developer – Java & Reactive Systems

---

## ✅ Status

✔️ Functional endpoints implemented
✔️ Reactive use cases
✔️ OpenAPI documented
✔️ Dockerized
✔️ Ready for technical review 🚀
