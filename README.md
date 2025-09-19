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


# Application Testing
Health Check
bashcurl https://your-app.railway.app/actuator/health
Swagger UI
Visit: https://your-app.railway.app/swagger-ui.html
MongoDB Connection Test
The application will fail to start if MongoDB connection fails, ensuring database connectivity.
# Assumptions Made

