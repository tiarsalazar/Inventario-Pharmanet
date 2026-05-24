CREATE TABLE venta (
    venta_id BIGINT AUTO INCREMENT PRIMARY KEY,
    sku VARCHAR(30) NOT NULL,
    cod_sucursal VARCHAR(30) NOT NULL,
    run_vendedor VARCHAR(10) NOT NULL,
    cantidad INTGIT NOT NULL,
    fecha_venta DATE DEFAULT (CURRENT_DATE) NOT NULL
);