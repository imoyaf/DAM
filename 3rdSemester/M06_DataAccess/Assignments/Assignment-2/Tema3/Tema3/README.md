# Gestión de Incidencias con Hibernate

## Descripción

Este proyecto permite gestionar incidencias en un sistema de gestión de incidencias utilizando Hibernate como ORM. La aplicación interactúa con una base de datos que contiene dos tablas principales:

1. **Empleados**: Contiene los datos de los empleados que utilizan el sistema (nombre de usuario, contraseña, nombre completo, teléfono, etc.)
2. **Incidencias**: Contiene las incidencias generadas por los empleados, incluyendo detalles como fecha/hora, empleado origen, empleado destino, tipo (Urgente o Normal) y una descripción del detalle de la incidencia

## Enunciado del Ejercicio

### EJERCICIO 1: Base de datos con ORM

Deseamos construir una aplicación que permita acceder a los empleados y sus incidencias en un sistema de gestión de incidencias. La aplicación se debe construir utilizando el ORM Hibernate.

La base de datos que vamos a utilizar tiene las siguientes tablas:

* Tabla incidencias: Albergará las incidencias de los empleados. Contendrá para cada una la fecha-hora de generación, el empleado origen, el empleado destino, detalle de la incidencia y el tipo de la misma (U:Urgente o N:Normal)
* Tabla empleados: Contendrá los datos de inicio de sesión de los empleados al sistema: nombre de usuario y contraseña. Además, incluirá los datos del empleado, como el nombre completo y teléfono de contacto

Todas las tablas tendrán sus correspondientes identificadores en forma de clave primaria y claves foráneas que corresponden.

### Requisitos

1. Crear las clases POJO correspondientes para representar de forma adecuada los datos de las tablas explicadas (Incidencia, Empleado).

2. Backend para la gestión de empleados con las siguientes funcionalidades:

   * Insertar un empleado nuevo
   * Validar la entrada de un empleado (usuario y contraseña)
   * Modificar el perfil de un empleado
   * Cambiar la contraseña de un empleado
   * Eliminar un empleado

3. Backend para la gestión de incidencias con las siguientes funcionalidades:

   * Obtener una incidencia por su ID
   * Obtener todas las incidencias
   * Insertar una incidencia
   * Obtener las incidencias creadas por un empleado
   * Obtener las incidencias destinadas a un empleado

## Tecnologías Utilizadas
- **Java**: Versión 21
- **Hibernate ORM**: Versión 6.6.9
- **MySQL**: Base de datos utilizada para almacenar la información de empleados e incidencias
- **mysql-connector-j**: Versión 9.2.0 para la conexión con la base de datos MySQL
- **Maven**: Versión 4.0.0 para la gestión de dependencias y construcción del proyecto

## Funcionalidades
1. **Gestión de Empleados**:
    - Insertar un nuevo empleado.
    - Validar la entrada de un empleado (usuario y contraseña).
    - Modificar el perfil de un empleado existente.
    - Cambiar la contraseña de un empleado.
    - Eliminar un empleado.

2. **Gestión de Incidencias**:
    - Obtener una incidencia por su ID.
    - Obtener todas las incidencias.
    - Insertar una nueva incidencia.
    - Obtener las incidencias creadas por un empleado.
    - Obtener las incidencias destinadas a un empleado

## Instalación y Configuración

1. **Clonar el repositorio**:
   `git clone <URL_DEL_REPOSITORIO>`
2. **Configurar la base de datos**: Asegúrate de tener MySQL instalado y ejecutándose. Crea una base de datos con el nombre de tu elección.
   
