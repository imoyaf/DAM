CREATE DATABASE IF NOT EXISTS empleados_AAD;

USE empleados_AAD;

CREATE TABLE IF NOT EXISTS empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    puesto VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS incidencias (
    id INT(11) AUTO_INCREMENT PRIMARY KEY,
    fecha_hora TIMESTAMP,
    empleado_origen_id INT(11),
    empleado_destino_id INT(11),
    detalle TEXT NOT NULL,
    tipo VARCHAR(1) NOT NULL,
    CONSTRAINT tipo_check CHECK (tipo IN ('N', 'U')),
    FOREIGN KEY (empleado_origen_id) REFERENCES empleados(id),
    FOREIGN KEY (empleado_destino_id) REFERENCES empleados(id)
);

INSERT INTO empleados (usuario, contrasena, nombre_completo, telefono, correo_electronico, puesto)
VALUES ('joanpetit', '123456', 'Joan Petit', '666555444', 'joan.petit@email.com', 'Administrativo');
INSERT INTO empleados (usuario, contrasena, nombre_completo, telefono, correo_electronico, puesto)
VALUES ('joanapetita', '123456', 'Joana Petita', '666333222', 'joana.petita@email.com', 'Programadora');

INSERT INTO incidencias (fecha_hora, empleado_origen_id, empleado_destino_id, detalle, tipo)
VALUES (NOW(), 1, 2, 'Problema con el acceso al sistema', 'N');
INSERT INTO incidencias (fecha_hora, empleado_origen_id, empleado_destino_id, detalle, tipo)
VALUES (NOW(), 2, 1, 'Fallo en la conexión a la red', 'U');
