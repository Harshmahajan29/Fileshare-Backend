# FileShare — Cloud-Native File Sharing Application

A cloud-native file-sharing application built with **Spring Boot 3**, **PostgreSQL**, and **Supabase Cloud Storage**. FileShare allows users to upload files and share them through unique access codes with cloud-backed storage and persistent metadata.

## Features

* **Cloud File Storage** — Stores uploaded files in Supabase Cloud Storage.
* **Unique File Sharing** — Generates unique access codes for sharing files.
* **Persistent Metadata** — Stores file metadata, UUIDs, timestamps, and access codes in PostgreSQL.
* **Large File Support** — Tested with file uploads of **400MB+**.
* **Secure Configuration** — Database credentials and API keys are managed through environment-specific configuration and excluded from source control.
* **Docker Support** — Containerized application using a multi-stage Docker build.
* **RESTful Backend** — Provides APIs for file upload, retrieval, sharing, and deletion.

## Architecture

```text
                    User
                     |
                     v
              Spring Boot API
                     |
          +----------+----------+
          |                     |
          v                     v
   PostgreSQL Database     Supabase Storage
   (File Metadata)          (File Content)
```

## Data Flow

### Upload

```text
Client
  |
  | Upload File
  v
Spring Boot
  |
  +-- Generate UUID
  |
  +-- Generate Share Code
  |
  +-- Store Metadata ------> PostgreSQL
  |
  +-- Upload File ---------> Supabase Storage
```

### Download

```text
Client
  |
  | Share Code
  v
Spring Boot
  |
  | Lookup Metadata
  v
PostgreSQL
  |
  | File Location
  v
Supabase Storage
  |
  v
File Download
```

## Tech Stack

| Technology       | Purpose                            |
| ---------------- | ---------------------------------- |
| Java 17+         | Backend development                |
| Spring Boot 3    | REST API and application framework |
| PostgreSQL       | File metadata persistence          |
| Supabase Storage | Cloud file storage                 |
| Maven            | Dependency management and build    |
| Docker           | Containerization                   |

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── ...
    └── resources/
        ├── application.properties.example
        └── application.properties
```

The backend follows a layered architecture separating API handling, business logic, storage integration, and database access.

## Prerequisites

Before running the project, install:

* Java 17 or higher
* Maven 3.6+
* PostgreSQL or a PostgreSQL-compatible database
* Supabase project
* Docker (optional)

## Local Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd fileshare
```

### 2. Configure the Application

Copy the example configuration:

```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```

Configure the required database and Supabase credentials:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://<host>/<database>
spring.datasource.username=<username>
spring.datasource.password=<password>

spring.jpa.hibernate.ddl-auto=update

# Supabase Storage
supabase.url=https://<project-id>.supabase.co
supabase.key=<supabase-key>
```

Do not commit `application.properties` or any file containing credentials to the repository.

## Running the Application

### Option 1: Maven

```bash
./mvnw spring-boot:run
```

Or, if Maven is installed globally:

```bash
mvn spring-boot:run
```

The application will start on the configured port.

### Option 2: Docker

Build the image:

```bash
docker build -t fileshare-backend .
```

Run the container:

```bash
docker run -d \
  -p 8080:8080 \
  --name fileshare-app \
  fileshare-backend
```

Environment variables can be supplied at runtime rather than storing credentials inside the image.

## Storage Configuration

FileShare uses a Supabase Storage bucket for storing uploaded files.

The database stores metadata such as:

```text
File ID
Original File Name
Generated UUID
Share Code
Upload Timestamp
File Location
```

The actual binary file is stored separately in cloud storage.

This separation keeps file metadata and file content independent while allowing the backend to manage file-sharing operations.

## Security

The project follows environment-based configuration for sensitive information.

Sensitive values such as:

* Database passwords
* Database connection strings
* Supabase keys
* API credentials

should never be committed to source control.

The repository uses `.gitignore` to prevent local configuration files containing secrets from being tracked.

## Scalability Considerations

The application separates metadata storage from binary file storage, allowing each layer to scale independently.

The architecture can be extended with:

* File expiration
* Authentication and authorization
* Download limits
* File size restrictions
* Access control
* File deletion policies
* Download analytics
* Object storage lifecycle policies

## License

This project is open-source and available under the **MIT License**.

## Author

**Harsh Mahajan**

B.Tech Information Technology
Walchand College of Engineering, Sangli
