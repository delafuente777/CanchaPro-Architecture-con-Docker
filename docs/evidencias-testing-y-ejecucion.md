# Evidencias de Testing y Ejecucion - CanchaPro

## 1. Ejecucion de pruebas Maven

Se ejecutaron las pruebas completas desde el proyecto padre utilizando:

mvn clean test

Resultado obtenido:

BUILD SUCCESS
Total time: 05:48 min

Todos los modulos finalizaron correctamente:

eureka-server ................. SUCCESS
api-gateway ................... SUCCESS
auth-service .................. SUCCESS
ms-usuarios ................... SUCCESS
ms-canchas .................... SUCCESS
ms-disponibilidad ............. SUCCESS
ms-reservas ................... SUCCESS
ms-pagos ...................... SUCCESS
ms-notificaciones ............. SUCCESS
ms-calificaciones ............. SUCCESS
ms-reportes ................... SUCCESS
CanchaPro Parent .............. SUCCESS

## 2. Build final del proyecto

Se ejecuto:

mvn clean install

Resultado obtenido:

BUILD SUCCESS
Total time: 05:54 min

Con esto se generaron los archivos .jar finales de todos los microservicios.

## 3. Microservicios registrados en Eureka

URL de Eureka:

http://localhost:8761

Servicios registrados correctamente como UP:

API-GATEWAY
AUTH-SERVICE
MS-USUARIOS
MS-CANCHAS
MS-DISPONIBILIDAD
MS-PAGOS
MS-RESERVAS
MS-NOTIFICACIONES
MS-CALIFICACIONES
MS-REPORTES

## 4. Pruebas por API Gateway

URL base del Gateway:

http://localhost:8080

Endpoints probados:

GET /api/canchas
GET /api/usuarios
GET /api/disponibilidad
GET /api/pagos
GET /api/reservas
GET /api/notificaciones
GET /api/calificaciones
GET /api/reportes

Resultado general:

StatusCode: 200 OK

Esto confirma que el API Gateway enruta correctamente hacia los microservicios.

## 5. Validacion Swagger OpenAPI

Se valido la documentacion OpenAPI de los microservicios:

http://localhost:8081/v3/api-docs
http://localhost:8082/v3/api-docs
http://localhost:8083/v3/api-docs
http://localhost:8085/v3/api-docs
http://localhost:8086/v3/api-docs
http://localhost:8087/v3/api-docs
http://localhost:8088/v3/api-docs
http://localhost:8089/v3/api-docs
http://localhost:8090/v3/api-docs

Resultado general:

StatusCode: 200 OK

Esto confirma que los microservicios cuentan con documentacion Swagger OpenAPI disponible.

## 6. Pruebas unitarias y de controller

Se implementaron pruebas con JUnit 5, Mockito y MockMvc en los servicios principales:

auth-service
ms-usuarios
ms-canchas
ms-disponibilidad
ms-reservas
ms-pagos
ms-notificaciones
ms-calificaciones
ms-reportes

Tipos de pruebas implementadas:

ApplicationTests: validan la carga del contexto Spring Boot.
ServiceTest: validan reglas de negocio y manejo de excepciones.
ControllerTest: validan endpoints, codigos HTTP, validaciones y respuestas.

## 7. Distribucion sin Docker

Se genero una version ejecutable sin Docker en:

CanchaPro-Distribucion/sin-docker

Contenido principal:

arrancar-nativo.bat
apps/eureka-server.jar
apps/api-gateway.jar
apps/auth-service.jar
apps/ms-usuarios.jar
apps/ms-canchas.jar
apps/ms-disponibilidad.jar
apps/ms-reservas.jar
apps/ms-pagos.jar
apps/ms-notificaciones.jar
apps/ms-calificaciones.jar
apps/ms-reportes.jar

El sistema fue levantado correctamente desde los archivos .jar usando:

arrancar-nativo.bat

## 8. Evidencia de ejecucion

Se valido que la distribucion sin Docker ejecuta correctamente, que los servicios aparecen registrados en Eureka y que los endpoints principales responden por API Gateway con StatusCode: 200 OK.

Tambien se validaron los endpoints /v3/api-docs de los microservicios, confirmando la disponibilidad de Swagger OpenAPI.