-- 1. INSERCIÓN EN TABLA: INVENTARIO
INSERT INTO inventario (id, sku, codigo_sucursal, stock_total) VALUES
(1, 'PR00001', 'SU0001', 150),    -- Claritin en Farmacia Abril
(2, 'PR00002', 'SU0001', 80),     -- Losec en Farmacia Abril
(3, 'PR00015', 'SU0001', 30),     -- Xanax en Farmacia Abril
(4, 'PR00001', 'SU0002', 1000),   -- Claritin en Farmacia Av. Valparaíso
(5, 'PR00003', 'SU0001', 0);      -- Motrin (Agotado) en Farmacia Abril

ALTER SEQUENCE inventario_id_seq RESTART WITH 6;

-- 2. INSERCIÓN EN TABLA: LOTE
INSERT INTO lote (lote_id, codigo_lote, cantidad, fecha_vencimiento, estado, inventario_id) VALUES
(1, 'LOT-PR00001-001', 100, '2027-12-31', 'ACTIVO', 1),
(2, 'LOT-PR00001-002', 50, '2026-07-15', 'ACTIVO', 1),
(3, 'LOT-PR00002-022', 80, '2028-03-20', 'ACTIVO', 2),
(4, 'LOT-PR00015-099', 30, '2027-05-18', 'ACTIVO', 3),
(5, 'LOT-PR00001-B01', 1000, '2028-01-01', 'ACTIVO', 4),
(6, 'LOT-PR00003-554', 0, '2025-11-12', 'AGOTADO', 5);

ALTER SEQUENCE lote_lote_id_seq RESTART WITH 7;

-- 3. INSERCIÓN EN TABLA: MOVIMIENTO
INSERT INTO movimiento (id, tipo, sku, codigo_sucursal, cantidad, fecha, run_usuario, codigo_lote, lote_id) VALUES
(1, 'ENTRADA', 'PR00001', 'SU0001', 100, '2026-05-01 09:00:00', '12345777-3', 'LOT-PR00001-001', 1),
(2, 'ENTRADA', 'PR00001', 'SU0001', 50, '2026-05-01 09:15:00', '12345777-3', 'LOT-PR00001-002', 2),
(3, 'ENTRADA', 'PR00002', 'SU0001', 80, '2026-05-02 10:30:00', '12345777-3', 'LOT-PR00002-022', 3),
(4, 'ENTRADA', 'PR00015', 'SU0001', 30, '2026-05-03 11:00:00', '12345777-3', 'LOT-PR00015-099', 4),
(5, 'ENTRADA', 'PR00001', 'SU0002', 1000, '2026-04-20 08:00:00', '13987654-3', 'LOT-PR00001-B01', 5),
(6, 'ENTRADA', 'PR00003', 'SU0001', 40, '2025-10-01 14:00:00', '12345777-3', 'LOT-PR00003-554', 6),
(7, 'SALIDA',  'PR00003', 'SU0001', 40, '2026-01-10 16:45:00', '12345777-3', 'LOT-PR00003-554', 6);

ALTER SEQUENCE movimiento_id_seq RESTART WITH 8;