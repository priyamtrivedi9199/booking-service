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
