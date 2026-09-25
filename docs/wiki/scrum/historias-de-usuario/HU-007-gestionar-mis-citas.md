---
id: HU-007
tipo: historia-de-usuario
titulo: "Consultar, cancelar y reprogramar mis citas"
estado: Borrador
epica: "[[EP-003-ciclo-de-vida-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S4"
dependencias: ["[[HU-005-buscar-y-reservar-cita]]", "[[HU-006-gestion-administrativa-de-solicitudes]]"]
relacionadas: []
---

# HU-007 — Consultar, cancelar y reprogramar mis citas

## Historia de usuario

**COMO** USER  
**QUIERO** consultar mis citas, cancelar las permitidas y solicitar reprogramación  
**PARA** administrar mi agenda sin perder la reserva vigente antes de una decisión.

## Tareas

- [ ] Definir contratos de consulta, cancelación y reprogramación.
- [ ] Implementar ownership, transiciones, reservas provisionales e historial.
- [ ] Conectar lista/detalle y formularios React.

## Criterios de aceptación

### CA-01 — Mis citas
**Dado** un USER autenticado **cuando** consulta por estado/fecha **entonces** ve exclusivamente sus citas con sede, profesional, especialidad, hora, duración, estado y motivo disponible.

### CA-02 — Cancelación
**Dado** una cita propia futura no terminal **cuando** la cancela **entonces** cambia a `CANCELLED`, registra auditoría y libera slots; no puede reactivarse directamente.

### CA-03 — Solicitud de reprogramación
**Dado** una cita propia futura `APPROVED` **cuando** solicita una franja compatible **entonces** retiene la nueva franja, conserva la cita/slots originales y mantiene profesional/especialidad.

### CA-04 — Decisión
**Dado** una reprogramación pendiente **cuando** ADMIN aprueba/rechaza **entonces** se intercambian/liberan franjas al aprobar o solo se libera la provisional al rechazar; el motivo requerido se audita.

### CA-05 — Ownership
**Dado** id de cita ajeno **cuando** se consulta/cancela/reprograma **entonces** no se revela ni modifica la cita.

## Definition of Done

- [ ] Contratos y estados documentados; reglas transaccionales implementadas.
- [ ] Pruebas de ownership, terminalidad, reservas antiguas/nuevas, rechazo y liberación.
- [ ] UI integrada y verifica cargas/errores/confirmaciones.
- [ ] Evidencia por criterio; backend/frontend verificables.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–05 | Pendiente | — | — |
| DoD | Pendiente | — | — |
