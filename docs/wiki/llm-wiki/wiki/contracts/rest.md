# Contrato REST

## Estado

El PRD exige una API REST JSON consumida directamente por el frontend. El subcontrato de carga de planes activos y registro USER fue aprobado el 2026-09-23. Otros recursos de la aplicación todavía requieren contrato.

## Planes activos

- `GET /api/v1/plans/active`
- Respuesta `200`: arreglo de `{ "id": number, "name": string, "epsName": string }`.
- Se incluyen planes cuyo propio catálogo tiene `active = true`, ordenados por EPS y nombre del plan.
- Sin planes activos, responde `200` con `[]`.

## Registro USER

- `POST /api/v1/auth/register`
- Requeridos: `firstName`, `lastName`, `documentType`, `documentNumber`, `email`, `phone`, `password`.
- `planId` es opcional. Omitido o `null` registra sin afiliación; con ID válido crea la afiliación vinculada por FK.
- Respuesta exitosa: `201 Created`, sin cuerpo.
- Si `planId` no existe o el plan está inactivo: `400 Bad Request`, cuerpo `{ "code": "INVALID_PLAN", "message": string }`. No se crea el usuario.
- Correo o documento duplicado: `409 Conflict`, cuerpo `{ "code": "USER_ALREADY_EXISTS", "message": string }`.

Los nombres de EPS y plan se exponen para mostrar el catálogo en el formulario, pero se almacenan en sus tablas de catálogo, no en `users`.

## Reserva de citas

- `POST /api/v1/appointments`
- Cuerpo: `userId`, `professionalId`, `specialtyId`, `startsAt` (ISO-8601 con zona) y `appointmentType` (`GENERAL` o `SPECIALIZED`).
- La duración no viaja en el cliente: se deriva de la especialidad activa (30 o 60 minutos). Los slots de disponibilidad deben existir, ser consecutivos y pertenecer al profesional.
- Respuesta exitosa: `201 Created` con `{ "id": number, "status": "APPROVED" | "REQUESTED" }`.
- Una cita `GENERAL` nace `APPROVED`; una `SPECIALIZED` nace `REQUESTED`. Ambos estados retienen sus slots mientras estén vigentes.
- Si algún slot solicitado ya está asociado a otra cita, o no existe como disponibilidad del profesional, responde `409 Conflict` con `{ "code": "SLOT_UNAVAILABLE", "message": string }`.
- La reserva bloquea transaccionalmente los slots de disponibilidad en orden ascendente y la unicidad de `appointment_slots.slot_id` es la salvaguarda de persistencia contra doble reserva.

## Regla de evolución

Antes de modificar un contrato se debe documentar la propuesta, identificar ambos repositorios afectados y aportar evidencia backend y frontend. La implementación debe seguir este subcontrato aprobado.

## Evidencia

- [PRD v1.0](../../raw/sources/PRD-v1.0.md)
- [Restricciones técnicas](../../raw/sources/RESTRICCIONES_TECNICAS.md)
