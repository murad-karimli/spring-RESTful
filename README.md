# User Spring MongoDB Application

This is a Spring Boot application that provides a RESTful API for managing users. It utilizes MongoDB as the database for data persistence.

## Requirements

- Docker
- Docker Compose

## Getting Started

1. Clone this repository to your local machine.
2. Ensure you have Docker and Docker Compose installed.
3. Configure the MongoDB connection details in the `application.properties` file.
4. Build the Docker image and run the containers:

```bash
docker-compose up --build
```

5. The application will be accessible at `http://localhost:8080/api/users`

## Endpoints

### Get All Users

```
GET /api/users
```

### Create User

```
POST /api/users
```

Example Request Body:

```json
{
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1990-01-01",
    "address": "123 Main St",
    "phoneNumber": "123-456-7890"
}
```

### Update User

#### Update Specific Fields

```
PATCH /api/users/{id}
```

Example Request Body:

```json
{
    "email": "newemail@example.com",
    "firstName": "New First Name"
}
```

#### Update All Fields

```
PUT /api/users/{id}
```

Example Request Body:

```json
{
    "email": "newemail@example.com",
    "firstName": "New First Name",
    "lastName": "New Last Name",
    "birthDate": "1990-01-01",
    "address": "New Address",
    "phoneNumber": "987-654-3210"
}
```

### Delete User

```
DELETE /api/users/{id}
```

### Search Users by Birth Date Range

```
GET /api/users/search?from={fromDate}&to={toDate}
```

Replace `{fromDate}` and `{toDate}` with the desired date range in the format `yyyy-MM-dd`.

## Technologies Used

- Spring Boot
- MongoDB
- Docker
- Docker Compose

## Author

Murad Karimli
