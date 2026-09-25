---
id: HU-011
tipo: historia-de-usuario
titulo: "Verificación, ciclos autónomos y trazabilidad"
estado: Borrador
epica: "[[EP-005-automatizacion-y-evidencia-s2-s6]]"
esfuerzo: Medio
sprint_sugerido: "Sprint S4–S6"
dependencias: ["[[HU-001-autenticacion-y-sesion]]", "[[HU-005-buscar-y-reservar-cita]]"]
relacionadas: []
---

# HU-011 — Verificación, ciclos autónomos y trazabilidad

## Historia de usuario

**COMO** estudiante/desarrollador  
**QUIERO** ejecutar verificaciones, loops controlados y registrar evidencia por sesión  
**PARA** demostrar calidad, progreso y límites del agente.

## Tareas

- [ ] Configurar hooks de pruebas y detección de secretos ficticios.
- [ ] Ejecutar una demostración FAIL y PASS del hook, incluida credencial ficticia bloqueada.
- [ ] Registrar GOAL y dos loops Builder/Verifier con límite, condición de parada, escalamiento y logs.
- [ ] Completar matriz de evidencia S2–S6 y commits por repositorio.

## Criterios de aceptación

### CA-01 — Red/Green
**Dado** regla de negocio automatizada **cuando** se demuestra primero un caso rojo y luego su corrección **entonces** quedan resultados reproducibles.

### CA-02 — Hook y secreto
**Dado** hook configurado **cuando** falla una prueba o se introduce patrón ficticio de secreto **entonces** bloquea commit; corregido el problema permite pasar.

### CA-03 — Builder/Verifier
**Dado** loop ejecutado **cuando** builder entrega cambio **entonces** verifier aislado evalúa sin implementar y el log guarda iteración, comprobaciones, decisión y stop/escalamiento.

### CA-04 — Trazabilidad
**Dado** sesión S2–S6 **cuando** se cierra incremento **entonces** evidencia registra repo, rama, commit, HU, criterios, pruebas y pendientes.

## Definition of Done

- [ ] Evidencia por sesión en repos apropiados sin secretos ni datos reales.
- [ ] Hook se demuestra en FAIL y PASS y no deja el secreto ficticio en historial.
- [ ] Logs de loops y matriz de evidencia permiten reproducir el resultado.
- [ ] Rama `develop` trazable y `main` estable al cierre.

## Evidencia de validación

| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01–04 | Pendiente | — | — |
| DoD | Pendiente | — | — |
