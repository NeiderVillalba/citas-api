# citas-api

Backend Spring Boot del laboratorio de citas. Incluye registro y sesión USER, catálogos de reserva, consulta de disponibilidad y citas propias, y reserva transaccional.

## Stack
- Java 21 + Spring Boot 3.5.x + Maven.
- Arquitectura hexagonal.
- MySQL + Flyway.
- Spring Security + JWT access/refresh.
- REST.
- Pruebas.

## Ejecutar y verificar

Requiere Java 21, Maven y MySQL 8.4. Configura las variables de .env.example en el entorno local y crea previamente la base de datos indicada por DB_NAME.

```powershell
mvn spring-boot:run
```

Para ejecutar las pruebas de integración con H2:

```powershell
mvn test
```

## Contrato implementado

- GET /api/v1/plans/active
- POST /api/v1/auth/register
- POST /api/v1/auth/login, /refresh, /logout
- GET /api/v1/specialties/active, /api/v1/venues, /api/v1/professionals, /api/v1/availability
- GET /api/v1/appointments/mine
- POST /api/v1/appointments

El contrato y sus errores están documentados en docs/wiki/llm-wiki/wiki/contracts/rest.md.

Para recorrer el flujo de reserva en un entorno local vacío, define `DEMO_SEED=true`. El arranque crea dos profesionales y slots sintéticos para los siguientes días hábiles. Sus contraseñas se generan aleatoriamente y no se muestran. Mantén esta opción desactivada fuera del laboratorio.

## Documentación compartida
- `docs/wiki/scrum/`: épicas/HU generadas con la Skill Scrum.
- `docs/wiki/llm-wiki/`: única LLM Wiki global del workspace.
- `automations/n8n/`: JSON exportados en S5/S6.

Lee el PRD en la carpeta raíz antes de inicializar Spring Boot.
