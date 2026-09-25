# Backlog del proyecto de citas

## Estado

Historias en borrador, pendientes de revisión y aprobación. La implementación existente solo cubre parte del registro/afiliación y la retención inicial de slots; no se considera que el MVP esté completo.

## Stack observado

- API: Java 21, Spring Boot 3.5, Maven, MySQL/Flyway, arquitectura hexagonal.
- Web: React, TypeScript, Vite; consumo directo de la API REST.
- Zona horaria propuesta: `America/Bogota`, persistiendo instantes como ISO-8601/UTC.

## Épicas

- [[EP-001-identidad-y-perfiles]]
- [[EP-002-catalogos-profesionales-y-agenda]]
- [[EP-003-ciclo-de-vida-de-citas]]
- [[EP-004-interfaz-integrada-y-segura]]
- [[EP-005-automatizacion-y-evidencia-s2-s6]]

## Incrementos sugeridos

1. **S2 — Identidad:** HU-001 y HU-002.
2. **S3 — Disponibilidad y reserva:** HU-003, HU-004 y HU-005.
3. **S4 — MVP de citas:** HU-006, HU-007 y HU-008.
4. **S5 — Recordatorios y seguridad del agente:** HU-009.
5. **S6 — Notificaciones y cierre:** HU-010 y HU-011.

Las HU cross-repo requieren contrato REST, implementación API, adaptación web y evidencia de compatibilidad. No se consideran aprobadas hasta confirmación explícita.

## Decisiones pendientes

- Método de transporte del refresh token: cookie HttpOnly o respuesta JSON.
- Detalle de vencimiento/rotación/revocación de refresh tokens.
- Confirmar `America/Bogota` como regla operativa.
- Semilla y criterio de selección para Medicina General.
- Proveedor/instancia n8n y disponibilidad de MCP/Gmail del trainer.
- Aprobar visualmente las pantallas que deban seguir el diseño Stitch/AI Studio.
