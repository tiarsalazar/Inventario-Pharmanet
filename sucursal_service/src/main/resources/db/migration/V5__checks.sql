ALTER TABLE sucursal
    DROP CHECK chk_tipo_sucursal;

ALTER TABLE sucursal
    DROP CHECK chk_min_cod_interno;

ALTER TABLE sucursal
    ADD CONSTRAINT ck_tipo_sucursal
    CHECK(UPPER(tipo_sucursal) IN ('FARMACIA', 'BODEGA'));

ALTER TABLE sucursal
    ADD CONSTRAINT ck_longitud_min_cod_sucursal
    CHECK(LENGTH(cod_interno) >= 6);

ALTER TABLE sucursal
    ADD CONSTRAINT ck_codigo_sucursal_inicio
    CHECK (SUBSTRING(cod_sucursal, 1, 2) = 'SU');

