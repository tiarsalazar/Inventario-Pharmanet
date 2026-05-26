UPDATE sucursal
    SET cod_sucursal = CASE
                            WHEN LENGTH(cod_sucursal) < 8 THEN CONCAT('SU', UPPER(cod_sucursal)
                            ELSE CONCAT('SU', SUBSTRING(cod_sucursal, 1, LENGTH(cod_sucursal) - 2)))
                        END;

    SET tipo_sucursal = UPPER(tipo_sucursal);