package br.edu.iff.ccc.devteck.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.iff.ccc.devteck.dto.ProdutoRequest;
import br.edu.iff.ccc.devteck.entities.Produto;
import br.edu.iff.ccc.devteck.exceptions.EntidadeDuplicadaException;
import br.edu.iff.ccc.devteck.exceptions.RecursoNaoEncontradoException;
import br.edu.iff.ccc.devteck.repository.ProdutoRepositorio;

@Service
public class ProdutoUseCase {

    private final ProdutoRepositorio produtoRepositorio;

    public ProdutoUseCase(ProdutoRepositorio produtoRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
    }

    public Produto cadastrarProduto(ProdutoRequest request) {
        Optional<Produto> existente = produtoRepositorio.findByNomeIgnoreCase(request.getNome());

        if (existente.isPresent()) {
            Produto produto = existente.get();

            if (produto.isAtivo()) {
                // Ja existe um produto ATIVO com este nome - nao deixa criar um duplicado de verdade.
                throw new EntidadeDuplicadaException(
                        "Já existe um produto ativo cadastrado com o nome '" + request.getNome() + "'");
            }

            // Existe um produto com este nome, mas ele esta desativado.
            // Em vez de falhar por causa da constraint de unicidade em "nome",
            // reaproveita o registro: reativa e atualiza os dados com o que veio no request.
            produto.setCodigoBarras(request.getCodigoBarras());
            produto.setDescricao(request.getDescricao());
            produto.atualizarPreco(request.getPreco());
            produto.atualizarEstoque(request.getQuantidadeEstoque());
            produto.setCategoria(request.getCategoria());
            produto.setImagem(request.getImagem());
            produto.alterarStatus(true);
            return produtoRepositorio.save(produto);
        }

        Produto produto = new Produto(
                null,
                request.getNome(),
                request.getCodigoBarras(),
                request.getDescricao(),
                request.getPreco(),
                request.getQuantidadeEstoque(),
                request.getCategoria(),
                request.getImagem(),
                true
        );
        return produtoRepositorio.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepositorio.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado: id " + id));
    }

    public Produto atualizarProduto(Long id, ProdutoRequest request) {
        Produto produto = buscarPorId(id);
        produto.setNome(request.getNome());
        produto.setCodigoBarras(request.getCodigoBarras());
        produto.setDescricao(request.getDescricao());
        produto.atualizarPreco(request.getPreco());
        produto.atualizarEstoque(request.getQuantidadeEstoque());
        produto.setCategoria(request.getCategoria());
        produto.setImagem(request.getImagem());
        return produtoRepositorio.save(produto);
    }

    
    public void darBaixaEstoque(Long produtoId, int quantidade) {
        Produto produto = buscarPorId(produtoId);
        int novoEstoque = produto.getQuantidadeEstoque() - quantidade;
        produto.atualizarEstoque(Math.max(novoEstoque, 0));
        produtoRepositorio.save(produto);
    }

    
    public void devolverEstoque(Long produtoId, int quantidade) {
        Produto produto = buscarPorId(produtoId);
        produto.atualizarEstoque(produto.getQuantidadeEstoque() + quantidade);
        produtoRepositorio.save(produto);
    }

    public void removerProduto(Long id) {
        
        Produto produto = buscarPorId(id);
        produto.alterarStatus(false);
        produtoRepositorio.save(produto);
    }

    public ProdutoRequest paraRequest(Produto produto) {
        
        ProdutoRequest request = new ProdutoRequest();
        request.setNome(produto.getNome());
        request.setCodigoBarras(produto.getCodigoBarras());
        request.setDescricao(produto.getDescricao());
        request.setPreco(produto.getPreco());
        request.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        request.setCategoria(produto.getCategoria());
        request.setImagem(produto.getImagem());
        return request;
    }

}
