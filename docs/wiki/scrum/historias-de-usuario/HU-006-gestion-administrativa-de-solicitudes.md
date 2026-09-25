---
id: HU-006
tipo: historia-de-usuario
titulo: "Gestión administrativa de solicitudes"
estado: Borrador
epica: "[[EP-003-ciclo-de-vida-de-citas]]"
esfuerzo: Medio
sprint_sugerido: "Sprint S4"
dependencias: ["[[HU-005-buscar-y-reservar-cita]]"]
relacionadas: ["[[HU-007-gestionar-mis-citas]]"]
---

# HU-006 — Gestión administrativa de solicitudes

## Historia de usuario

**COMO** ADMIN  
**QUIERO** filtrar solicitudes especializadas y aprobarlas o rechazarlas con motivo  
**PARA** gestionar la agenda pendiente de forma auditable.

## Tareas

- [ ] Acordar transiciones y documentar contrato de bandeja/decisión.
- [ ] Implementar autorización ADMIN, cambios transaccionales y auditoría.
- [ ] Conectar bandeja y detalle de solicitud en React.

## Criterios de aceptación

### CA-01 — Bandeja
**Dado** citas `REQUESTED` **cuando** ADMIN filtra por sede/profesional/especialidad/fecha **entonces** obtiene solo coincidencias pendientes.

### CA-02 — Aprobar
**Dado** una solicitud vigente **cuando** ADMIN aprueba **entonces** pasa a `APPROVED` y conserva slots retenidos.

### CA-03 — Rechazar
**Dado** una solicitud **cuando** ADMIN rechaza con motivo **entonces** pasa a `REJECTED`, registra actor/motivo y libera slots; sin motivo se rechaza la operación.

### CA-04 — Permisos e idempotencia
**Dado** rol distinto de ADMIN o cita ya resuelta **cuando** intenta decidir **entonces** se deniega sin cambio adicional.

## Definition of Done

- [ ] Contrato documentado; transitions implementadas en backend.
- [ ] Pruebas para permisos, transiciones, motivo obligatorio, auditoría y liberación.
- [ ] UI muestra bandeja, confirmación y errores; lint/build/pruebas aplicables pasan.
- [ ] Evidencia registrada.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–04 | Pendiente | — | — |
| DoD | Pendiente | — | — |
