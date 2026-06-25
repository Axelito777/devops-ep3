package com.smartlogix.ms_inventario.service;

import com.smartlogix.ms_inventario.dto.ProductoRequest;
import com.smartlogix.ms_inventario.dto.ProductoResponse;
import com.smartlogix.ms_inventario.model.Producto;
import com.smartlogix.ms_inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para la gestión del inventario de productos.
 * <p>
 * Implementa las operaciones CRUD sobre {@link com.smartlogix.ms_inventario.model.Producto},
 * el control de stock con validación de negativos y la consulta de productos con bajo stock.
 * </p>
 *
 * @author SmartLogix Team
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Retorna el catálogo completo de productos.
     *
     * @return lista de {@link ProductoResponse}; vacía si no hay productos
     */
    @Cacheable(value = "productos", key = "'all'")
    public List<ProductoResponse> listar() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por su identificador UUID.
     *
     * @param id identificador UUID del producto
     * @return {@link ProductoResponse} con los datos del producto
     * @throws RuntimeException si no existe el producto
     */
    @Cacheable(value = "productos", key = "#id")
    public ProductoResponse obtener(String id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Producto no encontrado"));
        return convertirAResponse(producto);
    }

    /**
     * Crea y persiste un nuevo producto en el inventario.
     *
     * @param request datos del producto a registrar
     * @return {@link ProductoResponse} del producto recién creado
     */
    @CacheEvict(value = "productos", allEntries = true)
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setBodegaId(request.getBodegaId());
        producto.setProveedorId(request.getProveedorId());

        productoRepository.save(producto);
        return convertirAResponse(producto);
    }

    /**
     * Ajusta el stock de un producto sumando la cantidad indicada (puede ser negativa).
     *
     * @param id      identificador UUID del producto
     * @param cantidad cantidad a sumar (positivo) o restar (negativo) al stock actual
     * @return {@link ProductoResponse} con el stock actualizado
     * @throws RuntimeException si el producto no existe o el stock resultante sería negativo
     */
    @CacheEvict(value = "productos", allEntries = true)
    public ProductoResponse actualizarStock(
            String id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Producto no encontrado"));

        int stockNuevo = producto.getStock() + cantidad;

        if (stockNuevo < 0) {
            throw new RuntimeException("Stock insuficiente");
        }

        producto.setStock(stockNuevo);
        productoRepository.save(producto);
        return convertirAResponse(producto);
    }

    /**
     * Retorna los productos con stock igual o inferior a 10 unidades.
     *
     * @return lista de {@link ProductoResponse} con bajo stock; vacía si todos tienen stock suficiente
     */
    public List<ProductoResponse> bajoStock() {
        return productoRepository
                .findByStockLessThanEqual(10)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private ProductoResponse convertirAResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getStockMinimo(),
                producto.getBodegaId(),
                producto.getProveedorId(),
                producto.getCreatedAt()
        );
    }

    /**
     * Actualiza todos los datos de un producto existente.
     *
     * @param id      identificador UUID del producto
     * @param request nuevos datos del producto
     * @return {@link ProductoResponse} con los datos actualizados
     * @throws RuntimeException si no existe el producto
     */
    @CacheEvict(value = "productos", allEntries = true)
    public ProductoResponse actualizar(String id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Producto no encontrado"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setBodegaId(request.getBodegaId());
        producto.setProveedorId(request.getProveedorId());

        productoRepository.save(producto);
        return convertirAResponse(producto);
    }

    /**
     * Elimina un producto del inventario.
     *
     * @param id identificador UUID del producto a eliminar
     * @throws RuntimeException si no existe el producto
     */
    @CacheEvict(value = "productos", allEntries = true)
    public void eliminar(String id) {
        productoRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Producto no encontrado"));
        productoRepository.deleteById(id);
    }
}