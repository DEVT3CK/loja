
package br.edu.iff.ccc.devteck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iff.ccc.devteck.entities.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

}