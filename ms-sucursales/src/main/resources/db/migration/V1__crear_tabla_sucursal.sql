CREATE TABLE sucursal (
    id_sucursal BIGINT AUTO_INCREMENT,
    cod_interno VARCHAR(10) NOT NULL UNIQUE,
    nombre_sucursal VARCHAR(30) UNIQUE,
    tipo_sucursal VARCHAR(20) NOT NULL,
    region VARCHAR(30) NOT NULL,
    comuna VARCHAR(40) NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'activo',

    PRIMARY KEY (id_sucursal)
);