[README.md](https://github.com/user-attachments/files/23340844/README.md)
# PDS - Rezervacije

Micro - services (Eureka, API Gateway, Users, Booking)

Koriscen :
Java 17 
Spring Boot 3.5.x 
Spring Cloud 2025.0.0 
Maven

Servisi se pokrecu sledecim redosledom: 
discovery-service -> users-service -> booking-service -> api-gateway

Moguce je pristupiti Eureka dashboard-u na http://localhost:8761, gde mozemo videti sve registrovane korisnike na Eureka server.

Dodatno moguce slanje zahteva preko Swagger-UIa za users-service i booking service.

Pristup centralizovanoj tacki za sve konekcije preko api-gateway-a na http://localhost:8080/api/**

Pracenje CircuitBreaker stanja je moguce preko actuatora na http://localhost:8082/actuator/circuitbreakers

Port-ovi svih pokrenutih servisa:

discovery-service : 8761
api-gateway : 8080
users-service : 8081
booking-service : 8082

