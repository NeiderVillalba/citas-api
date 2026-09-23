# AGENTS.md — citas-api

## Stack y estructura verificados

- Java 21, Spring Boot 3.5.x y Maven.
- Arquitectura hexagonal por paquetes domain, application y adapter.
- REST/JSON con Spring MVC y validación Jakarta.
- Persistencia Spring Data JPA sobre MySQL 8.4; cambios de esquema en Flyway.
- Pruebas de integración con Spring Boot, MockMvc y H2.

## Inspección obligatoria

Antes de proponer cambios:

1. Leer el PRD, restricciones técnicas y requisitos de normalización del workspace.
2. Leer la Wiki global y la HU/CA/DoD aprobados relevantes.
3. Inspeccionar los paquetes, configuración, migraciones y pruebas actuales.
4. Identificar reglas, puertos, adaptadores, esquema, contrato REST y pruebas afectados.
5. Presentar un plan antes de editar.

## Responsabilidad

- Implementar reglas de negocio del PRD, casos de uso, persistencia, REST y pruebas backend.
- No editar citas-web. Si un cambio requiere adaptar un contrato REST, informar al orquestador el impacto y la evidencia requerida en ambos repositorios.

## Reglas arquitectónicas

- El dominio no depende de Spring, JPA ni HTTP.
- Los casos de uso viven en application.
- Los puertos representan dependencias hacia dentro y fuera.
- REST y persistencia son adaptadores.
- Los controladores traducen HTTP; no concentran negocio.
- No acoplar el backend a React ni Angular.
- Todo cambio de esquema requiere migración Flyway y justificación.

## Seguridad y datos

- Secretos únicamente por variables de entorno; no abrir ni registrar valores de .env.
- Nunca registrar tokens ni contraseñas; almacenar passwords con hash adaptativo.
- Aplicar validación server-side, autorización por rol y ownership.
- Usar solo datos sintéticos del laboratorio.

## Modo de trabajo

1. Localizar la HU aprobada y su DoD.
2. Identificar reglas y contratos afectados.
3. Proponer un plan acotado.
4. Implementar el mínimo incremento coherente.
5. Ejecutar pruebas de dominio, aplicación e integración relevantes.
6. Verificar la arquitectura y el DoD.
7. Resumir evidencia y declarar expresamente lo no verificado.

## Wiki

No mantener una Wiki propia. La Wiki global está en docs/wiki/llm-wiki/ y la mantiene el agente orquestador. Las especificaciones Scrum solo se escriben en docs/wiki/scrum/ mediante la Skill autorizada.
