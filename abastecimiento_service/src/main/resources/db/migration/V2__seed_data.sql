-- 1. INSERCIÓN EN TABLA: RECEPCIONES
INSERT INTO recepciones (
    recepcion_id, run_usuario, codigo_sucursal, numero_documento,
    tipo_documento, rut_proveedor, fecha_ingreso,
    monto_total, estado_recepcion
) VALUES
(1, '12345777-3', 'SU0001', '88431', 'FACTURA', '76.123.456-K', '2026-05-01 09:00:00', 1325000.00, 'PROCESADA'),
(2, '13987654-3', 'SU0002', '9921', 'GUIA_DESPACHO', '76.123.456-K', '2026-04-20 08:00:00', 7000000.00, 'PROCESADA'),
(3, '12345777-3', 'SU0001', '7654', 'FACTURA', '84.998.221-3', '2025-10-01 14:00:00', 240000.00, 'PROCESADA');

ALTER SEQUENCE recepciones_recepcion_id_seq RESTART WITH 4;

-- 2. INSERCIÓN EN TABLA: DETALLE_RECEIPCION
INSERT INTO detalle_recepcion (
    detalle_recepcion_id, sku, cantidad, precio_unitario, subtotal, codigo_lote, fecha_vencimiento, recepcion_id
) VALUES
(1, 'PR00001', 100,  7000.00,  700000.00, 'LOT-PR00001-001', '2027-12-31', 1),
(2, 'PR00001',  50,  7500.00,  375000.00, 'LOT-PR00001-002', '2026-07-15', 1),
(3, 'PR00002',  50,  5000.00,  250000.00, 'LOT-PR00002-022', '2028-03-20', 1),
(4, 'PR00001', 1000, 7000.00, 7000000.00, 'LOT-PR00001-B01', '2028-01-01', 2),
(5, 'PR00003',  40,  6000.00,  240000.00, 'LOT-PR00003-554', '2025-11-12', 3);

ALTER SEQUENCE detalle_recepcion_detalle_recepcion_id_seq RESTART WITH 6;
