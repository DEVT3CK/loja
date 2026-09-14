package br.edu.iff.ccc.devteck.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "br.edu.iff.ccc.devteck.controller.view")
public class GlobalExceptionHandler {

    // Recurso não encontrado no repositório
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ModelAndView tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("mensagem", ex.getMessage());
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }

    // Violação de regra de negócio
    @ExceptionHandler(RegraDeNegocioException.class)
    public ModelAndView tratarRegraDeNegocio(RegraDeNegocioException ex) {
        ModelAndView mv = new ModelAndView("error/erro");
        mv.addObject("mensagem", ex.getMessage());
        mv.setStatus(HttpStatus.BAD_REQUEST);
        return mv;
    }

    // Tentativa de cadastrar algo que já existe
    @ExceptionHandler(EntidadeDuplicadaException.class)
    public ModelAndView tratarEntidadeDuplicada(EntidadeDuplicadaException ex) {
        ModelAndView mv = new ModelAndView("error/erro");
        mv.addObject("mensagem", ex.getMessage());
        mv.setStatus(HttpStatus.CONFLICT);
        return mv;
    }

    // Outra exceção não prevista
    @ExceptionHandler(Exception.class)
    public ModelAndView tratarErroInesperado(Exception ex) {
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("mensagem", "Ocorreu um erro inesperado no sistema. Tente novamente mais tarde.");
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mv;
    }

}
