ALTER TABLE usuario
    ADD CONSTRAINT ck_run_usuario
    CHECK(REGEXP_LIKE(run, '^[0-9]{7,8}-[0-9Kk]$'));