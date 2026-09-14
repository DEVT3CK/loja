package br.edu.iff.ccc.devteck.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.iff.ccc.devteck.dto.ClienteRequest;
import br.edu.iff.ccc.devteck.entities.Cliente;
import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.exceptions.EntidadeDuplicadaException;
import br.edu.iff.ccc.devteck.services.ClienteUseCase;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    public ClienteController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    //CREATE
    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("clienteRequest", new ClienteRequest());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid ClienteRequest clienteRequest, BindingResult bindingResult,
                             HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "cadastro";
        }

        try {
            Cliente cliente = clienteUseCase.cadastrar(clienteRequest);

            // Depois de criar a conta, ja loga o cliente automaticamente
            session.setAttribute("usuarioLogado", cliente);
            return "redirect:/produtos";

        } catch (EntidadeDuplicadaException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("clienteRequest", clienteRequest);
            return "cadastro";
        }
    }

    //READ e UPDATE
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Cliente cliente = clienteLogado(session);
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("clienteRequest", clienteUseCase.paraRequest(cliente));
        return "perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute ClienteRequest clienteRequest,
                                   HttpSession session, Model model) {
        Cliente cliente = clienteLogado(session);
        if (cliente == null) {
            return "redirect:/login";
        }

        Cliente atualizado = clienteUseCase.atualizar(cliente.getId(), clienteRequest);

        // Atualiza tambem a sessao, para o header/nome refletirem a troca na hora
        session.setAttribute("usuarioLogado", atualizado);

        model.addAttribute("clienteRequest", clienteUseCase.paraRequest(atualizado));
        model.addAttribute("sucesso", "Dados atualizados com sucesso!");
        return "perfil";
    }

    //DELETE
    @PostMapping("/perfil/excluir")
    public String excluirConta(HttpSession session) {
        Cliente cliente = clienteLogado(session);
        if (cliente == null) {
            return "redirect:/login";
        }

        clienteUseCase.remover(cliente.getId());
        session.invalidate();
        return "redirect:/login";
    }

    private Cliente clienteLogado(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario instanceof Cliente cliente) {
            return cliente;
        }
        return null;
    }

}
