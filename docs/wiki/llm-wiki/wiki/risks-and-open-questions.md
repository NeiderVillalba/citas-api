# Riesgos y preguntas abiertas

## PREGUNTAS ABIERTAS

1. Definir contrato REST de perfil/afiliación editable, CRUD administrativo de EPS/planes/especialidades/profesionales, bloques de agenda, reprogramaciones y agenda/cierre PROFESSIONAL. El ciclo inicial de decisiones, cancelación e historial está en [Contrato REST](contracts/rest.md).
2. Definir transiciones y auditoría para reprogramaciones; los estados iniciales de cita y las transiciones USER/ADMIN implementadas están en [Contrato REST](contracts/rest.md). `COMPLETED` y `NO_SHOW` todavía no tienen transición profesional.
3. Definir qué atributos históricos deben conservarse como snapshot y cuáles mantenerse por FK.
4. Definir el catálogo definitivo de especialidades y sus duraciones; el seed local actual es sintético y opcional.
5. Confirmar si la afiliación EPS conserva historial o solo representa la afiliación actual.
6. Diseñar pantallas de agenda PROFESSIONAL a partir del PRD; la bandeja ADMIN ya se implementó con los estilos Stitch porque el ZIP contiene solo el portal USER.
7. La ejecución de workflows depende de la instancia MCP/n8n y credenciales OAuth del trainer, no disponibles en el workspace local.

## RIESGOS CONTROLADOS

- No usar `database/reference/` hasta que el trainer lo habilite como fuente.
- No abrir ni reproducir valores de `.env`.
- No usar datos privados reales de FCV.

## Enlaces

- [Modelo de datos](data-model.md)
- [Contrato REST](contracts/rest.md)
