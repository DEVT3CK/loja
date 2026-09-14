package br.edu.iff.ccc.devteck.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import br.edu.iff.ccc.devteck.entities.Pedido;

@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepositorio pedidoRepository;

    @Test
    @DisplayName("Deve salvar um pedido com sucesso e gerar o ID")
    void deveSalvarPedidoComSucesso() {
        // Arrange (Preparacao)
        Pedido pedido = new Pedido(null, 1L, "Rua das Flores, 123 - Centro");

        // Act (Acao)
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Assert (Verificacao)
        assertNotNull(pedidoSalvo.getId(), "O ID nao deveria ser nulo apos salvar");
        assertEquals("PENDENTE", pedidoSalvo.getStatus());
    }

    @Test
    @DisplayName("Deve buscar um pedido existente pelo ID")
    void deveBuscarPedidoPorId() {
        // Arrange
        Pedido pedido = new Pedido(null, 2L, "Av. Principal, 456 - Jardim");
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Act
        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(pedidoSalvo.getId());

        // Assert
        assertTrue(pedidoEncontrado.isPresent(), "O pedido deveria ter sido encontrado");
        assertEquals(pedidoSalvo.getId(), pedidoEncontrado.get().getId());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar salvar pedido sem cliente associado (nullable=false)")
    void deveLancarExcecaoQuandoClienteIdNulo() {
        // Arrange
        Pedido pedidoSemCliente = new Pedido(null, null, "Endereco qualquer");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            pedidoRepository.save(pedidoSemCliente);
            pedidoRepository.flush();
        }, "Deveria lancar erro pois configuramos @Column(nullable=false) no clienteId");
    }

}
