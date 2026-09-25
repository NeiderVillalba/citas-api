---
id: EP-004
tipo: epica
titulo: "Experiencia web integrada y segura"
estado: Borrador
historias: ["[[HU-001-autenticacion-y-sesion]]", "[[HU-002-perfil-y-afiliacion]]", "[[HU-003-catalogos-y-profesionales]]", "[[HU-004-gestionar-disponibilidad]]", "[[HU-005-buscar-y-reservar-cita]]", "[[HU-006-gestion-administrativa-de-solicitudes]]", "[[HU-007-gestionar-mis-citas]]", "[[HU-008-agenda-y-cierre-profesional]]"]
dependencias: ["[[EP-001-identidad-y-perfiles]]", "[[EP-002-catalogos-profesionales-y-agenda]]", "[[EP-003-ciclo-de-vida-de-citas]]"]
---

# EP-004 — Experiencia web integrada y segura

## Objetivo y valor

Dar a USER, PROFESSIONAL y ADMIN pantallas funcionales, conectadas a la API y coherentes con el diseño aprobado.

## Actores

- USER, PROFESSIONAL, ADMIN

## Alcance

- Formularios, dashboards, disponibilidad, gestión de agenda y decisiones administrativas.
- Estados de carga, vacío, error, éxito y controles accesibles.
- Cliente REST directo; URL de API configurable por environment.

## Fuera de alcance

- BFF/Express y lógica de negocio exclusivamente en cliente.

## Dependencias y completitud

- Cada historia cross-repo especifica pantallas y contrato conjuntamente.
- [ ] No quedan flujos principales respaldados solo por datos simulados.

## Riesgos e incógnitas

- Evidencia de aprobación Stitch/AI Studio no está en el repositorio inspeccionado.
