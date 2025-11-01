# Master Chef Celebrity – API de Recetas (Java + Spring Boot)
Nombre: Raquel Selma
API REST para gestionar recetas del programa **Master Chef Celebrity** en **Java 17** con **Spring Boot 3**, **MongoDB**, **Validación** y **OpenAPI/Swagger**.
## Requisitos
- Java 17+, Maven 3.9+, MongoDB (local o Atlas)
## Ejecutar
```bash
mvn spring-boot:run
```
- API: `http://localhost:8080/api/v1/recipes`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/api-docs`
## Endpoints
- POST `/api/v1/recipes` (con `authorType`)
- POST `/api/v1/recipes/viewer` | `/participant` | `/chef`
- GET `/api/v1/recipes`
- GET `/api/v1/recipes/{seq}`
- GET `/api/v1/recipes/type/{viewer|participant|chef}`
- GET `/api/v1/recipes/season/{n}`
- GET `/api/v1/recipes/search/by-ingredient?ingredient=Queso`
- PUT `/api/v1/recipes/{seq}`
- DELETE `/api/v1/recipes/{seq}`
## Notas
- `season` es obligatoria cuando `authorType=participant` (HTTP 400 si falta).
- `seq` se genera con colección `counters` usando `findAndModify`.
