-- Creación de la tabla principal: recepciones
CREATE TABLE recepciones (
    recepcion_id BIGSERIAL PRIMARY KEY,
    run_usuario VARCHAR(10) NOT NULL,
    orden_compra VARCHAR(30),
    codigo_sucursal VARCHAR(10) NOT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    tipo_documento VARCHAR(30) NOT NULL,
    rut_proveedor VARCHAR(15) NOT NULL,
    nombre_proveedor VARCHAR(100) NOT NULL,
    fecha_ingreso TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observaciones VARCHAR(255),
    monto_total NUMERIC(12, 2) NOT NULL,
    estado_recepcion VARCHAR(255) NOT NULL DEFAULT 'PENDIENTE',
    
    -- Restricción única solicitada en @UniqueConstraint
    CONSTRAINT uk_recepcion_doc_proveedor UNIQUE (rut_proveedor, tipo_documento, numero_documento)
);

-- Creación de la tabla dependiente: detalle_recepcion
CREATE TABLE detalle_recepcion (
    detalle_recepcion_id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(30) NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL,
    subtotal NUMERIC(12, 2) NOT NULL,
    codigo_lote VARCHAR(30) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    recepcion_id BIGINT NOT NULL,
    
    -- Llave foránea con eliminación en cascada (cascadeType.ALL + orphanRemoval)
    CONSTRAINT fk_detalle_recepcion_recepcion 
        FOREIGN KEY (recepcion_id) 
        REFERENCES recepciones (recepcion_id) 
        ON DELETE CASCADE
);

-- Índice opcional para mejorar el rendimiento de las búsquedas por llave foránea
CREATE INDEX idx_detalle_recepcion_recepcion_id ON detalle_recepcion(recepcion_id);