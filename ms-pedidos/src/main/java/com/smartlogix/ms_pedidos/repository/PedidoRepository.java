package com.smartlogix.ms_pedidos.repository;

import com.smartlogix.ms_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.smartlogix.ms_pedidos.model.Pedido}.
 * <p>
 * Hereda operaciones CRUD de {@link JpaRepository} y añade búsquedas
 * por cliente y por estado utilizadas en los flujos de consulta.
 * </p>
 *
 * @author SmartLogix Team
 */
public interface PedidoRepository
    extends JpaRepository<Pedido, String> {

    /**
     * Busca todos los pedidos de un cliente específico.
     *
     * @param clienteId identificador UUID del cliente
     * @return lista de pedidos del cliente; vacía si no tiene pedidos
     */
    List<Pedido> findByClienteId(String clienteId);

    /**
     * Busca todos los pedidos que se encuentran en un estado determinado.
     *
     * @param estado estado del pedido a filtrar (ej. {@code PENDIENTE}, {@code ENTREGADO})
     * @return lista de pedidos en el estado indicado
     */
    List<Pedido> findByEstado(String estado);
}
