# Flujo de INGEST

1. Confirmar que la fuente está aprobada o curada y no contiene información sensible.
2. Añadir una nueva instantánea inmutable en `raw/sources/`.
3. Consultar `wiki/index.md` y las páginas relacionadas.
4. Integrar únicamente conocimiento durable, con clasificación y enlaces a la fuente.
5. Actualizar `wiki/index.md` si se crea, elimina o reestructura una página.
6. Añadir una entrada append-only en `wiki/log.md`.
7. Ejecutar o planear un LINT cuando cambie una fuente normativa o un contrato.

No reinterpretar un requisito ambiguo como decisión. Registrar la ambigüedad en `wiki/risks-and-open-questions.md`.
