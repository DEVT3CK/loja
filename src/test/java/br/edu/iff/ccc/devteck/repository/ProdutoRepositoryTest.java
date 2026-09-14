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

import br.edu.iff.ccc.devteck.entities.Produto;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepositorio produtoRepository;

    private Produto novoProduto(String nome) {
        return new Produto(
                null,              
                nome,
                7891000100103L,    
                "Descricao de teste",
                9.99,
                50,
                "Categoria Teste",
                null,
                true
        );
    }

    @Test
    @DisplayName("Deve salvar um produto com sucesso e gerar o ID")
    void deveSalvarProdutoComSucesso() {
        // Arrange (Preparacao)
        Produto produto = novoProduto("Coca-Cola 2L");

        // Act (Acao)
        Produto produtoSalvo = produtoRepository.save(produto);

        // Assert (Verificacao)
        assertNotNull(produtoSalvo.getId(), "O ID nao deveria ser nulo apos salvar");
        assertEquals("Coca-Cola 2L", produtoSalvo.getNome());
    }

    @Test
    @DisplayName("Deve buscar um produto existente pelo ID")
    void deveBuscarProdutoPorId() {
        // Arrange
        Produto produto = novoProduto("Arroz Integral 1kg");
        Produto produtoSalvo = produtoRepository.save(produto);

        // Act
        Optional<Produto> produtoEncontrado = produtoRepository.findById(produtoSalvo.getId());

        // Assert
        assertTrue(produtoEncontrado.isPresent(), "O produto deveria ter sido encontrado");
        assertEquals(produtoSalvo.getId(), produtoEncontrado.get().getId());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar salvar produtos com nomes duplicados (unique=true)")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // Arrange
        Produto produto1 = novoProduto("Leite Integral 1L");
        Produto produto2 = novoProduto("Leite Integral 1L");

        // Salvamos o primeiro (deve funcionar)
        produtoRepository.save(produto1);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            produtoRepository.save(produto2);
            produtoRepository.flush();
        }, "Deveria lancar erro pois configuramos @Column(unique=true) no nome");
    }

}
