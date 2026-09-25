# Índice de la LLM Wiki

## Estado

- Última actualización: 2026-09-25
- Alcance: coordinación global de `citas-api` y `citas-web`
- Estado de implementación: portal USER Stitch/React conectado a registro, sesión, búsqueda/reserva, cancelación e historial; bandeja ADMIN para aprobación/rechazo; seed sintético local opcional. Perfil, catálogos configurables, reprogramación y agenda PROFESSIONAL siguen pendientes.

## Fuentes RAW vigentes

- [PRD v1.0](../raw/sources/PRD-v1.0.md): fuente funcional principal.
- [Restricciones técnicas](../raw/sources/RESTRICCIONES_TECNICAS.md): fuente arquitectónica y operativa.
- [Requisitos de normalización 3FN](../raw/sources/REQUISITOS_NORMALIZACION_3FN.md): restricciones de modelado.
- [README del workspace](../raw/sources/README-workspace.md): contexto académico y de repositorios.
- [README de citas-api](../raw/sources/README-citas-api.md): alcance inicial del backend.
- [README de citas-web](../raw/sources/README-citas-web.md): alcance inicial del frontend.

La solución de referencia de base de datos del trainer no está ingerida ni debe usarse hasta autorización explícita.

## Páginas

- [Síntesis](synthesis.md)
- [Dominio](domain.md)
- [Arquitectura](architecture.md)
- [Modelo de datos](data-model.md)
- [Contrato REST](contracts/rest.md)
- [Decisiones](decisions.md)
- [Preferencias](preferences.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
- [Registro cronológico](log.md)
- [Evidencia S3 de hooks](../../../verification/S3-quality-gates.md)

## Operación

Aplicar las convenciones de [schema/conventions.md](../schema/conventions.md) y el [flujo de ingestión](../schema/ingest-workflow.md).
