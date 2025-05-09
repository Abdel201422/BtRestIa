# Aplicación de Consultas a Modelos de Inteligencia Artificial

Este proyecto es una Prueba de Concepto (PoC) desarrollada en Java con Spring Boot, que permite integrar y ejecutar modelos de Inteligencia Artificial (IA) de manera local utilizando Ollama. A través de una API REST, los usuarios pueden enviar preguntas en texto y obtener respuestas generadas por modelos de IA, con trazabilidad completa de cada interacción.

## Objetivos Principales

* *Integración de IA local*: Uso de Ollama para ejecutar modelos en el propio servidor sin depender de la nube.
* *Selección de modelo por consulta*: Posibilidad de definir qué modelo de IA se usará en cada petición.
* *Trazabilidad de interacciones*: Registro de usuario, pregunta, respuesta, modelo y fecha en la base de datos.
* *Historial de consultas*: Endpoints para que los usuarios recuperen sus preguntas y respuestas anteriores.
* *Gestión administrativa de modelos*: Operaciones para listar modelos disponibles y cambiar su estado (activo/inactivo).

## Estructura del Proyecto

* *Servicio de consulta (ConsultaServiceImpl)*: Lógica principal para procesar preguntas, generar respuestas mediante OllamaChatModel, guardar entidades en repositorios y mapear respuestas a DTOs.
* *Controladores REST*:
  * ConsultaController: Endpoints para enviar preguntas y recuperar preguntas o respuestas por token.
  * ModeloIaController: Endpoint para listar todos los modelos de IA registrados.
  * UsuarioController: Endpoints para obtener preguntas y respuestas asociadas a un usuario.
* *Configuración de Ollama (OllamaConfig)*: Bean para crear instancias de OllamaApi y configurar el builder de modelos de chat.
* *Entidades y repositorios*:
  * Pregunta, Respuesta, Consulta, ModeloIA, Usuario.
  * Repositorios Spring Data JPA para persistencia de cada entidad.
* *DTOs y excepciones*:
  * Clases DTO para solicitudes (PreguntaRequestDto) y respuestas (PreguntaDto, RespuestaDto).
  * Excepciones personalizadas (ModeloAiNotFoundException, PreguntaNotFoundException, RespuestaNotFoundException) para manejo de errores.

## Requisitos del Sistema

1. *Java 17+* y *Spring Boot*.
2. *Base de datos*: PostgreSQL, MySQL o H2 para desarrollo.
3. *Ollama* instalado y accesible en http://localhost:11434.
4. *Dependencias* gestionadas con Maven o Gradle.

## Flujo de Operaciones

1. El cliente envía una petición POST con el token de usuario, texto de la pregunta y nombre de modelo.
2. El servicio valida el token de usuario y verifica que el modelo esté activo.
3. Se genera la respuesta mediante OllamaChatModel y se extrae el texto del resultado.
4. Se guardan en la base de datos la pregunta, la respuesta y la consulta asociada.
5. Se devuelve al cliente un DTO con el token de respuesta, el texto generado y la fecha.
6. El cliente puede recuperar preguntas o respuestas específicas mediante token o consultar su historial completo.

## Endpoints Principales

* *POST /api/consulta/preguntar*

  * Envía una pregunta y recibe la respuesta generada.
* *GET /api/consulta/pregunta/{token}*

  * Obtiene la pregunta original asociada al token.
* *GET /api/consulta/respuesta/{token}*

  * Recupera la respuesta generada asociada al token.
* *GET /api/modelo\_ia/modelo*

  * Lista todos los modelos de IA y su estado.
* *GET /api/usuario/{token}/preguntas*

  * Recupera todas las preguntas realizadas por el usuario.
* *GET /api/usuario/{token}/respuestas*

  * Recupera todas las respuestas generadas para el usuario.

## Requisitos Funcionales

* Recepción de preguntas en texto y generación de respuestas por IA.
* Selección dinámica de modelo por petición.
* Persistencia de cada interacción con detalles completos.
* Consulta de historial y recuperación por token.

## Requisitos No Funcionales

* *Rendimiento*: Respuestas en menos de 30 segundos mediante configuración de tiempo de espera.
* *Escalabilidad*: Arquitectura desacoplada que permite añadir nuevos modelos sin cambiar la lógica central.

## Exclusiones del Alcance

* No incluye interfaz de usuario (solo API REST).
* No se entrenan modelos personalizados; solo se usan modelos existentes.
* No hay integración con sistemas de autenticación avanzada ni facturación.

## Cómo Iniciar el Proyecto

1. Clonar el repositorio y abrir en el IDE.
2. Configurar las propiedades de conexión a la base de datos y la URL de Ollama.
3. Ejecutar la aplicación Spring Boot.
4. Probar los endpoints con herramientas como Postman o curl.

## Contribución

Las contribuciones son bienvenidas mediante pull requests. Por favor, sigue el flujo Git estándar: crear ramas de funcionalidad, hacer commits descriptivos y abrir PRs.

## Licencia

Proyecto bajo licencia MIT.
