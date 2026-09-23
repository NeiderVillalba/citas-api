# Modelo de datos

## HECHO

El modelo debe alcanzar 3FN y soportar usuarios con múltiples roles, profesionales, especialidades y sedes N:M, afiliaciones, bloques de disponibilidad, citas, estados, historial, reprogramaciones y tokens compatibles con el PRD.

Los catálogos fijos se precargan. EPS, planes y especialidades son configurables por ADMIN; los catálogos referenciados por transacciones no se eliminan físicamente.

La afiliación opcional del registro se almacena en `user_affiliations`, con una FK a `users` y otra a `eps_plans`. La tabla `users` no duplica nombres ni IDs de EPS o plan.

Las reservas usan `availability_slots` como inventario de franjas por profesional. `appointments` conserva la cita y `appointment_slots` enlaza cada cita con sus slots; una restricción única sobre `appointment_slots.slot_id` impide que un slot se asocie a dos citas. La operación bloquea los slots solicitados y verifica su consecutividad antes de crear las relaciones.

La cita almacena FKs a usuario, profesional y especialidad; `appointmentType` define si el estado inicial es `APPROVED` o `REQUESTED`. La duración se deriva de la especialidad y no se duplica en la cita.

## Evidencia

- [Requisitos de normalización 3FN](../raw/sources/REQUISITOS_NORMALIZACION_3FN.md)
- [PRD v1.0](../raw/sources/PRD-v1.0.md)
