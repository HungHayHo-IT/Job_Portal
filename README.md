# Job Portal

A full-stack job portal application that connects employers and job seekers.

## Features

### Job seekers

- Register and log in
- Manage personal profile
- Search and view job listings
- Save jobs
- Apply for jobs
- Upload profile information and resume
- View submitted applications

### Employers

- Manage company information
- Create and manage job postings
- Change job status
- Review job applications

### Administrators

- Manage companies and employers
- Manage contact messages
- Assign employer roles and companies

## Technology Stack

### Backend

- Java 17
- Spring Boot 3.2
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT authentication
- MySQL
- Bean Validation
- Spring Boot Actuator
- Springdoc OpenAPI
- Maven

### Frontend

- React 19
- Vite
- React Router
- Axios
- Tailwind CSS

### DevOps

- Docker
- Docker Compose
- GitHub Actions
- Kubernetes — planned

## Project Structure

```text
.
├── JobPortal
│   ├── BE                  # Spring Boot backend
│   ├── FE                  # React frontend
│   └── docker-compose.yml  # Local supporting services
├── .github
│   └── workflows
├── .env.example
├── .gitignore
└── README.md
```

## Prerequisites

- Java 17
- Node.js 20+
- MySQL 8
- Docker Desktop — optional
- Git

## Environment Variables

The backend requires the following environment variables:

| Variable | Description |
|---|---|
| `DB_URL` | MySQL JDBC connection URL |
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Random JWT signing secret of at least 32 bytes |

Never commit real credentials or `.env` files.

## Running the Backend

```powershell
cd JobPortal\BE
.\mvnw.cmd spring-boot:run
```

Backend URL:

```text
http://localhost:8082
```

Swagger UI:

```text
http://localhost:8082/swagger-ui.html
```

Health endpoint:

```text
http://localhost:8082/actuator/health
```

## Running the Frontend

```powershell
cd JobPortal\FE
npm ci
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## Build and Test

Backend:

```powershell
cd JobPortal\BE
.\mvnw.cmd clean verify
```

Frontend:

```powershell
cd JobPortal\FE
npm ci
npm run lint
npm run build
```

## Security

- Credentials are supplied through environment variables.
- Passwords are hashed using BCrypt.
- JWT signing secrets are not stored in source code.
- Production Actuator exposure is limited to health and info.
- Generated logs, local runtime data and Kubernetes secrets must not be committed.

## CI/CD Roadmap

- Repository hygiene and branch protection
- Backend and frontend CI
- Docker image build and security scan
- Container registry publishing
- Kubernetes manifests
- Automated deployment
