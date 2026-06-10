package com.pharmanet.producto_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.entity.Producto;
import com.pharmanet.producto_service.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    @Test
    @DisplayName("Agrega un producto exitosamente")
    void save_HappyPath() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");

        ProductoDto dto = new ProductoDto();
        dto.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());
        when(repo.save(any(Producto.class))).thenReturn(entidad);

        ProductoDto resultado = service.agregarProducto(dto);

        assertNotNull(resultado);
        assertEquals(dto.getSku(), resultado.getSku());

        verify(repo).findBySku("PR0001");
        verify(repo).save(entidad);
    }
}
