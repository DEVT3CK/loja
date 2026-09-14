package br.edu.iff.ccc.devteck.services;

import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.devteck.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;

@Service
public class UsuarioUseCase {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioUseCase(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepositorio.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RegraDeNegocioException("Email ou senha invalidos"));

        if (!usuario.login(senha)) {
            throw new RegraDeNegocioException("Email ou senha invalidos");
        }

        return usuario;
    }

}
