---
id: HU-005
tipo: historia-de-usuario
titulo: "Buscar disponibilidad y reservar cita"
estado: Borrador
epica: "[[EP-003-ciclo-de-vida-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S3"
dependencias: ["[[HU-001-autenticacion-y-sesion]]", "[[HU-003-catalogos-y-profesionales]]", "[[HU-004-gestionar-disponibilidad]]"]
relacionadas: ["[[HU-006-gestion-administrativa-de-solicitudes]]"]
---

# HU-005 — Buscar disponibilidad y reservar cita

## Historia de usuario

**COMO** USER  
**QUIERO** filtrar horarios disponibles y reservar una cita general o especializada  
**PARA** obtener atención en una sede, especialidad y horario adecuados.

## Alcance

Buscar por sede, tipo, especialidad, profesional y fecha. Mostrar solo franjas que satisfacen duración 30/60. La API deriva usuario autenticado; no confiar `userId` enviado por cliente. GENERAL queda `APPROVED`; SPECIALIZED `REQUESTED`.

## Tareas

- [ ] Documentar endpoints de consulta y reserva, errores y estados.
- [ ] Implementar disponibilidad con filtros y reserva transaccional autenticada.
- [ ] Conectar la vista React; eliminar horarios/doctores ficticios del flujo de reserva.

## Criterios de aceptación

### CA-01 — Búsqueda
**Dado** filtros válidos **cuando** USER busca **entonces** solo aparecen horarios futuros, activos y completos para la duración requerida.

### CA-02 — Duración y exclusión
**Dado** especialidad de 60 minutos **cuando** busca/reserva **entonces** requiere dos slots consecutivos; ningún slot puede asociarse a dos citas.

### CA-03 — Estados iniciales
**Dado** un horario disponible **cuando** USER confirma GENERAL/SPECIALIZED **entonces** se crea respectivamente `APPROVED`/`REQUESTED` y se retienen slots.

### CA-04 — Conflictos
**Dado** un slot ocupado/inválido **cuando** se reserva **entonces** API devuelve conflicto estable y la UI permite elegir otro horario.

### CA-05 — Identidad
**Dado** un cliente que altera un id de usuario **cuando** solicita cita **entonces** la API atribuye la cita al principal autenticado.

## Definition of Done

- [ ] Contrato documentado en Wiki y compatible en API/web.
- [ ] Pruebas de filtros, 30/60, doble reserva concurrente, ownership y errores.
- [ ] UI conectada y con estados de carga/vacío/error/éxito.
- [ ] Backend y frontend pasan verificaciones aplicables; evidencia registrada.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–05 | Pendiente | — | — |
| DoD | Pendiente | — | — |
