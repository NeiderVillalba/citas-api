---
id: HU-009
tipo: historia-de-usuario
titulo: "Recordatorios de citas con n8n"
estado: Borrador
epica: "[[EP-005-automatizacion-y-evidencia-s2-s6]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S5"
dependencias: ["[[HU-005-buscar-y-reservar-cita]]"]
relacionadas: ["[[HU-010-notificaciones-de-cambio]]"]
---

# HU-009 — Recordatorios de citas con n8n

## Historia de usuario

**COMO** USER  
**QUIERO** recibir un recordatorio de mi cita aprobada próxima  
**PARA** tener presente cuándo y dónde debo asistir.

## Tareas

- [ ] Definir consulta segura de citas próximas y payload mínimo.
- [ ] Crear WF-001 con Schedule, API, filtro de estado, Gmail y registro de ejecución.
- [ ] Configurar credencial OAuth individual en n8n; exportar JSON sin credenciales.

## Criterios de aceptación

### CA-01 — Elegibilidad
**Dado** citas en ventana de recordatorio **cuando** corre el workflow **entonces** solo procesa citas `APPROVED` próximas y evita enviar a citas canceladas/rechazadas.

### CA-02 — Mensaje
**Dado** una cita elegible **cuando** se envía correo **entonces** el destinatario y datos mínimos de fecha/sede corresponden a la cita.

### CA-03 — Reintentos y trazabilidad
**Dado** error de API/Gmail **cuando** el workflow falla **entonces** deja resultado revisable y aplica reintento controlado sin duplicar indebidamente.

### CA-04 — Secretos
**Dado** JSON exportado **cuando** se inspecciona **entonces** no contiene OAuth, tokens, contraseñas ni credenciales.

## Definition of Done

- [ ] WF-001 JSON importable y validado con ejecución controlada en instancia autorizada.
- [ ] Evidencia MCP invocada por agente, scopes mínimos y riesgos residuales escritos.
- [ ] Nunca se usaron datos reales de pacientes.
- [ ] JSON y evidencia versionados en citas-api.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–04 | Pendiente | — | — |
| DoD | Pendiente | — | — |
