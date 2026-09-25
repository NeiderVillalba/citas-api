---
id: EP-005
tipo: epica
titulo: "Automatización y evidencia S2–S6"
estado: Borrador
historias: ["[[HU-009-recordatorios-n8n]]", "[[HU-010-notificaciones-de-cambio]]", "[[HU-011-verificacion-y-trazabilidad]]"]
dependencias: ["[[EP-003-ciclo-de-vida-de-citas]]"]
---

# EP-005 — Automatización y evidencia S2–S6

## Objetivo y valor

Automatizar comunicaciones del laboratorio y conservar evidencia verificable del desarrollo, pruebas y operación del agente.

## Actores

- USER, ADMIN, desarrollador/estudiante, agente conectado a MCP.

## Alcance

- Recordatorios y notificaciones de estado con workflows JSON versionados.
- MCP n8n cuando la instancia y permisos del trainer estén disponibles.
- Evidencia de pruebas/hooks, loops Builder/Verifier, riesgos residuales y commits.

## Fuera de alcance

- Credenciales dentro del repositorio; activar flujos sin validación; sistemas reales de pacientes.

## Historias

- [[HU-009-recordatorios-n8n]]
- [[HU-010-notificaciones-de-cambio]]
- [[HU-011-verificacion-y-trazabilidad]]

## Completitud

- [ ] WF-001 y WF-002 importan sin secretos, ejecutan casos controlados y quedan evidenciados.
- [ ] Existe evidencia de pruebas, hooks, loops y commits por sesión.

## Riesgos e incógnitas

- La instancia MCP/n8n, OAuth Gmail y webhooks dependen del entorno del trainer.
