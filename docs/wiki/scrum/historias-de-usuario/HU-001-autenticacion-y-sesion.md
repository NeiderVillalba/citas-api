---
id: HU-001
tipo: historia-de-usuario
titulo: "Autenticación y sesión"
estado: Borrador
epica: "[[EP-001-identidad-y-perfiles]]"
esfuerzo: Alto
sprint_sugerido: "Sprint S2"
dependencias: []
relacionadas: ["[[HU-002-perfil-y-afiliacion]]"]
---

# HU-001 — Autenticación y sesión

## Historia de usuario

**COMO** USER, PROFESSIONAL o ADMIN  
**QUIERO** registrarme e iniciar, renovar y cerrar sesión de manera segura  
**PARA** usar las funciones que mi rol tiene autorizadas.

## Contexto y alcance

Completar el registro existente con login, access/refresh, logout/revocación y recuperación de contraseña. Mantener hash adaptativo y roles en el contexto autenticado. No incluye SMTP obligatorio. El transporte de refresh requiere decisión explícita registrada en Wiki antes de cerrar el contrato.

## Tareas

- [ ] Definir contrato y política de TTL/rotación/revocación.
- [ ] Implementar persistencia segura de sesión/tokens y endpoints Spring Security.
- [ ] Conectar formularios React y manejo de sesión/expiración.
- [ ] Probar casos positivos, credenciales inválidas, expiración, revocación y autorización.

## Criterios de aceptación

### CA-01 — Registro
**Dado** un visitante con datos válidos y únicos **cuando** se registra **entonces** se crea USER y la contraseña queda hasheada.

### CA-02 — Login
**Dado** credenciales válidas **cuando** inicia sesión **entonces** recibe access token y refresh según el contrato aprobado; credenciales inválidas no crean sesión.

### CA-03 — Renovación y cierre
**Dado** un refresh válido **cuando** renueva o cierra sesión **entonces** se rota/invalida según la política acordada y los tokens revocados no renuevan sesión.

### CA-04 — Recuperación
**Dado** una solicitud de recuperación **cuando** se emite y consume su token temporal de un solo uso **entonces** la contraseña se actualiza y el token deja de servir; la respuesta no revela si el correo existe.

### CA-05 — UI
**Dado** el flujo de acceso **cuando** ocurre carga, error o éxito **entonces** la UI presenta el estado sin mostrar ni persistir secretos de forma insegura.

## Definition of Done

- [ ] Contrato y decisión de seguridad registrados en Wiki.
- [ ] Pruebas cubren los CA de backend; autorización por rol se valida en endpoints protegidos.
- [ ] React consume el contrato y pasa lint/build/pruebas aplicables.
- [ ] No se registran passwords/tokens y secretos vienen de entorno.
- [ ] Matriz de evidencia completada antes de cerrar.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–05 | Pendiente | — | — |
| DoD | Pendiente | — | — |

## Notas y decisiones

- Refresh-token transport pendiente. TTL/rotación/revocación son preguntas abiertas.
