# Employee Management Service

A Spring MVC (Jakarta) application for managing employees and departments, with pagination, basic CRUD operations, and AWS S3 integration for serving media via pre‑signed URLs. Built with Java 21 and Lombok.

## Key Features
- Employee listing with server-side pagination.
- Department entity support.
- Employee job experiences with batch insert.
- AWS S3 pre‑signed GET URLs for time‑limited access to employee images.
- Transactional write operations for consistency.
- Mapper-based persistence with paging utilities.

## Tech Stack
- Java 21
- Spring (MVC, Transaction)
- Lombok
- MyBatis/PageHelper (for pagination)
- AWS SDK v2 (S3 Presigner)
- Jakarta annotations (via Spring Jakarta artifacts)
- Maven

## Project Structure
```plain text
.
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/
│  │  │     └─ chrisyo/
│  │  │        ├─ controller/           # REST controllers (if present)
│  │  │        ├─ service/
│  │  │        │  ├─ EmpService.java
│  │  │        │  └─ impl/
│  │  │        │     └─ EmpServiceImpl.java  # Pagination and S3 URL enrichment
│  │  │        ├─ mapper/
│  │  │        │  ├─ EmpMapper.java          # Employee queries
│  │  │        │  └─ EmpExprMapper.java      # Employee experience batch ops
│  │  │        ├─ entity/
│  │  │        │  ├─ Employee.java
│  │  │        │  ├─ EmpExpr.java
│  │  │        │  ├─ EmpQueryParam.java      # page, pageSize, filters
│  │  │        │  ├─ PageBean.java           # paged response wrapper
│  │  │        │  └─ Dept.java               # Department entity
│  │  │        └─ utils/
│  │  │           └─ AwsS3Utils.java         # Pre‑signed GET URL helper
│  │  └─ resources/
│  │     ├─ application.yml                  # App configuration (profiles)
│  │     └─ mapper/                          # MyBatis XML mappers (if used)
├─ pom.xml
└─ .gitignore
```


## How It Works
- Pagination:
    - The service layer starts a paged query using a paging helper, fetches employees via the mapper, and wraps results in a PageBean containing total count and items.
- S3 Pre‑signed URLs:
    - For each employee record containing an S3 object key (e.g., image), the service generates a short-lived pre‑signed GET URL and replaces the key with the URL before returning to the client.
- Create Flow:
    - The service creates an employee record, retrieves its ID, assigns it to each provided experience entry, and performs a batch insert—within a single transaction.

## Configuration
Provide configuration via environment variables or application.yml. Use placeholders for sensitive values.

Example application.yml:
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://<DB_HOST>:<DB_PORT>/<DB_NAME>
    username: <DB_USERNAME>
    password: <DB_PASSWORD>
    driver-class-name: org.postgresql.Driver
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

mybatis:
  mapper-locations: classpath*:mapper/*.xml

app:
  aws:
    s3:
      bucket-name: <AWS_S3_BUCKET_NAME>
aws:
  region: <AWS_REGION>
```


AWS credentials are resolved by the default provider chain. For local development you can set:
```shell script
export AWS_REGION=<AWS_REGION>
export AWS_ACCESS_KEY_ID=<AWS_ACCESS_KEY_ID>
export AWS_SECRET_ACCESS_KEY=<AWS_SECRET_ACCESS_KEY>
```


## Build and Run

Using Maven:
```shell script
mvn clean install
```


Run (Spring Boot style):
```shell script
mvn spring-boot:run
```


Or package and run:
```shell script
java -jar target/<your-artifact-name>.jar
```


If the project targets a traditional servlet container, build the WAR and deploy to your application server (e.g., Tomcat, Jetty).

## Typical Endpoints
- GET /employees?page=<n>&pageSize=<m> — Returns a PageBean with employee data; image fields contain pre‑signed URLs.
- POST /employees — Creates an employee and associated experience entries in a single transaction.

Note: Adjust actual paths to match your controller mappings.

## Development Tips
- Lombok: Ensure your IDE has the Lombok plugin enabled and annotation processing turned on.
- S3 object keys: Store only keys in the database; the service will convert them to pre‑signed URLs on read.
- Response headers: If you need specific download behavior (inline/attachment), configure response overrides when generating the pre‑signed URL in the S3 utility.
- Database migrations: If you use migrations, keep them in src/main/resources/db and run them on startup (tooling optional).

## License
Add your chosen license here.