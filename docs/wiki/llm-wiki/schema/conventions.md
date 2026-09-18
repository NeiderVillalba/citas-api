# Convenciones de la LLM Wiki

## Propósito

La Wiki conserva conocimiento durable, verificable y reutilizable para coordinar `citas-api` y `citas-web`. No es un transcript de conversaciones ni un repositorio de secretos.

## Clasificación de contenido

- **HECHO:** respaldado por una fuente RAW, código o prueba verificable.
- **DECISIÓN:** elección aprobada, con fecha, responsable y consecuencias.
- **PREFERENCIA:** criterio estable de trabajo o presentación; no sustituye un requisito.
- **PREGUNTA ABIERTA:** ambigüedad que no puede resolverse por inferencia.
- **INFERENCIA:** conclusión provisional; debe indicar la evidencia que la sustenta y no se promueve a HECHO sin validación.

## Fuentes RAW

- Cada archivo de `raw/sources/` es una instantánea textual de una fuente curada.
- Una instantánea ya ingresada no se edita. Una revisión se agrega como un archivo nuevo con versión o fecha en el nombre.
- El índice debe indicar origen, versión conocida y vigencia de cada fuente.
- Nunca ingresar secretos, tokens, contraseñas, valores de `.env`, PII ni datos privados reales.

## Páginas WIKI

- Usar enlaces relativos y títulos estables.
- Vincular los HECHOS y DECISIONES a su fuente RAW o evidencia de código.
- Mantener el resumen en `synthesis.md`; evitar duplicar sus detalles en todas las páginas.
- Registrar los cambios de conocimiento en `log.md`.

## Registro

Cada entrada de `log.md` usa el formato:

`YYYY-MM-DD | OPERACIÓN | alcance | fuentes/evidencia | resultado`

Operaciones permitidas: `INGEST`, `QUERY`, `LEARN` y `LINT`.
