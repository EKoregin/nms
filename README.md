# Network Management System

Приложение для автоматизации работы интернет провайдера

## Setup
1. Clone the repository: `git clone https://github.com/EKoregin/nms.git`
2. Create a `.env` file with the following variables:
```
DB_URL=jdbc:postgresql://nms_db:5432/nms
DB_USERNAME=postgres
DB_PASSWORD=password

DB_MIKROBILL_URL=jdbc:mysql://mikrobill_db:3306/mikrobill?useSSL=false&allowPublicKeyRetrieval=true
DB_MIKROBILL_USERNAME=remote_nms
DB_MIKROBILL_PASSWORD=password

MYSQL_ROOT_PASSWORD=password
MYSQL_DATABASE=mikrobill
MYSQL_USER=remote_nms
MYSQL_PASSWORD=password
```
3. Create package: `mvn clean package -DskipTests`
4. Run with Docker Compose: `docker-compose up`
5. The application will be available at: http://localhost:8999