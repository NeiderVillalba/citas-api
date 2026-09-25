# Riesgos y preguntas abiertas

## PREGUNTAS ABIERTAS

1. Definir contrato REST de perfil/afiliación, CRUD administrativo de catálogos/profesionales, bloques, decisiones administrativas, cancelación, reprogramación y agenda profesional. El contrato de lectura de catálogos/sedes/disponibilidad, citas propias, reserva, registro y sesión está en [Contrato REST](contracts/rest.md).
2. Definir catálogo exhaustivo y transiciones válidas de estados de cita y reprogramación, más allá de los estados iniciales descritos por el PRD.
3. Definir qué atributos históricos deben conservarse como snapshot y cuáles mantenerse por FK.
4. Definir el catálogo definitivo de especialidades y sus duraciones; el seed local actual es sintético y opcional.
5. Confirmar si la afiliación EPS conserva historial o solo representa la afiliación actual.
6. Diseñar pantallas ADMIN/PROFESSIONAL a partir del PRD; el ZIP entregado contiene solo el portal USER y no ofrece esas vistas.
7. La ejecución de workflows depende de la instancia MCP/n8n y credenciales OAuth del trainer, no disponibles en el workspace local.

## RIESGOS CONTROLADOS

- No usar `database/reference/` hasta que el trainer lo habilite como fuente.
- No abrir ni reproducir valores de `.env`.
- No usar datos privados reales de FCV.

## Enlaces

- [Modelo de datos](data-model.md)
- [Contrato REST](contracts/rest.md)
