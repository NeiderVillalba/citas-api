---
id: HU-010
tipo: historia-de-usuario
titulo: "Notificaciones de cambio de estado"
estado: Borrador
epica: "[[EP-005-automatizacion-y-evidencia-s2-s6]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S6"
dependencias: ["[[HU-006-gestion-administrativa-de-solicitudes]]", "[[HU-007-gestionar-mis-citas]]"]
relacionadas: ["[[HU-009-recordatorios-n8n]]"]
---

# HU-010 — Notificaciones de cambio de estado

## Historia de usuario

**COMO** USER  
**QUIERO** recibir aviso cuando mi cita cambie de estado  
**PARA** saber el resultado de solicitudes, reprogramaciones o cancelaciones.

## Tareas

- [ ] Definir eventos salientes con datos mínimos y política de autenticación del webhook.
- [ ] Crear WF-002 Webhook → Gmail → resultado/trazabilidad.
- [ ] Exportar JSON y probar eventos/error/reintento.

## Criterios de aceptación

### CA-01 — Evento
**Dado** un cambio soportado **cuando** la API notifica **entonces** transmite evento, cita y estado nuevo sin secretos ni datos innecesarios.

### CA-02 — Correo
**Dado** evento válido **cuando** n8n lo procesa **entonces** envía mensaje al destinatario correcto con el estado correcto.

### CA-03 — Error y reintento
**Dado** webhook/Gmail indisponible **cuando** falla ejecución **entonces** el error se registra y la política de reintento no altera el estado de la cita.

### CA-04 — Seguridad
**Dado** webhook sin autenticación válida o payload inválido **cuando** llega **entonces** se rechaza y no dispara correo.

## Definition of Done

- [ ] Contrato evento documentado; ambos lados compatibles.
- [ ] WF-002 JSON importable sin credenciales y validado en ejecución controlada.
- [ ] Pruebas API y workflow cubren eventos, rechazo y errores.
- [ ] Evidencia registrada en `automations/n8n/`.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–04 | Pendiente | — | — |
| DoD | Pendiente | — | — |
