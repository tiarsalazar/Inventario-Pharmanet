CREATE TABLE empleado(
    empleado_id NUMBER GENERATED ALWAYS AS IDENTITY(START WITH 1 INCREMENT BY 1),
    run VARCHAR2(10) NOT NULL CONSTRAINT un_run_emleado UNIQUE,
    nombre_completo VARCHAR2(80) NOT NULL,
    correo_institucional VARCHAR2(30) NOT NULL CONSTRAINT un_correo_empleado UNIQUE,
    telefono VARCHAR2(12) NOT NULL,
    cod_interno VARCHAR2(30) NOT NULL,
    profesion VARCHAR2(30) NOT NULL,
    direccion VARCHAR2(100),
    comuna VARCHAR2(30),
    region VARCHAR2(30),
    CONSTRAINT pk_empleado PRIMARY KEY (empleado_id)
);