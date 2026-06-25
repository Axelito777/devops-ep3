package com.smartlogix.ms_inventario.repository;

import com.smartlogix.ms_inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.smartlogix.ms_inventario.model.Producto}.
 * <p>
 * Extiende {@link JpaRepository} con consultas derivadas para alertas de bajo stock
 * y filtrado por bodega.
 * </p>
 *
 * @author SmartLogix Team
 */
public interface ProductoRepository
    extends JpaRepository<Producto, String> {

    /**
     * Busca productos cuyo stock sea menor o igual al umbral indicado.
     *
     * @param umbral valor máximo de stock (inclusive) para considerar bajo stock
     * @return lista de productos con stock igual o inferior al umbral
     */
    List<Producto> findByStockLessThanEqual(Integer umbral);

    /**
     * Busca todos los productos almacenados en una bodega específica.
     *
     * @param bodegaId identificador de la bodega
     * @return lista de productos pertenecientes a la bodega
     */
    List<Producto> findByBodegaId(String bodegaId);
}