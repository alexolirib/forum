package br.com.estudo.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoErro {
    //para pegar o local da mensagem
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    //tratamento do erro
    //esse metodo vai ser chamado quando for feito alguma exceção no controller
    //MethodArgumentNotValidException.class exceção de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormDTO> handle(MethodArgumentNotValidException exception){

        List<ErroDeFormDTO> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e->{
            //pegar localização atual
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormDTO erro = new ErroDeFormDTO(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }
}
