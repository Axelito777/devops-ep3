package com.smartlogix.ms_pedidos.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO con los datos de un producto, recibido desde el microservicio
 * ms-inventario a través de {@link com.smartlogix.ms_pedidos.client.InventarioClient}.
 *
 * @author SmartLogix Team
 */
@Data
public class ProductoDTO {
    private String id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
}