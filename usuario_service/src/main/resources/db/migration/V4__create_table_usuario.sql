CREATE TABLE usuario (
    usuario_id NUMBER GENERATED ALWAYS AS IDENTITY(START WITH 1 INCREMENT BY 1),
    nombre_usuario VARCHAR2(30) NOT NULL CONSTRAINT un_nombre_usuario UNIQUE,
    password VARCHAR2(60) NOT NULL,
    empleado_id NUMBER NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY(usuario_id),
    CONSTRAINT fk_usuario_empleado FOREIGN KEY(empleado_id) REFERENCES empleado(empleado_id)
);

INSERT INTO usuario (nombre_usuario, password, empleado_id)
    SELECT
        LOWER(SUBSTR(REPLACE(nombre_completo, ' ', ''), 0, 3))
        || SUBSTR(run, 1, 4)
        || EXTRACT(YEAR FROM SYSDATE)
        || EXTRACT(MONTH FROM SYSDATE) AS nombre_usuario,

        SUBSTR(REPLACE(nombre_completo, ' ', ''), 4, 3)
        || SUBSTR(run, -4) AS password,

        empleado_id
    FROM empleado;
