package br.edu.iff.ccc.devteck.services;

import br.edu.iff.ccc.devteck.dto.ClienteRequest;
import br.edu.iff.ccc.devteck.entities.Cliente;
import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.exceptions.EntidadeDuplicadaException;
import br.edu.iff.ccc.devteck.exceptions.RecursoNaoEncontradoException;
import br.edu.iff.ccc.devteck.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.devteck.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteUseCase {

    private final UsuarioRepositorio usuarioRepositorio;

    public ClienteUseCase(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    // Filtra a tabela unica de usuarios para devolver apenas os que sao Cliente
    public List<Cliente> listarTodos() {
        return usuarioRepositorio.findAll().stream()
                .filter(usuario -> usuario instanceof Cliente)
                .map(usuario -> (Cliente) usuario)
                .toList();
    }

    public Cliente cadastrar(ClienteRequest request) {
        if (usuarioRepositorio.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new EntidadeDuplicadaException("Ja existe uma conta cadastrada com este email");
        }

        Cliente cliente = new Cliente(
                null,
                request.getNome(),
                request.getEmail(),
                request.getSenha(),
                LocalDate.now(),
                request.getTelefone(),
                request.getEndereco(),
                request.getCep(),
                request.getCidade(),
                request.getEstado()
        );

        usuarioRepositorio.save(cliente);
        return cliente;
    }

    public Cliente buscarPorId(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: id " + id));

        if (!(usuario instanceof Cliente cliente)) {
            throw new RegraDeNegocioException("Usuario encontrado nao e um Cliente");
        }
        return cliente;
    }

    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);

        cliente.setNome(request.getNome());
        cliente.setTelefone(request.getTelefone());
        cliente.setEndereco(request.getEndereco());
        cliente.setCep(request.getCep());
        cliente.setCidade(request.getCidade());
        cliente.setEstado(request.getEstado());

        // Senha so e trocada se o campo vier preenchido - deixar em
        // branco significa "manter a senha atual"
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            cliente.setSenha(request.getSenha());
        }

        usuarioRepositorio.save(cliente);
        return cliente;
    }

    public void remover(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    public ClienteRequest paraRequest(Cliente cliente) {
        ClienteRequest request = new ClienteRequest();
        request.setNome(cliente.getNome());
        request.setEmail(cliente.getEmail());
        request.setTelefone(cliente.getTelefone());
        request.setEndereco(cliente.getEndereco());
        request.setCep(cliente.getCep());
        request.setCidade(cliente.getCidade());
        request.setEstado(cliente.getEstado());
        return request;
    }

}
