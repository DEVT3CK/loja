package br.edu.iff.ccc.devteck.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.edu.iff.ccc.devteck.dto.ItemCarrinhoView;
import br.edu.iff.ccc.devteck.entities.Carrinho;
import br.edu.iff.ccc.devteck.entities.Produto;
import br.edu.iff.ccc.devteck.repository.CarrinhoRepositorio;

@Service
public class CarrinhoUseCase {

    private final CarrinhoRepositorio carrinhoRepositorio;
    private final ProdutoUseCase produtoUseCase;

    
    public CarrinhoUseCase(CarrinhoRepositorio carrinhoRepositorio, ProdutoUseCase produtoUseCase) {
        this.carrinhoRepositorio = carrinhoRepositorio;
        this.produtoUseCase = produtoUseCase;
    }

    public void adicionarProduto(Long clienteId, Long produtoId, int quantidade) {
        Carrinho carrinho = carrinhoRepositorio.buscarPorCliente(clienteId);
        carrinho.adicionarProduto(produtoId, quantidade);
        recalcularTotal(carrinho);
        carrinhoRepositorio.salvar(carrinho);
    }

    public void removerProduto(Long clienteId, Long produtoId) {
        Carrinho carrinho = carrinhoRepositorio.buscarPorCliente(clienteId);
        carrinho.removerProduto(produtoId);
        recalcularTotal(carrinho);
        carrinhoRepositorio.salvar(carrinho);
    }

    public void atualizarQuantidade(Long clienteId, Long produtoId, int quantidade) {
        Carrinho carrinho = carrinhoRepositorio.buscarPorCliente(clienteId);
        Produto produto = produtoUseCase.buscarPorId(produtoId);

       
        int quantidadeValida = Math.min(quantidade, produto.getQuantidadeEstoque());

        carrinho.definirQuantidade(produtoId, quantidadeValida);
        recalcularTotal(carrinho);
        carrinhoRepositorio.salvar(carrinho);
    }

    public void limparCarrinho(Long clienteId) {
        Carrinho carrinho = carrinhoRepositorio.buscarPorCliente(clienteId);
        carrinho.limparCarrinho();
        carrinhoRepositorio.salvar(carrinho);
    }

    public Carrinho buscarCarrinho(Long clienteId) {
        return carrinhoRepositorio.buscarPorCliente(clienteId);
    }

    
    public List<ItemCarrinhoView> listarItens(Long clienteId) {
        Carrinho carrinho = buscarCarrinho(clienteId);
        List<ItemCarrinhoView> view = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : carrinho.getItens().entrySet()) {
            Produto produto = produtoUseCase.buscarPorId(entry.getKey());
            view.add(new ItemCarrinhoView(produto.getId(), produto.getNome(),
                    produto.getPreco(), entry.getValue()));
        }

        return view;
    }

    private void recalcularTotal(Carrinho carrinho) {
        double total = 0;
        for (Map.Entry<Long, Integer> entry : carrinho.getItens().entrySet()) {
            Produto produto = produtoUseCase.buscarPorId(entry.getKey());
            total += produto.getPreco() * entry.getValue();
        }
        carrinho.setValorTotal(total);
    }

}
