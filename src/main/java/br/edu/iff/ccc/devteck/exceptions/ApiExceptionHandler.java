package br.edu.iff.ccc.devteck.exceptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "br.edu.iff.ccc.devteck.controller.apirest")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TYPE_BASE = "https://devteck.iff.edu.br/erros/";

    // Recurso nao encontrado no repositorio
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso não encontrado");
        problem.setType(URI.create(TYPE_BASE + "recurso-nao-encontrado"));
        return problem;
    }

    // Violacao de uma regra de negocio
    @ExceptionHandler(RegraDeNegocioException.class)
    public ProblemDetail tratarRegraDeNegocio(RegraDeNegocioException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Violação de regra de negócio");
        problem.setType(URI.create(TYPE_BASE + "regra-de-negocio"));
        return problem;
    }

    // Tentativa de cadastrar algo que ja existe
    @ExceptionHandler(EntidadeDuplicadaException.class)
    public ProblemDetail tratarEntidadeDuplicada(EntidadeDuplicadaException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Entidade duplicada");
        problem.setType(URI.create(TYPE_BASE + "entidade-duplicada"));
        return problem;
    }

    // Qualquer outra excecao nao prevista
    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroInesperado(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado no sistema. Tente novamente mais tarde.");
        problem.setTitle("Erro interno do servidor");
        problem.setType(URI.create(TYPE_BASE + "erro-interno"));
        return problem;
    }

    // Erros de validacao do @Valid nos DTOs recebidos via @RequestBody.
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos. Verifique os detalhes em 'invalid_params'.");
        problem.setTitle("Erro de validação");
        problem.setType(URI.create(TYPE_BASE + "erro-de-validacao"));

        List<Map<String, String>> invalidParams = new ArrayList<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            invalidParams.add(Map.of(
                    "name", erro.getField(),
                    "reason", erro.getDefaultMessage() != null ? erro.getDefaultMessage() : "Valor inválido"
            ));
        }
        problem.setProperty("invalid_params", invalidParams);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

}
