# Decisiones

## DECISIÓN — 2026-09-18

- La Wiki global se versiona dentro de `citas-api/docs/wiki/llm-wiki/`.
- `raw/` preserva fuentes curadas como instantáneas inmutables.
- `main` permanece estable y `develop` es la rama de trabajo de ambos repositorios.

## DECISIÓN — 2026-09-23

- Aprobado para el alcance de registro: `GET /api/v1/plans/active` devuelve `id`, `name`, `epsName`; `POST /api/v1/auth/register` acepta `planId` opcional y responde `400 INVALID_PLAN` cuando el plan no existe o está inactivo.
- Aprobado para reservas: `POST /api/v1/appointments` recibe usuario, profesional, especialidad, inicio y tipo. Los slots se bloquean en transacción y una restricción única por slot evita asociaciones duplicadas. Las generales nacen `APPROVED`; las especializadas, `REQUESTED`.
- Los detalles están en [Contrato REST](contracts/rest.md).

## DECISIÓN — 2026-09-25

- Aprobación explícita del usuario: el refresh token viaja en cookie `HttpOnly`; la interfaz no lo guarda ni lo lee. La política de implementación elegida rota el refresh token por uso, persiste únicamente su hash y separa el secreto de access JWT. Se usan TTL de 15 minutos para access y 7 días para refresh desde los valores de configuración de ejemplo existentes.
- Aprobación explícita del usuario: `America/Bogota` es la zona horaria operativa de agenda; el contrato transporta instantes ISO-8601 con zona/UTC.
- El ZIP entregado de Stitch/AI Studio es la fuente visual del frontend USER. El repo ya comparte la mayoría de esos componentes; al reconciliar se conserva la integración REST, assets locales y restricciones arquitectónicas, no los fragmentos del ZIP que los eliminan o contradicen.
- El acople del portal USER usa únicamente datos REST para especialidades, sedes, profesionales, disponibilidad y citas. Se conserva el lenguaje visual Stitch, pero se retiran de la navegación clínica, recetas, videollamada y avisos simulados por estar fuera del PRD.
- La migración V4 incorpora las dos sedes públicas del PRD. Los slots/citas previos quedan con sede nula para evitar una asignación retroactiva ficticia; las reservas nuevas exigen `venueId` y slots de esa sede.
- `DEMO_SEED=true` habilita datos sintéticos locales y horarios próximos para recorrer el portal sin credenciales de profesionales. La opción queda desactivada por defecto en el servicio.
- Las decisiones de cita siguen el PRD: ADMIN solo decide solicitudes futuras `REQUESTED`; rechazo requiere motivo y libera slots, mientras USER puede cancelar citas propias futuras. V5 registra los cambios del ciclo inicial en auditoría. El portal ADMIN hereda los componentes y paleta Stitch; no se copiaron módulos de IA Studio que introducen Express/Gemini.
- La agenda PROFESSIONAL usa identidad del JWT, solo devuelve citas propias `APPROVED` dentro de un rango máximo de 31 días y filtra opcionalmente por sede. Solo permite cerrar citas ya iniciadas; el resultado y fuente `PROFESSIONAL` quedan auditados (V6 amplía el catálogo de fuentes).

## Pendientes de decisión

Consultar [riesgos y preguntas abiertas](risks-and-open-questions.md).
