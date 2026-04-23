CREATE TABLE usuario (
    usuario_id BIGINT AUTO_INCREMENT,
    numrun INT NOT NULL,
    dvrun CHAR(1) NOT NULL,
    appaterno VARCHAR(20) NOT NULL,
    apmaterno VARCHAR(20) NOT NULL,
    nombres VARCHAR(30) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(100),
    region_id  VARCHAR(30) NOT NULL,
    telefono VARCHAR(12) NOT NULL,
    correo VARCHAR(50) CONSTRAINT USUARIO_CORREO_UN UNIQUE NOT NULL,
    profesion VARCHAR(25) NOT NULL,
    sucursal_id BIGINT NOT NULL,

    CONSTRAINT USUARIO_PK PRIMARY KEY (usuario_id)
);