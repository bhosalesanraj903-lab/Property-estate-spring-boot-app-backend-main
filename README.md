# Property estate Spring Boot backend

REST API for a property listing site. It handles user sign-in by email OTP, admin login, property listings, viewing bookings, and contact messages. The React frontend that uses it is in [Property-react-app-frontend-main](https://github.com/bhosalesanraj903-lab/Property-react-app-frontend-main).

## Tech stack

- Java 17, Spring Boot 3.3
- Spring Web, Spring Data JPA, Spring Security
- MySQL 8
- JWT auth with jjwt 0.11.5
- Spring Mail for OTP and booking emails (any SMTP provider; tested setup uses Brevo)
- Cloudinary for listing image uploads
- springdoc-openapi for Swagger UI

## Running locally

You need JDK 17 and a MySQL server. Maven comes with the project through `./mvnw`.

1. Start MySQL. The quickest way is Docker:

   ```bash
   docker run -d --name realestate-mysql -p 3306:3306 \
     -e MYSQL_ROOT_PASSWORD=change-me -e MYSQL_DATABASE=propertydealer mysql:8.4
   ```

   The app connects to `jdbc:mysql://localhost:3306/propertydealer` as `root`. It creates the database if it is missing, and Hibernate creates the tables on first start (`ddl-auto=update`).

2. Create `local.properties` next to `pom.xml` with your own values (see the next section). Git ignores this file.

3. Start the API:

   ```bash
   ./mvnw spring-boot:run
   ```

   It listens on `http://localhost:3060`. Swagger UI is at `http://localhost:3060/swagger-ui/index.html`.

## Configuration

`src/main/resources/application.properties` holds no secrets. Each secret is read from an environment variable, or from `local.properties` when that file exists (its values win).

| Property | Environment variable | What it is |
|---|---|---|
| `spring.datasource.password` | `DB_PASSWORD` | MySQL root password |
| `jwt.secret` | `JWT_SECRET` | Signing key for access tokens, at least 32 characters |
| `spring.mail.username` | `MAIL_USERNAME` | SMTP login |
| `spring.mail.password` | `MAIL_PASSWORD` | SMTP password or key |
| `app.mail.from` | `MAIL_FROM` | Sender address, verified with your SMTP provider |
| `cloudinary.cloud-name` | `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `cloudinary.api-key` | `CLOUDINARY_API_KEY` | Cloudinary API key |
| `cloudinary.api-secret` | `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `sms.api.key` | `SMS_API_KEY` | MSG91 key, used by `SmsService` |

Example `local.properties`:

```properties
spring.datasource.password=change-me
jwt.secret=replace-with-a-random-string-of-at-least-32-characters
spring.mail.username=your-smtp-login
spring.mail.password=your-smtp-key
app.mail.from=you@example.com
cloudinary.cloud-name=
cloudinary.api-key=
cloudinary.api-secret=
sms.api.key=
```

The SMTP host is set to Brevo (`smtp-relay.brevo.com`, port 587). To use another provider, change `spring.mail.host` and `spring.mail.port`.

CORS allows `http://localhost:5173`, the Vite dev server. Add your real frontend address in `config/SecurityConfig.java` before deploying.

## Authentication

Users sign in with an email OTP. `POST /api/auth/send-otp` creates the user on first use and emails a 6-digit code that is valid for 5 minutes. `POST /api/auth/verify-otp` checks the code and returns an access token, a refresh token, and the role.

Admins sign in with email and password at `POST /admin/login`.

Access tokens last 15 minutes. Send them as `Authorization: Bearer <token>`. `POST /api/auth/refresh` returns a new access token.

## Endpoints

Public, no token needed:

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/auth/send-otp` | Email a sign-in code |
| POST | `/api/auth/verify-otp` | Check the code and get tokens |
| POST | `/api/auth/refresh` | New access token |
| POST | `/admin/login` | Admin sign-in |
| POST | `/admin/create` | Create an admin account |
| GET | `/properties` | All listings, paged (`page`, `size`, default size 6) |
| GET | `/properties/{id}` | One listing |
| GET | `/properties/type/{type}` | Listings by type, `BUY` or `RENT` |
| GET | `/properties/featured` | Featured listings |
| GET | `/properties/search` | Search listings |
| POST | `/contacts` | Send a contact message |
| GET, POST | `/bookings` | List or create viewing bookings |
| GET | `/bookings/user/{email}` | Bookings for one user |
| DELETE | `/bookings/{id}`, `/bookings/delete/{id}` | Cancel a booking |

Admin only (token with role `ADMIN`):

| Method | Path | Purpose |
|---|---|---|
| POST | `/properties/admin` | Create a listing |
| PUT | `/properties/admin/{id}` | Update a listing |
| DELETE | `/properties/admin/{id}` | Delete a listing |
| PATCH | `/properties/admin/{id}/featured` | Set featured on or off, body `{"featured": true}` |
| POST | `/admin/upload` | Upload listing images to Cloudinary |
| GET | `/admin/users` | List users |
| DELETE | `/admin/users/{id}` | Delete a user |
| GET | `/admin/bookings` | List bookings |
| PUT | `/admin/bookings/{id}/status` | Change a booking's status |
| PUT | `/admin/approve/{id}`, `/admin/reject/{id}` | Approve or reject a booking |
| GET | `/admin/contacts` | List contact messages |
| DELETE | `/admin/contacts/{id}` | Delete a contact message |
| PUT | `/admin/{id}` | Update an admin account |

## Known gaps

- `/bookings/**` and `/admin/create` are open without a token in `SecurityConfig.java`. Anyone who knows a user's email can read their bookings. Lock these down before a public deployment.
- `spring.jpa.show-sql` and `spring.mail.properties.mail.debug` are on, which suits development but not production.

## Project layout

```
src/main/java/com/properties/real_estate_spring_boot_app/
  config/       security rules, CORS, Cloudinary, Swagger
  controller/   REST endpoints
  dto/          request and response objects
  model/        JPA entities: Property, User, Admin, Booking, Contact, OTP
  repository/   Spring Data repositories
  security/     JWT creation and request filter
  service/      business logic, email, OTP, SMS
src/main/resources/application.properties
```
