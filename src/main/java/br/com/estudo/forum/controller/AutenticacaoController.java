package br.com.estudo.forum.controller;

import br.com.estudo.forum.config.security.TokenService;
import br.com.estudo.forum.controller.dto.TokenDto;
import br.com.estudo.forum.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
//funcionar em dois profiles
@Profile( value = {"prod", "test"})
public class AutenticacaoController {

    //tive que sobrescrever metodo no SecurityConfig para funcionar a injeção de dependencia
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
        //aqui é feito a autenticação
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            //nessa linha será chamado a classe AutenticacaoService
            Authentication authentication = authManager.authenticate(dadosLogin);
            //se der tudo certo, aí vamos gerar o token
            String token = tokenService.gerarToken(authentication);

            System.out.println(token);
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
