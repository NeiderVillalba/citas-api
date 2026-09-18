# Síntesis del proyecto

## HECHO

El proyecto es un laboratorio académico de agendamiento de citas con datos sintéticos. Tiene dos repositorios independientes: un backend Java/Spring Boot y un frontend TypeScript que lo consume directamente por REST.

La aplicación debe cubrir registro, autenticación JWT con refresh, perfil y afiliación, catálogos, profesionales, disponibilidad, citas generales y especializadas, cancelación, reprogramación, agenda profesional, cierre de atención, bandeja administrativa y auditoría de estados.

## Estado actual

- `citas-api` no contiene implementación de negocio.
- `citas-web` no contiene una aplicación importada.
- Ambos repositorios usan `develop` como rama de trabajo.
- El contrato REST aún no está diseñado.
- El framework frontend aún no está decidido.

## Enlaces

- [Dominio](domain.md)
- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
