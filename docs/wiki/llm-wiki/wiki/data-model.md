# Modelo de datos

## HECHO

El modelo debe alcanzar 3FN y soportar usuarios con múltiples roles, profesionales, especialidades y sedes N:M, afiliaciones, bloques de disponibilidad, citas, estados, historial, reprogramaciones y tokens compatibles con el PRD.

Los catálogos fijos se precargan. EPS, planes y especialidades son configurables por ADMIN; los catálogos referenciados por transacciones no se eliminan físicamente.

## PREGUNTA ABIERTA

La estrategia concreta para evitar doble reserva, representar retenciones y decidir snapshots históricos no está prescrita. Requiere una decisión de diseño antes de implementar persistencia.

## Evidencia

- [Requisitos de normalización 3FN](../raw/sources/REQUISITOS_NORMALIZACION_3FN.md)
- [PRD v1.0](../raw/sources/PRD-v1.0.md)
