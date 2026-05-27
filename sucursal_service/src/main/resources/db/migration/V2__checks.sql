ALTER TABLE sucursal
    ADD CONSTRAINT ck_tipo_sucursal
    CHECK(UPPER(tipo_sucursal) IN ('FARMACIA', 'BODEGA'));
