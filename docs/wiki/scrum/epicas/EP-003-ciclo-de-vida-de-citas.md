---
id: EP-003
tipo: epica
titulo: "Ciclo de vida de citas"
estado: Borrador
historias: ["[[HU-005-buscar-y-reservar-cita]]", "[[HU-006-gestion-administrativa-de-solicitudes]]", "[[HU-007-gestionar-mis-citas]]", "[[HU-008-agenda-y-cierre-profesional]]"]
dependencias: ["[[EP-001-identidad-y-perfiles]]", "[[EP-002-catalogos-profesionales-y-agenda]]"]
---

# EP-003 — Ciclo de vida de citas

## Objetivo y valor

Completar el proceso desde encontrar un horario hasta atender, cancelar o reprogramar una cita, sin doble reserva y con auditoría.

## Actores

- USER, PROFESSIONAL, ADMIN

## Alcance

- Disponibilidad filtrable; reservas generales y especializadas.
- Aprobación/rechazo administrativo, mis citas, cancelación y reprogramación.
- Agenda del profesional, cierre de atención e historial de estados.

## Fuera de alcance

- Pagos, historia clínica y tratamiento médico.

## Reglas y dependencias

- Slots de 60 minutos son dos slots consecutivos; solicitudes especializadas retienen el horario.
- Rechazos requieren motivo; cancelaciones y rechazos liberan reservas pertinentes.
- La reprogramación conserva la cita original hasta decisión administrativa.

## Historias

- [[HU-005-buscar-y-reservar-cita]]
- [[HU-006-gestion-administrativa-de-solicitudes]]
- [[HU-007-gestionar-mis-citas]]
- [[HU-008-agenda-y-cierre-profesional]]

## Completitud

- [ ] Transiciones válidas, ownership, auditoría y exclusión de slots están probados.

## Riesgos e incógnitas

- Falta definir la matriz exhaustiva de transiciones y la selección de Medicina General.
