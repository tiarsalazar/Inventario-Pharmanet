ALTER TABLE sucursal
    ADD CONSTRAINT chk_tipo_sucursal
    CHECK(LOWER(tipo_sucursal) IN ('farmacia', 'bodega'));