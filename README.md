# Drivers Files Web Application

A web application for managing truck driver records — CDL information, employment history,
residency history, accidents, traffic convictions, emergency contacts and documents — with
role-based access for **Admins**, **Companies** and **Drivers**.

Originally built in 2011 on Java 7 / Spring 3.1 / Hibernate 3, the application has been fully
modernized to **Spring Boot 3.4.5 on Java 21** with Swagger/OpenAPI documentation and a
JUnit 5 test suite.

## Technology Stack

| Component | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.4.5 (Web, Data JPA, Security, Validation, Mail, Quartz) |
| ORM | Hibernate 6.x via JPA + HikariCP connection pool |
| Database | PostgreSQL |
| Security | Spring Security 6 — form login, role-based access, admin user-switching |
| Views | JSP + JSTL with a lightweight custom layout resolver (Apache Tiles replacement) |
| API Docs | springdoc-openapi 2.8.5 → Swagger UI at `/swagger-ui.html` |
| Scheduling | Quartz 2.3.2 (CSV import job, access-code cleanup job) |
| Email | Spring Mail + FreeMarker templates |
| Testing | JUnit 5 (`spring-boot-starter-test`) |
| Packaging | Executable WAR (`ROOT.war`) |

## Project Structure

```
src/main/java/com/driversfiles/www/
├── Application.java        Spring Boot entry point (@SpringBootApplication)
├── config/                 Configuration & infrastructure
│   ├── SecurityConfig      Security filter chain, providers, switch-user
│   ├── QuartzConfig        Job details + cron triggers
│   ├── WebConfig           MVC config + LayoutViewResolver (Tiles replacement)
│   ├── TilesDefinitions    Parses WEB-INF/tiles.xml, flattens definition inheritance
│   ├── OpenApiConfig       Swagger metadata
│   └── DataConfig          App beans (batch size, file store path, mail addresses)
├── admin|company|driver/   Role portal controllers
├── common/                 Shared controllers (trucks, JSON APIs)
├── auth/                   Authentication service, user details
├── captcha/                Captcha service (custom engine, jcaptcha compatible)
├── core/
│   ├── data/               JPA entities (Person, Driver, Company, License, ...)
│   ├── dao/                DAO interfaces + implementations
│   │   └── criteria/       Lightweight criteria DSL translated to JPA Criteria
│   ├── job/                Quartz jobs
│   └── ...
├── email/, fs/, util/, hibernate/, quartz/, spring/, template/, jsp/
src/main/resources/
├── application.yml         All runtime configuration
├── messages.properties     UI message bundle
└── logback-spring.xml      Logging
src/main/webapp/WEB-INF/    JSP views, tiles.xml layout definitions, email templates
src/test/java/...           JUnit 5 unit tests
src/main/sql/               DDL, seed data, maintenance scripts
```

## Prerequisites

- **JDK 21**
- **Maven 3.9+**
- **PostgreSQL 12+**

## Database Setup

1. Create user and database (defaults match `application.yml`):

```sql
CREATE USER driversfiles WITH ENCRYPTED PASSWORD 'driversfiles';
CREATE DATABASE driversfiles ENCODING 'utf8';
GRANT ALL PRIVILEGES ON DATABASE driversfiles TO driversfiles;
```

2. Load schema and seed data **while connected to the `driversfiles` database**:

```bash
psql -U driversfiles -d driversfiles -f src/main/sql/ddl.sql
psql -U driversfiles -d driversfiles -f src/main/sql/populate.sql
```

> If you loaded the schema as the `postgres` superuser instead, grant the app user rights:
>
> ```sql
> GRANT ALL ON ALL TABLES IN SCHEMA public TO driversfiles;
> GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO driversfiles;
> ```

Useful scripts in `src/main/sql/`:

| Script | Purpose |
|---|---|
| `ddl.sql` | Creates all tables and sequences |
| `populate.sql` | Reference/seed data |
| `restore.sh` | Drop/recreate DB or restore a `.dmp` backup (Linux/macOS) |
| `admin-password.sh` | Resets a user's password to `password1` |

