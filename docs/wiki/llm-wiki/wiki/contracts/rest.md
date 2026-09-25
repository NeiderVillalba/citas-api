# Contrato REST

## Estado

El PRD exige una API REST JSON consumida directamente por el frontend. Los subcontratos de carga de planes, registro USER, reserva inicial, sesión y ciclo inicial de citas descritos aquí son la base vigente. Perfil, catálogos configurables, agenda profesional y reprogramación siguen pendientes.

## Sesión y autenticación

- `POST /api/v1/auth/login`: cuerpo `{ "email": string, "password": string }`. Respuesta `200` `{ "accessToken": string, "tokenType": "Bearer", "expiresIn": number, "user": { "id": number, "firstName": string, "lastName": string, "email": string, "roles": string[] } }`; emite cookie `refresh_token`.
- `POST /api/v1/auth/refresh`: no requiere cuerpo; requiere cookie `refresh_token` y encabezado `X-Requested-With: citas-web`. Rota refresh token y emite access token nuevo con la misma forma de login.
- `POST /api/v1/auth/logout`: revoca refresh asociado a cookie y la elimina; responde `204`.
- `POST /api/v1/auth/password-reset/request`: cuerpo `{ "email": string }`; responde `202` con mensaje genérico, sin confirmar existencia de cuenta. En perfil local, la entrega de token solo se permite mediante configuración explícita protegida.
- `POST /api/v1/auth/password-reset/confirm`: cuerpo `{ "token": string, "newPassword": string }`; consume token temporal de un solo uso; respuesta `204`.
- Passwords se hashean con BCrypt; access JWT vence en 15 minutos y refresh JWT en 7 días según parámetros de ejemplo configurables. Refresh JWT usa secreto distinto, se rota al usarlo y su hash se registra para revocación.
- La cookie es `HttpOnly`, `SameSite=Lax`, `Path=/api/v1/auth`, `Secure` en entornos no locales, y con expiración de 7 días. El `Secure`/SameSite/origen se configuran para permitir despliegue sin debilitar producción.
- CORS acepta el origen de frontend configurado con credenciales; operaciones con cookie exigen origen permitido y encabezado no simple. El frontend conserva access token solo en memoria y usa `credentials: include`.
- Respuesta `401` genérica para credenciales inválidas; `401` para refresh ausente, expirado, revocado o inválido. No se entregan detalles que permitan enumerar cuentas.

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
- Requiere access JWT con rol `USER`; la identidad se toma del token. El cliente no envía `userId`.
- Cuerpo: `professionalId`, `specialtyId`, `venueId`, `startsAt` (ISO-8601 con zona) y `appointmentType` (`GENERAL` o `SPECIALIZED`).
- La duración no viaja en el cliente: se deriva de la especialidad activa (30 o 60 minutos). Los slots de disponibilidad deben existir, ser consecutivos y pertenecer al profesional.
- Respuesta exitosa: `201 Created` con `{ "id": number, "status": "APPROVED" | "REQUESTED" }`.
- Una cita `GENERAL` nace `APPROVED`; una `SPECIALIZED` nace `REQUESTED`. Ambos estados retienen sus slots mientras estén vigentes.
- Si algún slot solicitado ya está asociado a otra cita, o no existe como disponibilidad del profesional, responde `409 Conflict` con `{ "code": "SLOT_UNAVAILABLE", "message": string }`.
- La reserva bloquea transaccionalmente los slots de disponibilidad en orden ascendente y la unicidad de `appointment_slots.slot_id` es la salvaguarda de persistencia contra doble reserva.
- No se permiten reservas en el pasado. `appointmentType` debe ser `GENERAL` para `Medicina General` y `SPECIALIZED` para las demás especialidades.

## Consulta para el portal USER

Los endpoints de consulta del portal USER requieren access JWT con rol `USER`. Ninguno acepta un `userId` proporcionado por el cliente.

