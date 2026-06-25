package com.smartlogix.ms_pedidos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad JPA que representa un pedido, con su lista de
 * {@link DetallePedido} asociados.
 *
 * @author SmartLogix Team
 */
@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String tipo;

    private BigDecimal total;

    // Un pedido tiene muchos detalles
    @OneToMany(mappedBy = "pedido", 
               cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Inicializa las marcas de tiempo de creación y actualización, y
     * fija el estado inicial en {@code PENDIENTE}, antes de persistir
     * el pedido por primera vez.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        estado = "PENDIENTE";
    }

    /**
     * Actualiza la marca de tiempo de última modificación antes de
     * persistir un cambio sobre el pedido.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