## Configuration

Everything lives in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/driversfiles
    username: driversfiles
    password: driversfiles
  mail:
    host: localhost
    port: 25

app:
  db:
    batch-size: 1000
  external-file-store: /opt/driversfiles/   # document storage directory
```

> **Windows:** set `app.external-file-store` to e.g. `C:/driversfiles/`.
> Other useful keys: `server.servlet.session.timeout`, `spring.servlet.multipart.*`,
> `logging.level.*`, `spring.quartz.*`.

## How to Run

```bash
# Development (exploded, hot classpath)
mvn spring-boot:run

# Production-style executable WAR
mvn clean package -DskipTests
java -jar target/ROOT.war

# External server
# Deploy target/ROOT.war to Apache Tomcat 10.1+ (Jakarta EE)
```

Once running (default port **8080**):

| URL | Description |
|---|---|
| `http://localhost:8080/` | Public home page |
| `http://localhost:8080/login` | Login |
| `http://localhost:8080/swagger-ui.html` | Swagger UI (interactive API docs) |
| `http://localhost:8080/v3/api-docs` | OpenAPI 3 JSON spec |

## Testing

```bash
mvn test
```

The JUnit 5 suite covers:

- `CustomPasswordEncoderTest` — hash format + **legacy credential compatibility** regression test
- `HashedFieldTest` — salted SHA-256 hashing contract (format, randomness, determinism)
- `AuthDetailsTest` — authority mapping and folded password format
- `IOHelperTest`, `ThrowableHelperTest` — utility behavior
- `PersonTest` — entity equals/hashCode contract
- `CriteriaShimTest` — criteria DSL used by all DAOs
- `TilesDefinitionsTest` — tiles.xml parsing + inheritance flattening
- `JCaptchaServiceImplTest` — captcha image generation end-to-end

Database-dependent layers (DAOs against live PostgreSQL, controllers) are exercised through
the running application; integration tests with Testcontainers are a planned enhancement.

## Roles & Access

| Role | Access |
|---|---|
| `ADMIN` | `/secure/admin/**` — users, imports, CMS content, switch-user |
| `COMPANY` | `/secure/company/**` — profile, drivers, trucks |
| `DRIVER` | `/secure/driver/**` — own records, documents, access codes |

Passwords are stored salted + SHA-256 hashed (base64), preserving compatibility with
credentials created by earlier versions of the app.

## Background Jobs (Quartz)

| Job | Schedule | Purpose |
|---|---|---|
| `dataImportJob` | every 15 min | Processes queued CSV data imports |
| `accessCodeCleanupJob` | every 5 min | Expires stale driver access codes |

Jobs start ~10 s after boot (`spring.quartz.startup-delay`).

## Troubleshooting

| Symptom | Fix |
|---|---|
| `permission denied for table ...` | Run the GRANT statements from *Database Setup* as `postgres`, connected to the `driversfiles` DB |
| Port 8080 busy | Add `server.port: 8081` to `application.yml` |
| Blank/broken pages after upgrade | Hard-refresh browser; JSPs recompile on first request |
| Login fails for seeded user | Run `admin-password.sh` SQL (password becomes `password1`) |

## Migration Notes

This codebase was migrated from Java 7 / Spring 3.1 / Hibernate 3 / Tiles 2 / javax to
Java 21 / Spring Boot 3.4 / Hibernate 6 / custom layout resolver / jakarta. Key decisions:

- Legacy Hibernate `DetachedCriteria` calls preserved via an internal criteria shim
  (`core/dao/criteria/`) translated to JPA Criteria at execution time.
- Password hashing formula kept byte-for-byte identical so existing accounts still log in.
- Apache Tiles replaced by `LayoutViewResolver` + `TilesDefinitions`, which reuse the
  original `tiles.xml` definitions unchanged.
