package com.smartlogix.ms_inventario.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida con los datos públicos de un producto.
 *
 * @author SmartLogix Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {
    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Integer stockMinimo;
    private String bodegaId;
    private String proveedorId;
    private LocalDateTime createdAt;
}