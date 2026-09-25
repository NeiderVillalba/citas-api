---
id: HU-002
tipo: historia-de-usuario
titulo: "Perfil y afiliación"
estado: Borrador
epica: "[[EP-001-identidad-y-perfiles]]"
esfuerzo: Medio
sprint_sugerido: "Sprint S2"
dependencias: ["[[HU-001-autenticacion-y-sesion]]"]
relacionadas: []
---

# HU-002 — Perfil y afiliación

## Historia de usuario

**COMO** USER  
**QUIERO** consultar y actualizar mis datos permitidos y mi EPS/plan  
**PARA** mantener mi perfil correcto sin duplicar datos de catálogo.

## Alcance

Extender el vínculo opcional de plan del registro existente con consulta/actualización de perfil y afiliación. La persona autenticada solo administra sus datos permitidos.

## Tareas

- [ ] Especificar endpoints/DTO de perfil y afiliación en contrato REST.
- [ ] Implementar casos de uso, ownership, persistencia y validaciones.
- [ ] Conectar pantalla de perfil y catálogos activos en React.

## Criterios de aceptación

### CA-01 — Consulta y actualización propia
**Dado** un USER autenticado **cuando** consulta/actualiza su perfil **entonces** ve sus datos permitidos y no puede cambiar rol, id ni datos de otra cuenta.

### CA-02 — Afiliación
**Dado** un plan activo existente **cuando** lo asocia **entonces** se conserva por FK; un plan inexistente/inactivo no se asocia.

### CA-03 — UI
**Dado** la pantalla de perfil **cuando** carga, guarda o falla **entonces** muestra datos/estados y permite corregir errores.

## Definition of Done

- [ ] Contrato API documentado y consistente en ambos repos.
- [ ] Pruebas de ownership, plan activo/inactivo y persistencia; sin duplicar nombres de catálogo en usuarios.
- [ ] React pasa verificaciones disponibles y muestra estados de carga/éxito/error.
- [ ] Evidencia de CA/DoD registrada.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
