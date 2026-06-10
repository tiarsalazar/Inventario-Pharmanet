CREATE TABLE producto (
    producto_id BIGINT NOT NULL AUTO_INCREMENT,
    sku VARCHAR(10) NOT NULL UNIQUE,
    nombre_comercial VARCHAR(100) NOT NULL,
    principio_activo VARCHAR(80) NOT NULL,
    laboratorio VARCHAR(100),
    precio_venta DECIMAL(10, 2) NOT NULL,
    receta VARCHAR(30) NOT NULL,
    presentacion VARCHAR(100),
    concentracion VARCHAR(30) NOT NULL,

    PRIMARY KEY (producto_id)
);