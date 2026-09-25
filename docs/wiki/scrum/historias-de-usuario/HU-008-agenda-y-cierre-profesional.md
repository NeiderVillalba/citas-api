---
id: HU-008
tipo: historia-de-usuario
titulo: "Agenda y cierre profesional"
estado: Borrador
epica: "[[EP-003-ciclo-de-vida-de-citas]]"
esfuerzo: Medio
sprint_sugerido: "Sprint S4"
dependencias: ["[[HU-004-gestionar-disponibilidad]]", "[[HU-005-buscar-y-reservar-cita]]"]
relacionadas: []
---

# HU-008 — Agenda y cierre profesional

## Historia de usuario

**COMO** PROFESSIONAL  
**QUIERO** consultar mis citas aprobadas por día/semana/sede y registrar el resultado  
**PARA** gestionar mi atención sin acceder a pacientes fuera de mi agenda.

## Tareas

- [ ] Definir contrato de agenda y cierre.
- [ ] Implementar filtros, ownership profesional, estados terminales e historial.
- [ ] Construir agenda y acciones en React.

## Criterios de aceptación

### CA-01 — Agenda propia
**Dado** un PROFESSIONAL autenticado **cuando** consulta fechas/sede **entonces** ve solo citas `APPROVED` propias y los datos mínimos necesarios.

### CA-02 — Cerrar atención
**Dado** una cita pasada aplicable **cuando** marca `COMPLETED` o `NO_SHOW` **entonces** cambia estado y registra actor, fuente y fecha.

### CA-03 — Validar cambio
**Dado** cita futura/ajena/terminal o actor distinto **cuando** intenta cierre **entonces** se rechaza sin mutación.

## Definition of Done

- [ ] Contrato documentado y permisos por ownership.
- [ ] Pruebas de filtros, sede, actor y transiciones.
- [ ] UI conectada y verificaciones aplicables completadas.
- [ ] Evidencia registrada.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
