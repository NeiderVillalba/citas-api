---
id: HU-003
tipo: historia-de-usuario
titulo: "Catálogos y profesionales"
estado: Borrador
epica: "[[EP-002-catalogos-profesionales-y-agenda]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S3"
dependencias: ["[[HU-001-autenticacion-y-sesion]]"]
relacionadas: ["[[HU-004-gestionar-disponibilidad]]"]
---

# HU-003 — Catálogos y profesionales

## Historia de usuario

**COMO** ADMIN  
**QUIERO** administrar EPS, planes, especialidades y profesionales con sus asignaciones  
**PARA** mantener configurable la oferta de citas.

## Alcance

CRUD de EPS/planes/especialidades, creación y activación de profesionales, datos profesionales ficticios, asignación N:M de especialidades y sedes, especialidad primaria. No borrar físicamente catálogos referenciados.

## Tareas

- [ ] Acordar y documentar contrato REST admin.
- [ ] Completar migraciones/modelo, seeds fijos requeridos, validación y autorización.
- [ ] Crear vistas/formularios admin en React.

## Criterios de aceptación

### CA-01 — Acceso administrativo
**Dado** un ADMIN **cuando** gestiona esos recursos **entonces** crea/lee/actualiza/desactiva registros; USER/PROFESSIONAL reciben denegación.

### CA-02 — Relaciones
**Dado** un profesional **cuando** se guarda **entonces** tiene una o más asignaciones válidas de especialidad/sede y como máximo una especialidad primaria.

### CA-03 — Integridad
**Dado** un catálogo referenciado **cuando** ADMIN intenta borrarlo **entonces** se conserva y se desactiva cuando aplique.

### CA-04 — UI
**Dado** un formulario de gestión **cuando** guarda o falla validación **entonces** presenta resultado y errores accionables.

## Definition of Done

- [ ] Migraciones Flyway compatibles y modelo justificado 3FN.
- [ ] Pruebas de autorización, relaciones, duplicados y desactivación.
- [ ] Contrato, UI, lint/build y pruebas aplicables validados.
- [ ] Evidencia registrada para CA y DoD.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–04 | Pendiente | — | — |
| DoD | Pendiente | — | — |
