package br.edu.iff.ccc.devteck.controller.view;

import br.edu.iff.ccc.devteck.entities.Pedido;
import br.edu.iff.ccc.devteck.entities.Produto;
import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.services.PedidoUseCase;
import br.edu.iff.ccc.devteck.services.ProdutoUseCase;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDashboardController {

    private static final int LIMITE_ESTOQUE_BAIXO = 10;

    private final PedidoUseCase pedidoUseCase;
    private final ProdutoUseCase produtoUseCase;

    public AdminDashboardController(PedidoUseCase pedidoUseCase, ProdutoUseCase produtoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
        this.produtoUseCase = produtoUseCase;
    }

  @GetMapping("/admin/dashboard")
public String dashboard(HttpSession session, Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    // Ninguém logado -> manda para o login
    if (usuarioLogado == null) {
        return "redirect:/login";
    }

    // Alguém logado, mas não é Admin
    if (!"ADMIN".equals(usuarioLogado.getTipo())) {
        return "redirect:/produtos";
    }
        List<Pedido> pedidos = pedidoUseCase.listarTodos();
        List<Produto> produtos = produtoUseCase.listarProdutos();

        double faturamentoTotal = pedidos.stream()
                .mapToDouble(Pedido::getValorTotal)
                .sum();

        List<Produto> estoqueBaixo = produtos.stream()
                .filter(Produto::isAtivo)
                .filter(p -> p.getQuantidadeEstoque() > 0 && p.getQuantidadeEstoque() <= LIMITE_ESTOQUE_BAIXO)
                .toList();

        model.addAttribute("faturamentoTotal", faturamentoTotal);
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("totalProdutos", produtos.size());
        model.addAttribute("estoqueBaixo", estoqueBaixo);

        return "adminDashboard";
    }

}
