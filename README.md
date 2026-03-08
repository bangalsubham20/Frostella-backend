# ⚙️ Frostella - Backend API

This is the core engine of the Frostella boutique bakery platform. It provides a robust, secure REST API built with Java and Spring Boot to handle orders, payments, user accounts, and product management.

## 🚀 Tech Stack
*   **Core**: Java 17, Spring Boot 3.2.3
*   **Security**: Spring Security, JWT (JSON Web Token)
*   **Persistence**: MySQL, Spring Data JPA
*   **Integrations**: 
    *   **Razorpay**: Payment gateway integration
    *   **Cloudinary**: Automated image storage and optimization
    *   **JavaMail**: Automated order confirmations and notifications
*   **Documentation**: Swagger/OpenAPI (Optional)
*   **Deployment**: Docker, Maven

## 📂 Project Structure
*   `src/main/java/.../controller`: REST Endpoints
*   `src/main/java/.../service`: Business logic layer
*   `src/main/java/.../model`: JPA Entities
*   `src/main/java/.../repository`: Data access layer (Spring Data JPA)
*   `src/main/java/.../config`: Security, CORS, and Cloudinary configurations

## 🛠️ Local Development

### Prerequisites
*   JDK 17 or higher
*   Maven 3.x
*   MySQL Server

### Configuration
1. Clone this repository.
2. Update `src/main/resources/application.properties` with your local database credentials.
3. Ensure the database `frostella_db` exists or let JPA create it automatically.

### Running
```bash
mvn spring-boot:run
```

## 🐳 Docker Support
Build the container:
```bash
docker build -t frostella-backend .
```

Run the container:
```bash
docker run -p 8080:8080 frostella-backend
```

## 🔐 API Reference
| Endpoint | Method | Description | Role |
| :--- | :---: | :--- | :--- |
| `/api/auth/register` | `POST` | Create new account | Public |
| `/api/auth/login` | `POST` | Authenticate & get JWT | Public |
| `/api/products` | `GET` | Fetch all products | Public |
| `/api/admin/products` | `POST` | Add new product | ADMIN |
| `/api/orders` | `POST` | Place an order | USER |
| `/api/users/profile` | `GET` | View current user profile | USER |

---
*Created with ❤️ for Frostella Bakery.*
