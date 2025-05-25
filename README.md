# personapp-hexa-spring-boot

Este proyecto implementa una arquitectura hexagonal con múltiples adaptadores (REST y CLI), persistencia en MariaDB y MongoDB, y despliegue mediante Docker.

## 🚀 Ejecución del Proyecto

Para ejecutar la aplicación completa en **Docker Desktop para Windows**, sigue los pasos:

### 🔧 1. Levantar todos los servicios (REST, DBs, etc.)

Desde la raíz del proyecto:

```bash
docker-compose up --build
```

Esto construye y lanza todos los contenedores, incluidos:

* API REST (`rest-input-adapter`)
* MariaDB y MongoDB
* Swagger UI en: [http://localhost:3000/swagger-ui.html](http://localhost:3000/swagger-ui.html)

### ⚠️ Nota importante sobre Windows + Docker

> Si el backend REST (`rest-input-adapter`) **falla al iniciar por primera vez** (carga de datos), es probable que se deba a cómo Docker Desktop para Windows maneja los volúmenes y el timing de los containers.
>
> En ese caso, puedes solucionarlo **renombrando temporalmente los archivos `entrypoint` de los volúmenes**, por eso están con nombres como `ppersona.sql`.

*Sí, es una solución poco ortodoxa, pero necesaria para evitar conflictos de sincronización en volúmenes en Docker para Windows.*

---

### 👤 2. Ejecutar el CLI (terminal interactiva)

El servicio CLI requiere una terminal interactiva, por lo que **no se ejecuta automáticamente**.

Para correrlo manualmente:

```bash
docker-compose run --rm -it cli-service
```

Esto lanza el CLI con entrada interactiva, desde donde se puede:

* Consultar datos
* Crear, editar y eliminar personas, teléfonos, profesiones y estudios
* Probar la lógica del dominio directamente

---

## 🌐 Servicios

* **REST API**: `http://localhost:3000/api/v1`
* **Swagger UI**: `http://localhost:3000/swagger-ui.html`

---

## 🛠️ Stack

* JDK 11
* Spring Boot
* MongoDB & MariaDB
* Docker & Docker Compose
* Arquitectura hexagonal (puertos y adaptadores)
* REST y CLI
