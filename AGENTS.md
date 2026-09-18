# AGENTS.md — citas-api

## Estado de bootstrap

Este repositorio aún no contiene un proyecto Spring Boot inicializado. Estas instrucciones rigen el bootstrap sin inventar una estructura concreta. Después de inicializar el proyecto, inspeccionar `pom.xml`, el árbol de paquetes, la configuración, las migraciones y las pruebas; depurar este archivo para reflejar evidencia real.

## Inspección obligatoria

Antes de proponer cambios:

1. Leer el PRD, restricciones técnicas y requisitos de normalización del workspace.
2. Leer `docs/wiki/llm-wiki/wiki/index.md` y la HU, CA y DoD aprobados relevantes.
3. Inspeccionar el stack y la estructura reales del repositorio.
4. Identificar reglas, puertos, adaptadores, esquema, contrato REST y pruebas afectados.
5. Presentar un plan antes de editar.

## Responsabilidad

- Java 21, Spring Boot 3.5.x y Maven.
- Arquitectura hexagonal, REST/JSON y contratos backend.
- Spring Security con JWT access/refresh.
- MySQL 8.4, Spring Data JPA y Flyway.
- Reglas de negocio del PRD y pruebas backend.

No editar `citas-web` desde este repositorio. Cuando un cambio requiera adaptar un contrato REST, informarlo al orquestador con impacto, compatibilidad y evidencia necesaria en ambos repositorios.

## Reglas arquitectónicas

- El dominio no depende de Spring, JPA ni HTTP.
- Los casos de uso viven en aplicación.
- Los puertos expresan dependencias de entrada y salida.
- REST y persistencia son adaptadores.
- Los controladores traducen HTTP y no concentran lógica de negocio.
- No acoplar el backend a React ni Angular.
- Todo cambio de esquema requiere migración Flyway y justificación.

## Seguridad y datos

- Secretos únicamente por variables de entorno; no abrir ni registrar valores de `.env`.
- Nunca registrar tokens ni contraseñas.
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

No mantener una Wiki propia. La Wiki global está en `docs/wiki/llm-wiki/` y la mantiene el agente orquestador. Las especificaciones Scrum solo se escriben en `docs/wiki/scrum/` mediante la Skill autorizada.
