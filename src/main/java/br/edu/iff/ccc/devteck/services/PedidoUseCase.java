package br.edu.iff.ccc.devteck.services;

import br.edu.iff.ccc.devteck.entities.Carrinho;
import br.edu.iff.ccc.devteck.entities.ItemPedido;
import br.edu.iff.ccc.devteck.entities.Pedido;
import br.edu.iff.ccc.devteck.entities.Produto;
import br.edu.iff.ccc.devteck.exceptions.RecursoNaoEncontradoException;
import br.edu.iff.ccc.devteck.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.devteck.repository.PedidoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PedidoUseCase {

    private final PedidoRepositorio pedidoRepositorio;
    private final CarrinhoUseCase carrinhoUseCase;
    private final ProdutoUseCase produtoUseCase;

    public PedidoUseCase(PedidoRepositorio pedidoRepositorio,
                          CarrinhoUseCase carrinhoUseCase,
                          ProdutoUseCase produtoUseCase) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.carrinhoUseCase = carrinhoUseCase;
        this.produtoUseCase = produtoUseCase;
    }

    public Pedido finalizarPedido(Long clienteId, String enderecoEntrega) {
        Carrinho carrinho = carrinhoUseCase.buscarCarrinho(clienteId);

        if (carrinho.getItens().isEmpty()) {
            throw new RegraDeNegocioException("Carrinho vazio, nao e possivel finalizar o pedido");
        }

        Pedido pedido = new Pedido(null, clienteId, enderecoEntrega);

        // 1a passada: valida estoque e monta os itens do pedido
        for (Map.Entry<Long, Integer> entry : carrinho.getItens().entrySet()) {
            Long produtoId = entry.getKey();
            int quantidade = entry.getValue();
            Produto produto = produtoUseCase.buscarPorId(produtoId);

            if (quantidade > produto.getQuantidadeEstoque()) {
                throw new RegraDeNegocioException(
                        "Estoque insuficiente para " + produto.getNome() +
                        " (disponivel: " + produto.getQuantidadeEstoque() + ")");
            }

            pedido.adicionarItem(new ItemPedido(produto.getId(), produto.getNome(),
                    quantidade, produto.getPreco()));
        }

        // 2a passada: so agora da baixa no estoque, ja que sabemos que
        // TODOS os itens sao validos
        for (ItemPedido item : pedido.getItens()) {
            produtoUseCase.darBaixaEstoque(item.getProdutoId(), item.getQuantidade());
        }

        pedido.calcularTotal();
        pedidoRepositorio.save(pedido);
        carrinhoUseCase.limparCarrinho(clienteId);

        return pedido;
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepositorio.findByClienteId(clienteId);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepositorio.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado: id " + id));
    }

    public void atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.alterarStatus(novoStatus);
        pedidoRepositorio.save(pedido);
    }

    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        for (ItemPedido item : pedido.getItens()) {
            produtoUseCase.devolverEstoque(item.getProdutoId(), item.getQuantidade());
        }

        pedidoRepositorio.deleteById(id);
    }

}
