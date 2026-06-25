package com.smartlogix.ms_inventario.controller;

import com.smartlogix.ms_inventario.dto.ProductoRequest;
import com.smartlogix.ms_inventario.dto.ProductoResponse;
import com.smartlogix.ms_inventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión del inventario de productos.
 * <p>
 * Expone operaciones CRUD sobre productos y control de stock
 * bajo la ruta base {@code /api/inventario}.
 * </p>
 *
 * @author SmartLogix Team
 */
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Retorna el catálogo completo de productos del inventario.
     *
     * @return {@code 200 OK} con la lista de {@link ProductoResponse}
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    /**
     * Obtiene un producto por su identificador UUID.
     *
     * @param id identificador UUID del producto
     * @return {@code 200 OK} con el {@link ProductoResponse} encontrado
     * @throws RuntimeException si no existe el producto
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> obtener(
            @PathVariable String id) {
        return ResponseEntity.ok(productoService.obtener(id));
    }

    /**
     * Crea un nuevo producto en el inventario.
     *
     * @param request datos del producto a registrar
     * @return {@code 200 OK} con el {@link ProductoResponse} del producto creado
     */
    @PostMapping("/productos")
    public ResponseEntity<ProductoResponse> crear(
            @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.crear(request));
    }

    /**
     * Ajusta el stock de un producto sumando o restando la cantidad indicada.
     *
     * @param id      identificador UUID del producto
     * @param cantidad cantidad a sumar (positivo) o restar (negativo) al stock actual
     * @return {@code 200 OK} con el {@link ProductoResponse} con el stock actualizado
     * @throws RuntimeException si el producto no existe o el stock resultante es negativo
     */
    @PutMapping("/productos/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable String id,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(
            productoService.actualizarStock(id, cantidad));
    }

    /**
     * Retorna los productos cuyo stock es igual o inferior a 10 unidades.
     *
     * @return {@code 200 OK} con la lista de {@link ProductoResponse} con bajo stock
     */
    @GetMapping("/productos/bajo-stock")
    public ResponseEntity<List<ProductoResponse>> bajoStock() {
        return ResponseEntity.ok(productoService.bajoStock());
    }

    /**
     * Actualiza todos los datos de un producto existente.
     *
     * @param id      identificador UUID del producto
     * @param request nuevos datos del producto
     * @return {@code 200 OK} con el {@link ProductoResponse} actualizado
     * @throws RuntimeException si no existe el producto
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable String id,
            @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    /**
     * Elimina un producto del inventario.
     *
     * @param id identificador UUID del producto a eliminar
     * @return {@code 204 No Content} si la eliminación fue exitosa
     * @throws RuntimeException si no existe el producto
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
