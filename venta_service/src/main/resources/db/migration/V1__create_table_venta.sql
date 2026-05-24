CREATE TABLE venta (
    venta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cod_venta BIGINT NOT NULL UNIQUE,
    sku VARCHAR(30) NOT NULL,
    cod_sucursal VARCHAR(30) NOT NULL,
    run_vendedor VARCHAR(10) NOT NULL,
    cantidad INT NOT NULL,
    fecha_venta DATE NOT NULL DEFAULT CURRENT_DATE
);