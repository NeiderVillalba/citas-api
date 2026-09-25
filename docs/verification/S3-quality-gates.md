# Evidencia S3 — hooks locales y protección de secretos

Fecha: 2026-09-25

Ramas: `develop` en `citas-api` y `citas-web`.

## Configuración

Cada clon habilita el hook versionado con:

```powershell
git config --local core.hooksPath .githooks
```

El hook bloquea archivos `.env` locales y patrones conocidos de credenciales en archivos staged. `.env.example` está permitido. Si la validación pasa, `citas-api` ejecuta `mvn test` (o Maven 3.9+/Java 21 dentro de Docker si Maven no está instalado) y `citas-web` ejecuta `npm run lint` y `npm test`.

## Prueba FAIL — contenido sensible

- Se stageó temporalmente un archivo de prueba con un valor sintético que coincidía con el patrón `API_KEY`.
- El hook de ambos repos devolvió código distinto de cero y bloqueó el commit indicando únicamente el nombre del archivo; no imprimió el valor.
- Se retiró el fixture del índice y del workspace antes de los commits. No contiene credenciales reales.

## Prueba PASS — correcciones y commits permitidos

- `citas-api`: `git hook run pre-commit` terminó con código `0`; el hook ejecutó `mvn -q test` en Docker con Java 21 y H2. Se validaron las cinco migraciones Flyway y 13 pruebas, sin fallos ni errores.
- `citas-web`: `git hook run pre-commit` terminó con código `0`; `npm run lint` pasó y `npm test` aprobó 9/9 casos del cliente REST.
- El build Vite de producción ya se había ejecutado con éxito para el mismo código frontend.
- Los commits S3 de hook y evidencia se crean en `develop`; el propio hook vuelve a ejecutarse antes de permitir cada commit.

## Límites de esta evidencia

Esto verifica los controles locales y las suites disponibles. No demuestra un recorrido E2E contra MySQL activo ni completa por sí solo las otras funciones S3/S4 del PRD.
