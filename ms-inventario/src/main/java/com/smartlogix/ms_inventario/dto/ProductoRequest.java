package com.smartlogix.ms_inventario.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO de entrada con los datos requeridos para crear o actualizar un producto.
 *
 * @author SmartLogix Team
 */
@Data
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Integer stockMinimo;
    private String bodegaId;
    private String proveedorId;
}