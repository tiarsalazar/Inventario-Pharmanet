ALTER TABLE empleado
    ADD CONSTRAINT ck_run_empleado
    CHECK(REGEXP_LIKE(run, '^[0-9]{7,8}-[0-9Kk]$'));