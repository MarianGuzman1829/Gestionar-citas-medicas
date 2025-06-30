# Sistema de Gestión de Citas Médicas (Backend)

Este repositorio contiene el backend de un sistema para la gestión de citas médicas y acceso a historiales clínicos, desarrollado con Spring Boot.

## Estado del Despliegue

¡El backend está desplegado y funcionando en Railway!

## Acceso a la Aplicación Desplegada

Puedes acceder a los diferentes componentes de la aplicación a través de las siguientes URLs públicas:

* **URL Base de la API:**
    `https://gestionar-citas-medicas-production.up.railway.app`

* **Documentación Interactiva de la API (Swagger UI):**
    Explora todos los endpoints disponibles y pruébalos directamente desde tu navegador.
    `https://gestionar-citas-medicas-production.up.railway.app/documentacion/swagger-ui.html`

* **Ejemplo de Reporte en PDF (Reporte de Pacientes):**
    Puedes generar y ver un reporte en formato PDF directamente en tu navegador.
    `https://gestionar-citas-medicas-production.up.railway.app/api/reportes/pacientes/pdf`

    *(Nota: Hay otros reportes disponibles, consulta Swagger UI para ver todos los endpoints de `ReporteController`.)*

## Conexión a la Base de Datos

La conexión a la base de datos se maneja internamente dentro del entorno de despliegue de Railway mediante **variables de entorno** (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`). Estas son configuradas directamente en nuestro proyecto de Railway 

La aplicación utiliza estas variables para establecer la conexión con YugabyteDB de forma segura.

