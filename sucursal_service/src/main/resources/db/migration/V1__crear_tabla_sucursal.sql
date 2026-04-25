CREATE TABLE sucursal (
    id_sucursal BIGINT AUTO_INCREMENT,
    cod_interno CHAR(10) NOT NULL UNIQUE,
    nombre_sucursal VARCHAR(30) UNIQUE,
    tipo_sucursal VARCHAR(20) NOT NULL,
    region VARCHAR(20) NOT NULL,
    comuna VARCHAR(25) NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    estado VARCHAR(15) NOT NULL DEFAULT 'activo',

    CONSTRAINT pk_sucursal PRIMARY KEY (id_sucursal)
);