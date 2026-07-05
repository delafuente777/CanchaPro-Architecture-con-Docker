# SISTEMA DE MICROSERVICIOS MULTIMODULO - ENTREGA FINAL

Proyecto desarrollado para la asignatura **Desarrollo FullStack I - DSY1103**.

Sistema: **CanchaPro Architecture**
Integrantes: **Caden Alexander Nieto Gonzalez** y **Sebastian De La Fuente**

---

## NOTA SOBRE CODIFICACION DEL DOCUMENTO

Este README fue redactado intencionalmente sin tildes, sin letra ene con virgulilla y sin caracteres especiales no ASCII, para evitar problemas de visualizacion, encoding o caracteres corruptos en Windows, PowerShell, GitHub y archivos Markdown.

---

## COMPONENTES DE DISTRIBUCION Y DEFENSA TECNICA

Utilice los siguientes enlaces externos para descargar la version lista para ejecucion nativa y visualizar la defensa tecnica del proyecto.

| Componente                                                 | Descripcion                                                                                                                                                                                                             | Enlace de Descarga / Visualizacion                                                                              |
| :--------------------------------------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------- |
| **Version Sin Docker** <br>*(Arranque Nativo)*             | Archivo `.zip` que contiene la carpeta `apps/` con los `.jar` compilados y el script `arrancar-nativo.bat`, ordenado por fases: **1. Eureka Server -> 2. Microservicios de negocio -> 3. API Gateway**.                 | [Descargar ZIP Nativo aqui](https://drive.google.com/file/d/1qMaLjZfhQrqIoAUqUyzTTPQL-yb394uu/view?usp=sharing) |
| **Version Con Docker** <br>*(Avance Examen Transversal)*   | Pendiente para el Examen Transversal. Para la Evaluacion Parcial 3 se presenta la puesta en marcha nativa sin Docker, segun lo indicado por el docente.                                                                 | Pendiente para Examen Transversal                                                                               |
| **Video de Defensa Tecnica** <br>*(Evaluacion Individual)* | Enlace directo al video explicativo donde se evidencia el funcionamiento del sistema, testing, uso de Eureka, API Gateway y el aporte tecnico individual. **Duracion ideal: 15 minutos. Maximo permitido: 18 minutos.** | [Ver Video Explicativo aqui](https://youtu.be/yaZx5VRmGps)

---

## SUBTITULOS O TRANSCRIPCION DEL VIDEO

La transcripcion del video explicativo se encuentra en el archivo:

```text
subtitulos-video.txt
```

Este archivo contiene el resumen del video de defensa tecnica, incluyendo explicacion del sistema, puesta en marcha, pruebas unitarias, endpoints principales y aporte individual.

---

## REPOSITORIO DEL PROYECTO

Repositorio GitHub:

```text
https://github.com/Cadennnnnn/canchapro-architecture
```

---

## ESTADO DE LA ENTREGA

| Elemento                            | Estado                            |
| :---------------------------------- | :-------------------------------- |
| Proyecto Maven multi-modulo         | Completado                        |
| Microservicios Spring Boot          | Completado                        |
| Eureka Server                       | Completado                        |
| API Gateway                         | Completado                        |
| Puesta en marcha sin Docker         | Completada                        |
| Pruebas unitarias JUnit 5 / Mockito | Completadas                       |
| Swagger / OpenAPI                   | Completado                        |
| ZIP nativo externo en Google Drive  | Completado                        |
| Video defensa tecnica               | Pendiente                         |
| Docker                              | Pendiente para Examen Transversal |

---

# CanchaPro Architecture

CanchaPro es un sistema de gestion de reservas de canchas deportivas desarrollado con arquitectura de microservicios usando Spring Boot, Spring Cloud, Eureka Server, API Gateway, bases de datos independientes y comunicacion REST entre servicios.

El objetivo del sistema es permitir la gestion de usuarios, canchas, disponibilidad, reservas, pagos, notificaciones, calificaciones y reportes dentro de un ecosistema distribuido.

---

## 1. Integrantes

| Nombre                         | Rol                                                                                      |
| :----------------------------- | :--------------------------------------------------------------------------------------- |
| Caden Alexander Nieto Gonzalez | Desarrollo de microservicios, testing, documentacion, puesta en marcha y defensa tecnica |
| Sebastian De La Fuente         | Desarrollo de microservicios, apoyo tecnico y documentacion                              |

---

## 2. Objetivo del proyecto

El objetivo del proyecto es implementar un sistema distribuido para gestionar reservas de canchas deportivas.

Flujo principal del sistema:

```text
Usuario -> Cancha -> Disponibilidad -> Reserva -> Pago -> Notificacion -> Calificacion -> Reportes
```

El proyecto fue construido aplicando arquitectura de microservicios, separacion por responsabilidades, comunicacion REST, pruebas unitarias y documentacion tecnica para facilitar su revision, ejecucion y defensa.

---

## 3. Arquitectura general

El sistema utiliza una arquitectura basada en microservicios con los siguientes componentes principales:

* **Eureka Server**: servidor de descubrimiento donde se registran los microservicios.
* **API Gateway**: punto central de entrada para consumir los endpoints del sistema.
* **Microservicios de negocio**: servicios independientes encargados de resolver funcionalidades especificas.
* **Bases de datos independientes**: cada microservicio posee su propia configuracion de persistencia.
* **Comunicacion REST**: los servicios se comunican mediante endpoints HTTP.
* **Documentacion Swagger/OpenAPI**: permite visualizar y probar los endpoints principales.
* **Testing unitario**: pruebas implementadas con JUnit 5, Mockito y MockMvc.

---

## 4. Microservicios y puertos

| Servicio          | Puerto | Descripcion                                 |
| :---------------- | :----: | :------------------------------------------ |
| eureka-server     |  8761  | Servidor de descubrimiento de servicios     |
| api-gateway       |  8080  | Puerta de entrada principal del sistema     |
| auth-service      |  8081  | Autenticacion, login, registro, JWT y roles |
| ms-usuarios       |  8082  | Gestion de usuarios                         |
| ms-canchas        |  8083  | Gestion de canchas deportivas               |
| ms-disponibilidad |  8085  | Gestion de disponibilidad horaria           |
| ms-pagos          |  8086  | Gestion de pagos                            |
| ms-reservas       |  8087  | Gestion de reservas                         |
| ms-notificaciones |  8088  | Gestion de notificaciones                   |
| ms-calificaciones |  8089  | Gestion de calificaciones                   |
| ms-reportes       |  8090  | Gestion de reportes                         |

---

## 5. Tecnologias utilizadas

* Java 21
* Spring Boot
* Spring Cloud
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* BCrypt
* MySQL
* Maven Multi-Modulo
* Eureka Server
* API Gateway
* OpenAPI / Swagger
* JUnit 5
* Mockito
* MockMvc
* Git y GitHub

---

## 6. Requisitos previos para ejecucion nativa

Para ejecutar el sistema sin Docker se requiere:

* Java 21 instalado.
* Maven instalado o configurado.
* MySQL funcionando localmente.
* Base de datos configurada segun los archivos `application.yml` de cada microservicio.
* Puertos disponibles desde el 8761 y desde el 8080 al 8090.
* Sistema operativo Windows para ejecutar el archivo `arrancar-nativo.bat`.

---

## 7. Puesta en marcha sin Docker

La version nativa se encuentra disponible en Google Drive como archivo `.zip`.

El archivo contiene:

```text
apps/
  eureka-server.jar
  api-gateway.jar
  auth-service.jar
  ms-usuarios.jar
  ms-canchas.jar
  ms-disponibilidad.jar
  ms-pagos.jar
  ms-reservas.jar
  ms-notificaciones.jar
  ms-calificaciones.jar
  ms-reportes.jar

arrancar-nativo.bat
```

El archivo `arrancar-nativo.bat` levanta los componentes en el siguiente orden:

```text
1. Eureka Server
2. Microservicios de negocio
3. API Gateway
```

Esto permite validar que el ecosistema completo funcione correctamente.

---

## 8. Ejecucion desde codigo fuente

Desde la raiz del proyecto se puede compilar todo el sistema con:

```bash
mvn clean install
```

Para ejecutar pruebas unitarias:

```bash
mvn clean test
```

Para ejecutar un microservicio especifico desde su carpeta:

```bash
mvn spring-boot:run
```

---

## 9. API Gateway

El API Gateway funciona como punto central de acceso al sistema.

URL principal:

```text
http://localhost:8080
```

Rutas principales por Gateway:

| Servicio          | Ruta Gateway             |
| :---------------- | :----------------------- |
| auth-service      | `/api/auth/**`           |
| ms-usuarios       | `/api/usuarios/**`       |
| ms-canchas        | `/api/canchas/**`        |
| ms-disponibilidad | `/api/disponibilidad/**` |
| ms-pagos          | `/api/pagos/**`          |
| ms-reservas       | `/api/reservas/**`       |
| ms-notificaciones | `/api/notificaciones/**` |
| ms-calificaciones | `/api/calificaciones/**` |
| ms-reportes       | `/api/reportes/**`       |

---

## 10. Eureka Server

El servidor Eureka permite visualizar los servicios registrados.

URL:

```text
http://localhost:8761
```

Servicios esperados registrados en Eureka:

```text
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
```

---

## 11. Swagger / OpenAPI

Cada microservicio cuenta con documentacion OpenAPI disponible mediante la ruta:

```text
/v3/api-docs
```

URLs principales:

| Servicio          | URL                                 |
| :---------------- | :---------------------------------- |
| auth-service      | `http://localhost:8081/v3/api-docs` |
| ms-usuarios       | `http://localhost:8082/v3/api-docs` |
| ms-canchas        | `http://localhost:8083/v3/api-docs` |
| ms-disponibilidad | `http://localhost:8085/v3/api-docs` |
| ms-pagos          | `http://localhost:8086/v3/api-docs` |
| ms-reservas       | `http://localhost:8087/v3/api-docs` |
| ms-notificaciones | `http://localhost:8088/v3/api-docs` |
| ms-calificaciones | `http://localhost:8089/v3/api-docs` |
| ms-reportes       | `http://localhost:8090/v3/api-docs` |

---

## 12. Seguridad

El sistema incorpora seguridad mediante:

* Registro de usuarios.
* Inicio de sesion.
* Encriptacion de contrasenas con BCrypt.
* Generacion de token JWT.
* Manejo de roles.
* Configuracion de seguridad con Spring Security.

---

## 13. Pruebas unitarias

El proyecto incluye pruebas unitarias y de controlador con:

* JUnit 5
* Mockito
* MockMvc

Las pruebas fueron implementadas para validar servicios, controladores y comportamiento basico de los microservicios.

Comando de ejecucion:

```bash
mvn clean test
```

Tambien se valido la compilacion completa del proyecto con:

```bash
mvn clean install
```

Resultado esperado:

```text
BUILD SUCCESS
```

---

## 14. Evidencias de ejecucion

Las evidencias de testing, ejecucion y validacion del sistema se encuentran en:

```text
docs/evidencias-testing-y-ejecucion.md
```

En ese documento se registran evidencias de:

* Ejecucion de pruebas unitarias.
* Compilacion del proyecto padre.
* Registro de servicios en Eureka.
* Validacion del API Gateway.
* Validacion de endpoints principales.
* Validacion de Swagger/OpenAPI.
* Puesta en marcha sin Docker.

---

## 15. Estructura general del proyecto

```text
CanchaPro(FINAL)/
  pom.xml
  README.md
  .gitignore
  eureka-server/
  api-gateway/
  auth-service/
  ms-usuarios/
  ms-canchas/
  ms-disponibilidad/
  ms-reservas/
  ms-pagos/
  ms-notificaciones/
  ms-calificaciones/
  ms-reportes/
  docs/
```

---

## 16. Buenas practicas aplicadas

El repositorio contiene unicamente codigo fuente, configuracion y documentacion.

No se suben a GitHub:

```text
.jar
.zip
target/
.bat
.sh
archivos temporales
datos locales de bases de datos
```

Estos elementos se distribuyen externamente mediante Google Drive para mantener limpio el repositorio y respetar buenas practicas de control de versiones.

---

## 17. Estado final del proyecto

El sistema se encuentra preparado para revision docente en modalidad de puesta en marcha sin Docker.

Estado general:

```text
Proyecto Maven Multi-Modulo: Completado
Microservicios: Completados
Eureka Server: Completado
API Gateway: Completado
Testing unitario: Completado
Swagger/OpenAPI: Completado
ZIP sin Docker: Disponible en Google Drive
Docker: Pendiente para Examen Transversal
Video defensa tecnica: Pendiente de carga
```

---

## 18. Notas para la defensa tecnica

Durante la defensa tecnica se puede explicar:

* Arquitectura general de microservicios.
* Funcionamiento de Eureka Server.
* Funcionamiento del API Gateway.
* Separacion por capas Controller, Service y Repository.
* Uso de DTOs y validaciones.
* Seguridad con JWT, BCrypt y roles.
* Pruebas unitarias con JUnit 5, Mockito y MockMvc.
* Ejecucion nativa mediante archivo `.bat`.
* Evidencias de endpoints funcionando.
* Aporte tecnico individual en desarrollo, testing, documentacion y puesta en marcha.

---

## 19. Entrega en AVA

Cada integrante debe subir individualmente el enlace del repositorio GitHub en su propio acceso del AVA.

Link del repositorio:

```text
https://github.com/Cadennnnnn/canchapro-architecture
```

Esto es obligatorio para que la entrega quede registrada y pueda ser revisada por el docente.
