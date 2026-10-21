SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS bd_aulas
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_aulas;

CREATE TABLE IF NOT EXISTS usuariossistema (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Apellido VARCHAR(255) NOT NULL,
    Usuario VARCHAR(255) NOT NULL,
    Clave VARCHAR(255) NOT NULL,
    Correo VARCHAR(255) NOT NULL UNIQUE,
    Roll VARCHAR(255) NOT NULL,
    Estado VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tipos_aula (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS estados_aula (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS plantas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS aulas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    edificio VARCHAR(100) NOT NULL,
    planta_id INT NOT NULL,
    capacidad INT NOT NULL,
    tipo_aula_id INT NOT NULL,
    responsable VARCHAR(150) NOT NULL,
    estado_id INT NOT NULL,
    UNIQUE (nombre, edificio),
    FOREIGN KEY (planta_id) REFERENCES plantas(id),
    FOREIGN KEY (tipo_aula_id) REFERENCES tipos_aula(id),
    FOREIGN KEY (estado_id) REFERENCES estados_aula(id)
);

-- IGNORE permite volver a importar sin duplicar los datos de los catálogos.
INSERT IGNORE INTO tipos_aula (nombre) VALUES
    ('Normal'), ('Centro de cómputo'), ('Laboratorio'), ('Especial');

INSERT IGNORE INTO estados_aula (nombre) VALUES
    ('Asignada'), ('Asignada temporalmente'), ('En mantenimiento');

INSERT IGNORE INTO plantas (numero) VALUES (1), (2), (3), (4), (5);

INSERT IGNORE INTO usuariossistema (Nombre, Apellido, Usuario, Clave, Correo, Roll, Estado)
VALUES ('Administrador', 'UFG', 'admin', MD5('1234'), 'admin@ufg.edu', 'ADMIN', 'ACTIVO');
