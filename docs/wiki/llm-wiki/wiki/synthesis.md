# Síntesis del proyecto

## HECHO

El proyecto es un laboratorio académico de agendamiento de citas con datos sintéticos. Tiene dos repositorios independientes: un backend Java/Spring Boot y un frontend TypeScript que lo consume directamente por REST.

La aplicación debe cubrir registro, autenticación JWT con refresh, perfil y afiliación, catálogos, profesionales, disponibilidad, citas generales y especializadas, cancelación, reprogramación, agenda profesional, cierre de atención, bandeja administrativa y auditoría de estados.

## Estado actual

- `citas-api` implementa consulta de planes activos y registro USER con afiliación opcional persistida por FK.
- `citas-api` implementa reservas por slots: la cita general nace `APPROVED`, la especializada `REQUESTED`, y ningún slot puede asociarse a dos citas.
- `citas-api` permite a ADMIN aprobar/rechazar solicitudes especializadas; el rechazo exige motivo y libera slots. USER puede cancelar citas futuras propias. Todos los cambios iniciales se registran en `appointment_history`.
- `citas-web` usa React, TypeScript y Vite; la interfaz importada desde Stitch se conecta a la API para planes, citas y ciclo inicial. La bandeja ADMIN mantiene el sistema visual Stitch.
- Perfil/afiliación editable, catálogos configurables, reprogramación y agenda PROFESSIONAL aún requieren contrato e implementación.
- Ambos repositorios usan `develop` como rama de trabajo.

## Verificación del slice de afiliación

- Backend: pruebas de integración REST/persistencia cubren planes activos, registro sin plan, creación de FK, planes inexistentes/inactivos y ausencia de nombres de catálogo en `users`.
- Frontend: pruebas del cliente REST cubren carga, envío opcional/seleccionado y error `INVALID_PLAN`; typecheck y build pasan.

## Enlaces

- [Dominio](domain.md)
- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
