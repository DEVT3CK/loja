package br.edu.iff.ccc.devteck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iff.ccc.devteck.entities.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeIgnoreCase(String nome);

}
