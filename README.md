# SnapCivic Backend Application

## Overview

SnapCivic is a digital platform designed to empower neighborhoods by facilitating community-driven issue reporting and resolution. The application enables residents to:

- Report local issues (potholes, waste management, street lighting, etc.)
- Track issue status and updates
- Collaborate through comments and upvotes
- Prioritize community concerns
- Monitor issue resolution progress

## Tech Stack

- **Java 21** - Core programming language
- **Spring Boot 3.3.4** - Application framework
- **MySQL** - Database
- **Spring Security** - Authentication and authorization
- **JWT** - Token-based security
- **OpenAPI/Swagger** - API documentation
- **Gradle** - Build tool
- **Docker** - Containerization

## Key Features

- **User Management**
  - Authentication & Authorization
  - Profile management
  - Password reset functionality
  - Email notifications

- **Issue Management**
  - Create, read, update, delete issues
  - Categorization (Infrastructure, Public Safety, Environment, etc.)
  - Status tracking (Open, In Progress, Closed)
  - Image attachments
  - Location-based issue tracking

- **Community Engagement**
  - Comments
  - Upvoting system
  - Bookmarking functionality
  - Distance-based issue feed

## Getting Started

### Prerequisites
- Java 21
- MySQL
- Gradle
- Docker (optional)

### Environment Variables
Create a `.env` file with the following configurations:

## OpenAPI Documentation

The application includes OpenAPI documentation, which can be accessed at `http://localhost:8080/swagger-ui.html`.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries, please contact M. Ali Farhan at [alifarhan231087@gmail.com](mailto:alifarhan231087@gmail.com).
