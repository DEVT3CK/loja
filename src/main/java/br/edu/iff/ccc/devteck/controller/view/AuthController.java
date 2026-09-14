package br.edu.iff.ccc.devteck.controller.view;

import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.devteck.services.UsuarioUseCase;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioUseCase usuarioUseCase;

    public AuthController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha,
                         HttpSession session, Model model) {
        try {
            Usuario usuario = usuarioUseCase.autenticar(email, senha);

            // Guarda o usuario logado na sessao, sob a chave "usuarioLogado"
            session.setAttribute("usuarioLogado", usuario);

            if ("ADMIN".equals(usuario.getTipo())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/produtos";

        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // Apaga toda a sessao, "deslogando" o usuario
        session.invalidate();
        return "redirect:/login";
    }

}
