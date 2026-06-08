CREATE TABLE usuario(
    usuario_id NUMBER GENERATED ALWAYS AS IDENTITY(START WITH 1 INCREMENT BY 1),
    run VARCHAR2(10) NOT NULL CONSTRAINT un_run_usuario UNIQUE,
    nombre_completo VARCHAR2(80) NOT NULL,
    correo_institucional VARCHAR2(30) NOT NULL CONSTRAINT un_correo_usuario UNIQUE,
    telefono VARCHAR2(12) NOT NULL,
    cod_sucursal VARCHAR2(30) NOT NULL,
    profesion VARCHAR2(30) NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (usuario_id)
);