- `GET /api/v1/specialties/active`: arreglo de `{ id, name, durationMinutes, appointmentType }`, ordenado por nombre. `appointmentType` es `GENERAL` para Medicina General y `SPECIALIZED` para el resto.
- `GET /api/v1/venues`: catálogo fijo `{ id, code, name, address }` para HIC e ICV.
- `GET /api/v1/professionals?specialtyId={id}`: arreglo de `{ id, firstName, lastName }` activos y asociados a la especialidad activa.
- `GET /api/v1/availability?specialtyId={id}&professionalId={id}&venueId={id}&from={ISO-8601}&to={ISO-8601}`: arreglo ordenado de instantes ISO-8601. Solo incluye horarios futuros libres en la sede, con todos los slots consecutivos de la duración de la especialidad. El rango máximo es 31 días; los filtros inválidos reciben `400 INVALID_APPOINTMENT_REQUEST`. La disponibilidad es orientativa y se revalida al reservar.
- `GET /api/v1/appointments/mine`: arreglo ordenado por fecha descendente de `{ id, professionalId, professionalName, specialtyId, specialtyName, durationMinutes, venueId, venueName, venueAddress, startsAt, appointmentType, status, rejectionReason }` perteneciente al JWT. `status` puede ser `APPROVED`, `REQUESTED`, `REJECTED`, `CANCELLED`, `COMPLETED` o `NO_SHOW`. `rejectionReason` es `null` salvo en una cita rechazada. Los campos de sede pueden ser `null` en citas anteriores a la migración V4.

## Decisión, cancelación e historial de citas

- `GET /api/v1/admin/appointments/pending`: requiere rol `ADMIN`; devuelve citas especializadas `REQUESTED`, ordenadas por inicio ascendente, con forma `{ appointment: MyAppointment, patientName }`.
- `POST /api/v1/admin/appointments/{id}/decision`: requiere rol `ADMIN`. Cuerpo `{ "approve": boolean, "reason"?: string }`. Solo permite decidir solicitudes futuras `REQUESTED`. Aprobar cambia a `APPROVED` y conserva la reserva. Rechazar requiere un motivo no vacío de máximo 1000 caracteres, cambia a `REJECTED`, lo devuelve en `rejectionReason` y libera los slots. Responde `200` con `MyAppointment` actualizado.
- `POST /api/v1/appointments/{id}/cancel`: requiere rol `USER` y ownership. Solo cancela citas `APPROVED` o `REQUESTED` futuras. Cambia a `CANCELLED`, libera los slots y responde `200` con `{ "id": number, "status": "CANCELLED" }`.
- `GET /api/v1/appointments/{id}/history`: requiere rol `USER` propietario o `ADMIN`. Responde con eventos ordenados `{ id, status, actorId, source, changedAt, reason }`; `source` es `SYSTEM`, `USER` o `ADMIN`. El evento inicial también se registra.
- Decisión o cancelación fuera de transición válida: `409 INVALID_APPOINTMENT_TRANSITION`. Rechazo sin motivo: `400 INVALID_APPOINTMENT_REQUEST`. Cita inexistente o fuera de ownership: `404 APPOINTMENT_NOT_FOUND` para no revelar registros de otros usuarios. Rol insuficiente: `403`; sesión ausente o inválida: `401`.

La migración Flyway V5 amplía los estados permitidos, agrega `rejection_reason` e introduce `appointment_history`, incluido el registro inicial de citas existentes. `COMPLETED` y `NO_SHOW` están permitidos en persistencia para la evolución del PRD; sus transiciones profesionales aún no están expuestas por REST.

La migración V4 agrega el catálogo de sedes y su vínculo con slots/citas. Los slots anteriores quedan sin sede para no atribuirles una ubicación inventada; solo los slots nuevos con sede se ofrecen para reservar.

## Regla de evolución

Antes de modificar un contrato se debe documentar la propuesta, identificar ambos repositorios afectados y aportar evidencia backend y frontend. La implementación debe seguir este subcontrato aprobado.

## Evidencia

- [PRD v1.0](../../raw/sources/PRD-v1.0.md)
- [Restricciones técnicas](../../raw/sources/RESTRICCIONES_TECNICAS.md)
