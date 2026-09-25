# Registro de operaciones

2026-09-18 | INGEST | base de la Wiki | README, PRD v1.0, restricciones técnicas, requisitos 3FN y README de ambos repositorios | se crearon instantáneas RAW, índice y síntesis inicial.

2026-09-18 | LEARN | coordinación del workspace | inspección de repositorios Git | ambos repositorios iniciaron en `main`; se creó `develop` como rama de trabajo en cada uno.

2026-09-18 | LEARN | guías de agentes por repositorio | instrucción aprobada del usuario | se crearon `AGENTS.md` de bootstrap para API y Web; deben depurarse tras inicializar Spring Boot o importar el frontend real.

2026-09-23 | QUERY | afiliación opcional durante registro | contrato REST actual, Wiki, historias Scrum y repositorios | requisito detenido: la API REST no tiene contrato documentado, no hay HU aprobada ni backend implementado; falta especificar consulta de planes activos y representación del plan opcional en el registro.

2026-09-23 | QUERY | revalidación de afiliación opcional durante registro | estado actual de ambos repositorios y contrato REST | sin cambios: siguen sin existir backend, contrato ni HU aprobada; se propuso un contrato mínimo para decisión del usuario antes de continuar.

2026-09-23 | LEARN | afiliación opcional durante registro | aprobación explícita del usuario | se aprobó el subcontrato GET planes activos y POST registro con planId opcional y error 400 INVALID_PLAN; el alcance ya puede implementarse.

2026-09-23 | LEARN | estado de implementación del slice de afiliación | código, migración Flyway y pruebas de ambos repositorios | API y frontend implementan el contrato aprobado; se actualizaron las síntesis y el índice para reflejar React/Vite y evidencia de verificación.
2026-09-23 | DECISIÓN | reservas sin doble agenda | aprobación explícita del usuario | `POST /api/v1/appointments` crea reservas generales `APPROVED` o especializadas `REQUESTED`; los slots se bloquean transaccionalmente y la asociación única por slot impide doble reserva.
2026-09-23 | LINT | reserva transaccional de slots | pruebas REST/persistencia de backend y verificación de frontend | `mvn test` aprobó 7 pruebas, incluidas las reservas general y especializada; el segundo intento sobre el mismo slot respondió `409 SLOT_UNAVAILABLE`. `npm test`, typecheck y build del cliente también aprobaron.
2026-09-25 | LEARN | decisiones de sesión y zona horaria | aprobación explícita del usuario; `.env.example`; requisitos del PRD | se aprobó refresh token en cookie HttpOnly y `America/Bogota`; se documentó rotación/hash de refresh, secretos separados y TTL de ejemplo 15 min/7 días para el nuevo subcontrato.
2026-09-25 | QUERY | integración de frontend Stitch/AI Studio | ZIP `medcitas---portal-médico-y-gestión-de-citas.zip` y árbol actual de `citas-web` | la mayoría de vistas/estilos ya coincide byte por byte; la variante del ZIP quita registro REST y añade Express/Gemini, por lo que esos cambios no se copian. El flujo vigente conserva assets locales, pruebas y consumo directo de Spring.
2026-09-25 | LEARN | acople REST del portal USER Stitch | PRD, HU-005/HU-007, código de ambos repositorios y ZIP entregado | se añadieron consultas de especialidades, sedes, profesionales, disponibilidad y citas propias; la reserva requiere sede y la interfaz usa datos REST. Se ocultaron pantallas fuera del PRD y se agregó seed sintético local opcional.
2026-09-25 | LINT | compilación del acople USER | Maven Java 21 con pruebas omitidas, TypeScript/Vite | compilación de API y frontend completada; no se verificó un recorrido contra MySQL en ejecución en esta sesión.

2026-09-25 | LEARN | ciclo inicial de vida de citas y acople ADMIN Stitch | PRD RF-12/RF-14/RF-19, API, portal React y contrato REST | V5 agrega estados extendidos, motivo de rechazo e historial; ADMIN puede aprobar/rechazar y USER cancelar; React conecta bandeja, cancelación e historial. Se mantienen pendientes perfil/catálogos configurables, reprogramación y agenda PROFESSIONAL.

2026-09-25 | LINT | ciclo inicial USER/ADMIN cross-repo | Maven/H2, pruebas del cliente REST, TypeScript y Vite | backend: 13 pruebas aprobadas; frontend: 7 pruebas, lint/typecheck y build de producción aprobados. El sandbox bloqueó inicialmente esbuild (`spawn EPERM`); el build se completó al ejecutar Vite con permiso revisado.

2026-09-25 | LINT | verificación final del acople Stitch | cambios finales de citas-web | se añadieron pruebas para la bandeja ADMIN, la decisión con motivo, la cancelación y la consulta de historial; suite final del cliente: 9/9 pruebas, typecheck y build de producción aprobados.

2026-09-25 | LEARN | gates de verificación S3 | Guía S2–S6, seguridad y ejecución de hooks en ambos repos | se versionaron hooks pre-commit con bloqueo de `.env`/credenciales y verificaciones; quedó registrada evidencia FAIL para fixture sintético y PASS para API (13 pruebas Maven) y web (9 pruebas + typecheck). Ver [evidencia S3](../../../verification/S3-quality-gates.md).

2026-09-25 | LEARN | agenda/cierre PROFESSIONAL | PRD RF-16/RF-17, contrato REST, código y pruebas cross-repo | se añadió consulta de agenda por fechas/sede y cierre `COMPLETED`/`NO_SHOW` con ownership y auditoría; UI Stitch incluye filtros y acciones de resultado. Backend migra fuente de auditoría V6; pendiente crear/gestionar perfiles profesionales y sus bloques.

2026-09-25 | QUERY | disponibilidad de n8n/MCP | registro de herramientas y recursos MCP habilitados para esta ejecución | no se encontró herramienta n8n ni servidor/recurso MCP de n8n conectado. No se puede inspeccionar ni ejecutar la instancia del trainer desde este workspace; los JSON/workflows aún pueden versionarse, pero su ejecución MCP requiere acceso externo.
