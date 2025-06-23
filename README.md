# BookStore

<details>
  <summary><strong>📁 BookStore Directory Structure</strong></summary>
<br>
  BookStore/<br>
  ├── .github/               # GitHub Actions CI/CD workflows<br>
  ├── bookstore/             # Main Spring Boot application<br>
  │   ├── src/<br>
  │   │   ├── main/          # Application source code (Java + resources)<br>
  │   │   └── test/          # Unit tests<br>
  │   └── pom.xml            # Maven build configuration<br>
  ├── docker-compose.yml     # Docker config for DB + Kafka + Hazelcast<br>
  └── README.md              # Project documentation<br>
</details>
<details>
  <summary><strong>📝 Setup Notes</strong></summary>

<br>

1️⃣ **Create the database**  
Manually create a database named `bookstore` in PostgreSQL.  
Then run the SQL schema from `resources/db/migration`.

2️⃣ **Update DB credentials**  
Edit `docker-compose.yml` and `R2dbcConfig.java` to match your desired database name, username, and password.

3️⃣ **Run Docker**  
Start the PostgreSQL container using:  
```bash
docker-compose up -d
```
2️⃣ **Run the Spring Boot application**  
Launch your Spring Boot app from your IDE or terminal.

2️⃣ **Open Swagger UI**  
Access the API documentation at:
http://localhost:8086/webjars/swagger-ui/index.html

</details>

