-- INVENTARIO
CREATE TABLE inventario (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(30) NOT NULL,
    codigo_sucursal VARCHAR(10) NOT NULL,
    stock_total INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT uk_sku_sucursal UNIQUE (sku, codigo_sucursal),
    CONSTRAINT chk_stock_no_negativo CHECK (stock_total >= 0)
);
CREATE INDEX idx_inventario_sku ON inventario(sku);
CREATE INDEX idx_inventario_sucursal ON inventario(codigo_sucursal);


-- LOTES
CREATE TABLE lote (
    lote_id BIGSERIAL PRIMARY KEY,
    codigo_lote VARCHAR(30) NOT NULL,
    cantidad INTEGER NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    inventario_id BIGINT NOT NULL,
    CONSTRAINT fk_lote_inventario FOREIGN KEY (inventario_id)
        REFERENCES inventario(id)
        ON DELETE CASCADE,
    CONSTRAINT chk_estado_lote CHECK (
        estado IN ('ACTIVO', 'VENCIDO', 'AGOTADO', 'DEFECTUOSO')
    ),
    CONSTRAINT chk_cantidad_lote CHECK (cantidad >= 0)
);
CREATE INDEX idx_lotes_inventario ON lote(inventario_id);
CREATE INDEX idx_lotes_codigo ON lote(codigo_lote);
CREATE INDEX idx_lotes_estado ON lote(estado);


-- MOVIMIENTOS
CREATE TABLE movimiento (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    sku VARCHAR(30) NOT NULL,
    codigo_sucursal VARCHAR(10) NOT NULL,  
    codigo_lote VARCHAR(30) NOT NULL,     
    cantidad INTEGER NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT NOW(),
    run_usuario VARCHAR(20) NOT NULL,    
    lote_id BIGINT,
    CONSTRAINT fk_movimiento_lote FOREIGN KEY (lote_id)
        REFERENCES lote(lote_id)
        ON DELETE SET NULL,
    CONSTRAINT chk_tipo_movimiento CHECK (
        tipo IN ('ENTRADA', 'SALIDA', 'AJUSTE', 'VENCIMIENTO')
        ),
    CONSTRAINT chk_cantidad_movimiento CHECK (cantidad > 0)
    );
CREATE INDEX idx_movimiento_lote ON movimiento(lote_id);
CREATE INDEX idx_movimiento_fecha ON movimiento(fecha);
CREATE INDEX idx_movimiento_usuario ON movimiento(run_usuario);
CREATE INDEX idx_movimiento_sku ON movimiento(sku);
CREATE INDEX idx_movimiento_sucursal ON movimiento(codigo_sucursal);