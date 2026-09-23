# Síntesis del proyecto

## HECHO

El proyecto es un laboratorio académico de agendamiento de citas con datos sintéticos. Tiene dos repositorios independientes: un backend Java/Spring Boot y un frontend TypeScript que lo consume directamente por REST.

La aplicación debe cubrir registro, autenticación JWT con refresh, perfil y afiliación, catálogos, profesionales, disponibilidad, citas generales y especializadas, cancelación, reprogramación, agenda profesional, cierre de atención, bandeja administrativa y auditoría de estados.

## Estado actual

- `citas-api` implementa consulta de planes activos y registro USER con afiliación opcional persistida por FK.
- `citas-web` usa React, TypeScript y Vite; el formulario carga planes activos y permite continuar sin elegir uno.
- El subcontrato REST de planes y registro está aprobado; los demás recursos aún requieren contrato.
- Ambos repositorios usan `develop` como rama de trabajo.

## Verificación del slice de afiliación

- Backend: pruebas de integración REST/persistencia cubren planes activos, registro sin plan, creación de FK, planes inexistentes/inactivos y ausencia de nombres de catálogo en `users`.
- Frontend: pruebas del cliente REST cubren carga, envío opcional/seleccionado y error `INVALID_PLAN`; typecheck y build pasan.

## Enlaces

- [Dominio](domain.md)
- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
