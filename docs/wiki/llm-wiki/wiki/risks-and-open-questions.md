# Riesgos y preguntas abiertas

## PREGUNTAS ABIERTAS

1. Definir el contrato REST: rutas, formatos, errores, versionado y compatibilidad.
2. Definir estrategia transaccional para reservas y retenciones sin doble agenda.
3. Definir catálogo exhaustivo y transiciones válidas de estados de cita y reprogramación.
4. Definir TTL, rotación, almacenamiento y revocación de refresh tokens, además de la exposición segura del reset token en desarrollo.
5. Definir zona horaria y reglas precisas para validar pasado/futuro.
6. Definir qué atributos históricos deben conservarse como snapshot y cuáles mantenerse por FK.
7. Definir seed y regla operativa de `Medicina General`.
8. Elegir e importar React o Angular solo después de aprobación visual en Stitch/AI Studio.

## RIESGOS CONTROLADOS

- No usar `database/reference/` hasta que el trainer lo habilite como fuente.
- No abrir ni reproducir valores de `.env`.
- No usar datos privados reales de FCV.

## Enlaces

- [Modelo de datos](data-model.md)
- [Contrato REST](contracts/rest.md)
