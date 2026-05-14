package com.pharmanet.producto_service.controller;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<ProductoDto>> mostrarTodos(@PageableDefault (size = 10, sort = "nombreComercial") Pageable pageable) {
        log.info("Se inicia búsqueda de todos los productos");
        Page<ProductoDto> productos = productoService.mostrarTodos(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductoDto> buscarPorSku(@PathVariable String sku) {
        ProductoDto productoDto = productoService.buscarPorSku(sku);

        return ResponseEntity.status(HttpStatus.OK).body(productoDto);
    }

    @GetMapping("/principioActivo")
    public ResponseEntity<Page<ProductoDto>> buscarPorPrincipioActivo(@RequestParam String activo,
        @PageableDeafult(size = 10, sort = {"nombreComercial", "precioVenta"}) Pageable pageable) {
        Page<ProductoDto> prodPorActivo = productoService.buscarPorPrincipioActivo(activo, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(prodPorActivo);
    }

    @GetMapping("/precioVenta")
    public ResponseEntity<Page<ProductoDto>> buscarPorPrecioVenta(
        @RequestParam @Min(value = 0, message = "El valor no puede ser negativo") int min,
        @RequestParam @Min(value = 0, message = "El valor no puede ser negativo") int max,
        @PageableDefault(size = 10, sort = {"nombreComercial", "precioVenta"}) Pageable pageable) {

        Page<ProductoDto> prodPorPrecioVenta = productoService.buscarPorPrecioVenta(min, max, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(prodPorPrecioVenta);
    }

    @PostMapping("/calcular")
    public Long calcularPrecioTotalVenta(@RequestBody Map<String, Integer> productos) {
        return productoService.calcularPrecioTotalVenta(productos);
    }

    @PostMapping
    public ResponseEntity<ProductoDto> agregarProducto(@Valid @RequestBody productoDto) {
        ProductoDto guardado = productoService.agregarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoDto);
    }

    @PutMapping
    public ResponseEntity<?> actualizarProducto(@Valid @RequestBody productoDto) {
        productoService.actualizarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.NOT_CONTENT).build();
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String sku) {
        productoService.eliminarProducto(sku);
        return ResponseEntity.status(HttpStatus.NOT_CONTEN).build();
    }

}
