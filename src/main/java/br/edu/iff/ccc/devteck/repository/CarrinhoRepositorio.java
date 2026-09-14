
package br.edu.iff.ccc.devteck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iff.ccc.devteck.entities.Carrinho;

public interface CarrinhoRepositorio extends JpaRepository<Carrinho, Long> {

    Optional<Carrinho> findByClienteId(Long clienteId);

    default Carrinho buscarPorCliente(Long clienteId) {
        return findByClienteId(clienteId)
                .orElseGet(() -> save(new Carrinho(null, clienteId)));
    }

    default Carrinho salvar(Carrinho carrinho) {
        return save(carrinho);
    }

}