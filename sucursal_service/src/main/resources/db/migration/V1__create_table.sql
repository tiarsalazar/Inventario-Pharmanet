CREATE TABLE sucursal (
    sucursal_id BIGINT AUTO_INCREMENT,
    cod_sucursal VARCHAR(8),
    nombre_sucursal VARCHAR(30) UNIQUE,
    cod_region INT NOT NULL,
    cod_comuna INT NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'activo',

    PRIMARY KEY (sucursal_id)
);