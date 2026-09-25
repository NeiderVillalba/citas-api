# Síntesis del proyecto

## HECHO

El proyecto es un laboratorio académico de agendamiento de citas con datos sintéticos. Tiene dos repositorios independientes: un backend Java/Spring Boot y un frontend TypeScript que lo consume directamente por REST.

La aplicación debe cubrir registro, autenticación JWT con refresh, perfil y afiliación, catálogos, profesionales, disponibilidad, citas generales y especializadas, cancelación, reprogramación, agenda profesional, cierre de atención, bandeja administrativa y auditoría de estados.

## Estado actual

- `citas-api` implementa consulta de planes activos y registro USER con afiliación opcional persistida por FK.
- `citas-api` implementa reservas por slots: la cita general nace `APPROVED`, la especializada `REQUESTED`, y ningún slot puede asociarse a dos citas.
- `citas-api` permite a ADMIN aprobar/rechazar solicitudes especializadas; el rechazo exige motivo y libera slots. USER puede cancelar citas futuras propias. Todos los cambios iniciales se registran en `appointment_history`.
- `citas-api` limita la agenda a citas aprobadas del profesional autenticado y permite cerrar citas iniciadas como `COMPLETED` o `NO_SHOW`, auditando fuente `PROFESSIONAL`.
- `citas-web` usa React, TypeScript y Vite; Stitch se conecta a la API para planes, citas y ciclo inicial. Bandeja ADMIN y agenda PROFESSIONAL conservan el mismo sistema visual.
- CRUD de profesionales/catálogos, bloques de disponibilidad, perfil editable, reprogramación y automatizaciones n8n/MCP aún requieren implementación/ejecución.
- Ambos repositorios usan `develop` como rama de trabajo.

## Verificación del slice de afiliación

- Backend: pruebas de integración REST/persistencia cubren planes activos, registro sin plan, creación de FK, planes inexistentes/inactivos y ausencia de nombres de catálogo en `users`.
- Frontend: pruebas del cliente REST cubren carga, envío opcional/seleccionado y error `INVALID_PLAN`; typecheck y build pasan.

## Enlaces

- [Dominio](domain.md)
- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
