# Proyecto de Gestión de Empleados con JDBC

## Descripción

Este proyecto implementa una aplicación de consola para gestionar los empleados de una empresa utilizando **JDBC** (Java Database Connectivity). El sistema permite realizar operaciones básicas de gestión de empleados en una base de datos MySQL.

## Enunciado del Ejercicio

### **EJERCICIO 1: Base de datos con JDBC**

Se desea construir una aplicación que permita realizar la gestión de los empleados de una empresa. La aplicación debe construirse utilizando JDBC.

La base de datos que se utilizará tiene la siguiente tabla:

### Tabla `empleados`

Contendrá los datos de inicio de sesión de los empleados al sistema: nombre de usuario y contraseña. Además, incluirá los datos del empleado como el nombre completo, teléfono de contacto, entre otros. La tabla tendrá su correspondiente clave primaria.

### Requerimientos del Sistema

1. Crear la clase POJO correspondiente para representar de forma adecuada los datos de la tabla `empleados`.
2. Crear un backend que permita la gestión de empleados mediante un menú interactivo con las siguientes opciones:
    - **Insertar un empleado nuevo en la base de datos.**
    - **Modificar el perfil de un empleado existente.**
    - **Cambiar la contraseña de un empleado existente.**
    - **Eliminar un empleado.**

## Tecnologías Utilizadas

- **Java 21**
- **JDBC (Java Database Connectivity)**
- **MySQL 8.0** (base de datos)
- **MySQL Connector/J 9.2.0** (driver JDBC)
- **Maven 4.0.0** (gestión de dependencias)

## Estructura del Proyecto

### Clases Principales

1. **Empleado**: Representa un empleado en el sistema con atributos como `id`, `usuario`, `contrasena`, `nombreCompleto`, `telefono`, `correoElectronico` y `puesto`.

2. **EmpleadoController**: Clase encargada de la lógica de acceso a la base de datos. Proporciona métodos para insertar, modificar, cambiar contraseñas y eliminar empleados, utilizando JDBC para interactuar con la base de datos MySQL.

3. **EmpleadoView**: Interfaz de consola que permite interactuar con el usuario, mostrando un menú para las diferentes opciones de gestión de empleados.

### Dependencias en `pom.xml`

Este proyecto utiliza Maven para gestionar las dependencias del proyecto. En el archivo `pom.xml` se incluye el conector MySQL necesario para que la aplicación se comunique con la base de datos.

```xml
<dependencies>
    <!-- MySQL JDBC Driver -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.2.0</version>
    </dependency>
</dependencies>

## Estructura de la Base de Datos
```

La base de datos contiene una tabla empleados con la siguiente estructura:

```
CREATE DATABASE IF NOT EXISTS empresa;

USE empresa;

CREATE TABLE IF NOT EXISTS empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    puesto VARCHAR(100)
);
```

## Instalación y Configuración

1. **Clonar el repositorio**:
   `git clone https://github.com/imoyaf/DAM/tree/main/3rdSemester/M06_DataAccess/Assignments/Assignment-2/Tema2`
## Uso de la Aplicación

2. Conexión a la Base de Datos: La aplicación utiliza JDBC para conectarse a una base de datos MySQL. Asegúrate de que la base de datos empresa esté creada y configurada correctamente
3. Ejecución del Proyecto: Para ejecutar el proyecto, abre una terminal y navega al directorio del proyecto. Luego ejecuta:<br>
```
mvn clean install
mvn exec:java
```
4. Menú de Opciones: Al iniciar la aplicación, verás un menú con las siguientes opciones:

    * Ver listado de empleados
    * Insertar un nuevo empleado
    * Modificar perfil de un empleado
    * Cambiar contraseña de un empleado
    * Eliminar un empleado
    * Salir

Elige la opción correspondiente para realizar una acción en la base de datos

