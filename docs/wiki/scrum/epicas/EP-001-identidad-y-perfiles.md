---
id: EP-001
tipo: epica
titulo: "Identidad, sesión y perfil"
estado: Borrador
historias: ["[[HU-001-autenticacion-y-sesion]]", "[[HU-002-perfil-y-afiliacion]]"]
dependencias: []
---

# EP-001 — Identidad, sesión y perfil

## Objetivo y valor

Permitir que pacientes ficticios creen y administren una cuenta segura y que los tres roles accedan únicamente a las capacidades autorizadas.

## Actores

- USER, PROFESSIONAL, ADMIN

## Alcance

- Registro y autenticación JWT; refresh, logout y recuperación de contraseña.
- Perfil y afiliación opcional a EPS/plan.
- Autorización por rol y ownership.

## Fuera de alcance

- Envío SMTP obligatorio, OAuth social o identidad de sistemas reales.

## Reglas y dependencias

- Passwords con hash adaptativo; nunca almacenar tokens en claro para uso persistente.
- Email y documento únicos; roles se asignan de forma controlada.
- La política de refresh y su transporte requieren decisión antes de cerrar el contrato.

## Historias

- [[HU-001-autenticacion-y-sesion]]
- [[HU-002-perfil-y-afiliacion]]

## Completitud

- [ ] Las historias obligatorias están completadas con evidencia backend, frontend y seguridad.

## Riesgos e incógnitas

- Transporte, TTL, rotación y revocación de refresh token pendientes de definición.
