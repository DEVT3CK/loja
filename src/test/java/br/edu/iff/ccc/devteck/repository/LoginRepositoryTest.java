package br.edu.iff.ccc.devteck.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import br.edu.iff.ccc.devteck.entities.Cliente;
import br.edu.iff.ccc.devteck.entities.Usuario;

@DataJpaTest
class LoginRepositoryTest {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    private Cliente novoCliente(String email) {
        return new Cliente(
                null,
                "Cliente Teste",
                email,
                "123456",
                LocalDate.now(),
                "22999999999",
                "Rua Teste, 123",
                "28000-000",
                "Campos dos Goytacazes",
                "RJ"
        );
    }

    @Test
    @DisplayName("Deve salvar um usuario com sucesso e gerar o ID")
    void deveSalvarUsuarioComSucesso() {
        // Arrange (Preparacao)
        Cliente cliente = novoCliente("teste@email.com");

        // Act (Acao)
        Usuario usuarioSalvo = usuarioRepository.save(cliente);

        // Assert (Verificacao)
        assertNotNull(usuarioSalvo.getId(), "O ID nao deveria ser nulo apos salvar");
        assertEquals("teste@email.com", usuarioSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve buscar um usuario existente pelo ID")
    void deveBuscarUsuarioPorId() {
        // Arrange
        Cliente cliente = novoCliente("joao@email.com");
        Usuario usuarioSalvo = usuarioRepository.save(cliente);

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioSalvo.getId());

        // Assert
        assertTrue(usuarioEncontrado.isPresent(), "O usuario deveria ter sido encontrado");
        assertEquals(usuarioSalvo.getId(), usuarioEncontrado.get().getId());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar salvar dois usuarios com o mesmo email (unique=true)")
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // Arrange
        Cliente cliente1 = novoCliente("duplicado@email.com");
        Cliente cliente2 = novoCliente("duplicado@email.com");

        // Salvamos o primeiro (deve funcionar)
        usuarioRepository.save(cliente1);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioRepository.save(cliente2);
            usuarioRepository.flush();
        }, "Deveria lancar erro pois configuramos @Column(unique=true) no email");
    }

}
