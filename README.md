Markdown
# ☁️ Cloud-Native File Sharing Application

A high-performance, modern file-sharing application built with **Spring Boot 3**, **Neon PostgreSQL**, and **Supabase Cloud Storage**. This system supports fast file uploads (tested up to 400MB+) and serves direct, secure, high-speed public CDN downloads via unique access codes.

---

## 🚀 Key Features
* **Cloud Storage Integration:** Streams physical assets directly to a public Supabase Storage CDN bucket.
* **Relational Metadata Tracker:** Stores upload timestamps, generated UUID file tokens, and access codes inside a managed Neon PostgreSQL database.
* **Secure Architecture:** Built using strict Environment Variable configurations to keep sensitive API keys completely hidden from source control.
* **Containerized Deployment:** Fully Dockerized using multi-stage builds to produce a lightweight runtime image.

---

## 🛠️ Architecture & Data Flow

1. **Upload:** User sends a file -> Spring Boot generates a unique UUID filename -> Saves text metadata to Neon DB -> Streams binary file to Supabase Storage via HTTP Client.
2. **Download:** User requests a file using a unique code -> Backend checks Neon DB -> Redirects browser natively via `302 Found` to the Supabase Public CDN URL.

---

## 📋 Prerequisites
Before running or developing this project, ensure you have the following installed:
* Java 17 or higher
* Maven 3.6+
* Docker

---

## ⚙️ Local Configuration (Security First)

This project strictly adheres to security best practices. The `src/main/resources/application.properties` file is excluded from git tracking via `.gitignore`. 

To set up the project locally:

1. Copy the provided template file:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
Open src/main/resources/application.properties and populate it with your personal database credentials and Supabase tokens:

Properties
# Neon Database Setup
spring.datasource.url=jdbc:postgresql://<your-neon-host>/neondb
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update

# Supabase Storage CDN Configuration
supabase.url=https://<your-project-id>.supabase.co/storage/v1/object/fileshare-uploads/
supabase.key=<your-anon-public-key>
📦 Running the Application
Option 1: Standard Spring Boot Execution
Open the root directory in your terminal and run:

Bash
./mvnw spring-boot:run
Option 2: Docker Container (Multi-Stage Build)
Build the lightweight, secure runtime production image:

Bash
docker build -t fileshare-backend .
Spin up the container while passing your environment properties securely on execution:

Bash
docker run -d -p 8080:8080 --name fileshare-app \
  -e DB_URL="jdbc:postgresql://your-neon-host/neondb" \
  -e DB_USERNAME="your_user" \
  -e DB_PASSWORD="your_password" \
  fileshare-backend
🔒 Supabase Storage Security Policy (RLS)
For download links to function seamlessly via public CDN routing, execute the following script in your Supabase SQL Editor to establish proper Row Level Security (RLS) configurations:

SQL
-- Drop old policy if it exists
DROP POLICY IF EXISTS "Allow public downloads" ON storage.objects;

-- Enforce bucket public accessibility status
UPDATE storage.buckets SET public = true WHERE id = 'fileshare-uploads';

-- Allow any user to fetch files via the CDN pipeline
CREATE POLICY "Allow public downloads" 
ON storage.objects FOR SELECT 
USING (bucket_id = 'fileshare-uploads');

-- Allow the Spring Boot backend to write files inside the bucket
CREATE POLICY "Allow public uploads" 
ON storage.objects FOR INSERT 
WITH CHECK (bucket_id = 'fileshare-uploads');
📄 License
This project is open-source and available under the MIT License.


---

### Why this is a great README:
1. **Professional Layout:** It clearly explains *what* the project is and *how* it handles data under the hood.
2. **Clear Setup Instructions:** Anyone checking out your GitHub will instantly see how to run it without breaking the security rules we just built.
3. **Database & SQL Included:** It keeps a record of the exact Supabase storage policies you need to run, making sure you never lose them if you need to build the project again from scratch!
