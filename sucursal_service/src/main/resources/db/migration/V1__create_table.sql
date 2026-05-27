CREATE TABLE sucursal (
    sucursal_id BIGINT AUTO_INCREMENT,
    nombre_sucursal VARCHAR(30) UNIQUE,
    tipo_sucursal VARCHAR(20) NOT NULL,
    region INT NOT NULL,
    comuna INT NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'activo',

    PRIMARY KEY (sucursal_id)
);