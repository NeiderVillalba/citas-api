# citas-api

Repositorio backend del proyecto. Implementa el slice inicial de registro USER y consulta de planes activos.

## Debe ser construido por el estudiante
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

El contrato y sus errores están documentados en docs/wiki/llm-wiki/wiki/contracts/rest.md.

## Documentación compartida
- `docs/wiki/scrum/`: épicas/HU generadas con la Skill Scrum.
- `docs/wiki/llm-wiki/`: única LLM Wiki global del workspace.
- `automations/n8n/`: JSON exportados en S5/S6.

Lee el PRD en la carpeta raíz antes de inicializar Spring Boot.
