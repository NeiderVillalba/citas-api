# Arquitectura

## HECHO

- Backend: Java 21 LTS, Spring Boot 3.5.x, Maven, arquitectura hexagonal, Spring Data JPA, MySQL 8.4, Flyway, Spring Security y JWT access/refresh.
- Frontend: TypeScript con React o Angular; la elección depende de Stitch y Google AI Studio.
- Integración: REST/JSON directo desde el frontend a Spring Boot. No hay Express ni BFF.
- La URL backend debe ser configurable por environment.
- Los workflows n8n se versionan como JSON en `citas-api/automations/n8n/`.

## Estado observado

`citas-web` está importado como React + TypeScript + Vite. `citas-api` se inicializó con Spring Boot y estructura por puertos y adaptadores. El backend sigue creciendo por slices; no inferir que los recursos del PRD ya están implementados.

## Evidencia

- [Restricciones técnicas](../raw/sources/RESTRICCIONES_TECNICAS.md)
- [README de citas-web](../raw/sources/README-citas-web.md)
