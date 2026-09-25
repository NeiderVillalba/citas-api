---
id: HU-004
tipo: historia-de-usuario
titulo: "Gestionar disponibilidad"
estado: Borrador
epica: "[[EP-002-catalogos-profesionales-y-agenda]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S3"
dependencias: ["[[HU-003-catalogos-y-profesionales]]"]
relacionadas: ["[[HU-005-buscar-y-reservar-cita]]"]
---

# HU-004 — Gestionar disponibilidad

## Historia de usuario

**COMO** PROFESSIONAL  
**QUIERO** publicar, editar, eliminar y consultar bloques disponibles por sede  
**PARA** ofrecer horarios de atención válidos.

## Alcance

Crear varios bloques futuros por día/sede, generar slots de 30 minutos, editar/eliminar bloques futuros libres y consultar calendario propio.

## Tareas

- [ ] Definir contrato de disponibilidad con fechas ISO-8601 y regla `America/Bogota`.
- [ ] Crear migración, validaciones de solapamiento, sede asignada y slots comprometidos.
- [ ] Crear vista de calendario profesional.

## Criterios de aceptación

### CA-01 — Crear bloque
**Dado** un profesional activo asignado a una sede **cuando** crea bloque futuro válido **entonces** se publican sus slots de 30 minutos.

### CA-02 — Reglas temporales
**Dado** un bloque en pasado, solapado o en sede no asignada **cuando** se intenta guardar **entonces** se rechaza sin alterar agenda.

### CA-03 — Cambiar bloque
**Dado** un bloque futuro sin slots comprometidos **cuando** se edita/elimina **entonces** se actualiza; con citas comprometidas se deniega.

### CA-04 — Ownership
**Dado** un profesional **cuando** consulta agenda **entonces** solo ve/gestiona sus bloques.

## Definition of Done

- [ ] Contrato REST documentado y alineado entre repos.
- [ ] Pruebas de pasado, solapamiento, sede, ownership y bloques comprometidos.
- [ ] UI calendario muestra carga/vacío/error/éxito y pasa verificaciones.
- [ ] Migración Flyway y evidencia registrada.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–04 | Pendiente | — | — |
| DoD | Pendiente | — | — |
