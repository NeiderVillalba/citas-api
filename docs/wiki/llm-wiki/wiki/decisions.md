# Decisiones

## DECISIÓN — 2026-09-18

- La Wiki global se versiona dentro de `citas-api/docs/wiki/llm-wiki/`.
- `raw/` preserva fuentes curadas como instantáneas inmutables.
- `main` permanece estable y `develop` es la rama de trabajo de ambos repositorios.

## DECISIÓN — 2026-09-23

- Aprobado para el alcance de registro: `GET /api/v1/plans/active` devuelve `id`, `name`, `epsName`; `POST /api/v1/auth/register` acepta `planId` opcional y responde `400 INVALID_PLAN` cuando el plan no existe o está inactivo.
- Los detalles están en [Contrato REST](contracts/rest.md).

## Pendientes de decisión

Consultar [riesgos y preguntas abiertas](risks-and-open-questions.md).
