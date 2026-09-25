---
id: EP-002
tipo: epica
titulo: "Catálogos, profesionales y agenda"
estado: Borrador
historias: ["[[HU-003-catalogos-y-profesionales]]", "[[HU-004-gestionar-disponibilidad]]"]
dependencias: []
---

# EP-002 — Catálogos, profesionales y agenda

## Objetivo y valor

Permitir que ADMIN configure la oferta y que PROFESSIONAL publique disponibilidad válida en sus sedes y especialidades.

## Actores

- ADMIN, PROFESSIONAL, USER

## Alcance

- CRUD lógico de EPS, planes, especialidades y profesionales.
- Asignaciones de especialidad/sede.
- Bloques futuros sin solapamientos, discretizados en slots de 30 minutos.

## Fuera de alcance

- Borrado físico de catálogos referenciados; facturación y sistemas clínicos.

## Reglas y dependencias

- Profesionales y especialidades deben estar activos y relacionados para publicar/reservar.
- No crear bloques en el pasado ni solapados; duración de cita 30 o 60 minutos.
- Usa la regla horaria acordada al validar fechas.

## Historias

- [[HU-003-catalogos-y-profesionales]]
- [[HU-004-gestionar-disponibilidad]]

## Completitud

- [ ] Administrador y profesional ejecutan sus tareas con autorización y persistencia verificables.

## Riesgos e incógnitas

- La sede debe formar parte del modelo de disponibilidad y del contrato de búsqueda/reserva.
