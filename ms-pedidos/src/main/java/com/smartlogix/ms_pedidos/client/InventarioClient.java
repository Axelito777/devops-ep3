package com.smartlogix.ms_pedidos.client;

import com.smartlogix.ms_pedidos.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Cliente Feign para consultar productos y actualizar stock en el
 * microservicio ms-inventario.
 *
 * @author SmartLogix Team
 */
@FeignClient(name = "ms-inventario")
public interface InventarioClient {

    /**
     * Obtiene los datos de un producto por su id.
     *
     * @param id identificador del producto
     * @return los datos del producto
     */
    @GetMapping("/api/inventario/productos/{id}")
    ProductoDTO getProductoById(@PathVariable String id);

    /**
     * Actualiza el stock disponible de un producto.
     *
     * @param id       identificador del producto
     * @param cantidad nueva cantidad de stock a aplicar
     */
    @PutMapping("/api/inventario/productos/{id}/stock")
    void actualizarStock(@PathVariable String id, @RequestBody Integer cantidad);
}