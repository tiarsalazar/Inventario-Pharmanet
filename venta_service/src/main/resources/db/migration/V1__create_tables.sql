CREATE TABLE venta (
    venta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cod_venta BIGINT NOT NULL UNIQUE,
    cod_sucursal VARCHAR(30) NOT NULL,
    run VARCHAR(10) NOT NULL,
    fecha_venta DATE NOT NULL DEFAULT CURRENT_DATE,
    monto_total INT NOT NULL
);

CREATE TABLE detalle_venta (
    detalle_venta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(10) NOT NULL,
    cantidad INT NOT NULL,
    cod_venta BIGINT NOT NULL,
    CONSTRAINT FK_DETALLE_VENTA_VENTA FOREIGN KEY (cod_venta)
    REFERENCES venta(cod_venta)
);