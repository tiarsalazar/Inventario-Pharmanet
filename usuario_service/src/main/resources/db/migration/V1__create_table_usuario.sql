CREATE TABLE usuario (
    usuario_id BIGINT AUTO_INCREMENT,
    run VARCHAR(10) NOT NULL,
    nombre_completo VARCHAR(80) NOT NULL,
    correo_institucional VARCHAR(30) NOT NULL,
    telefono VARCHAR(12) NOT NULL,
    cod_sucursal VARCHAR(30) NOT NULL,
    profesion VARCHAR(30) NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (usuario_id),
    CONSTRAINT un_run_usuario UNIQUE (run),
    CONSTRAINT un_correo_usuario UNIQUE (correo_institucional)
);