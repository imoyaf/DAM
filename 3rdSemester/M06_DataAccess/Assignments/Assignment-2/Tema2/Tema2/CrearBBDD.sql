CREATE DATABASE IF NOT EXISTS empleados_AAD;

USE empleados_AAD;

CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    puesto VARCHAR(100)
);

INSERT INTO empleados (usuario, contrasena, nombre_completo, telefono, correo_electronico, puesto)
VALUES ('joanpetit', '123456', 'Joan Petit', '666555444', 'joan.petit@email.com', 'Administrativo');
INSERT INTO empleados (usuario, contrasena, nombre_completo, telefono, correo_electronico, puesto)
VALUES ('joanapetita', '123456', 'Joana Petita', '666333222', 'joana.petita@email.com', 'Programadora');
