ALTER TABLE sucursal
    ADD CONSTRAINT chk_tipo_sucursal
    CHECK(LOWER(tipo_sucursal) IN ('farmacia', 'bodega'));

ALTER TABLE sucursal
    ADD CONSTRAINT chk_min_cod_interno
    CHECK(LENGTH(cod_interno) >= 6);

ALTER TABLE sucursal
    ADD CONSTRAINT chk_min_nombre_sucursal
    CHECK(nombre_sucursal IS NULL OR LENGTH(nombre_sucursal) >= 5);