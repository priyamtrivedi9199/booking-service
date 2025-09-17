src/
├── main/
│   ├── java/com/otelier/backend/
│   │   ├── OtelierBackendApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── MongoConfig.java
│   │   │   └── SwaggerConfig.java
│   │   ├── controller/
│   │   │   └── BookingController.java
│   │   ├── model/
│   │   │   ├── Booking.java
│   │   │   └── Hotel.java
│   │   ├── repository/
│   │   │   └── BookingRepository.java
│   │   ├── service/
│   │   │   ├── BookingService.java
│   │   │   └── NotificationService.java
│   │   ├── security/
│   │   │   └── JwtAuthenticationFilter.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       └── BookingConflictException.java
│   └── resources/
│       └── application.yml
└── test/


Setup Instructions
MongoDB Atlas Setup

Create account at MongoDB Atlas
Create new cluster (free tier available)
Create database user with read/write permissions
Whitelist IP addresses (or use 0.0.0.0/0 for development)
Get connection string and update application.yml

Supabase Setup

Create project at Supabase
Go to Authentication → Settings
Copy JWT Secret and Project URL
Create custom roles in your user metadata:
```{
     "role": "staff"
}```
SendGrid Setup (Optional)

Create account at SendGrid
Create API Key with mail send permissions
Set environment variable SENDGRID_API_KEY

# Database
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/otelier

# Supabase JWT
SUPABASE_JWT_SECRET=your-jwt-secret
SUPABASE_URL=https://your-project.supabase.co

# Email
SENDGRID_API_KEY=your-sendgrid-key
SUPPORT_EMAIL=support@yourcompany.com
FROM_EMAIL=noreply@yourcompany.com

# Railway specific
PORT=8080

Deployment Options
Option 1: Railway

Connect your GitHub repository
Set environment variables in Railway dashboard
Deploy automatically on git push

Option 2: Render

Create new Web Service
Connect GitHub repository
Set build command: ./mvnw clean package -DskipTests
Set start command: java -jar target/backend-1.0.0.jar

Option 3: Heroku

Install Heroku CLI
Create app: heroku create your-app-name
Set environment variables: heroku config:set MONGODB_URI=...
Deploy: git push heroku main


||authenticate||
# Get JWT token from Supabase (using their Auth API)
curl -X POST 'https://your-project.supabase.co/auth/v1/token?grant_type=password' \
  -H 'apikey: your-anon-key' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "user@example.com",
    "password": "password"
  }'

||Get Bookings||
curl -X GET 'https://your-app.railway.app/api/hotels/hotel123/bookings' \
  -H 'Authorization: Bearer your-jwt-token'

||Create Booking||
curl -X POST 'https://your-app.railway.app/api/hotels/hotel123/bookings' \
  -H 'Authorization: Bearer your-jwt-token' \
  -H 'Content-Type: application/json' \
  -d '{
    "guestName": "John Doe",
    "guestEmail": "john@example.com",
    "roomNumber": "101",
    "checkInDate": "2025-12-01",
    "checkOutDate": "2025-12-03",
    "totalAmount": 299.99
  }'

17. Key Features Implemented
✅ Core Requirements

Java 17+ with Spring Boot
MongoDB Atlas integration
Supabase JWT authentication
Role-based access control (staff/reception)
GET and POST endpoints with proper validation
Booking conflict detection
Email notifications via SendGrid

✅ Bonus Features

Structured logging with SLF4J
Swagger/OpenAPI documentation
Docker containerization
CI/CD pipeline with GitHub Actions
Comprehensive error handling
Unit tests

# Application Testing
Health Check
bashcurl https://your-app.railway.app/actuator/health
Swagger UI
Visit: https://your-app.railway.app/swagger-ui.html
MongoDB Connection Test
The application will fail to start if MongoDB connection fails, ensuring database connectivity.
# Assumptions Made

User Roles: Users have roles (staff, reception, admin) in their JWT claims
Hotel Data: Hotel information is managed externally; we only store hotelId
Room Management: Room availability is managed through booking conflicts
Notifications: Email notifications are sent for all new bookings
Date Handling: All dates are handled in ISO format (YYYY-MM-DD)
Pricing: Total amount is calculated externally and provided in booking request

# Future Enhancements

Room inventory management
Booking cancellation/modification
Payment processing integration
Advanced search and filtering
Audit logging
Rate limiting
Caching with Redis
WebSocket notifications
Multi-tenant architecture

This implementation provides a production-ready foundation that can be extended based on business requirements.
