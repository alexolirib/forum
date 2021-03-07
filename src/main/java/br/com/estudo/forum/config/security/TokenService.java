package br.com.estudo.forum.config.security;

import br.com.estudo.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;



    public String gerarToken(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Date agora = new Date();
        Date expiracao = new Date(agora.getTime()+Long.parseLong(expiration));

        return Jwts.builder()
                //quem é, que aplicação
                .setIssuer("Api do Forum")
                //quem é usuario
                .setSubject(usuario.getId().toString())
                //data que foi gerado
                .setIssuedAt(agora)
                //tempo para expirar
                .setExpiration(expiracao)
                //signWith(algoritmo criptografia, senha da aplicação)
                .signWith(SignatureAlgorithm.HS256, secret)
                //transformar em string
                .compact();
    }

    public boolean isTokenValido(String token) {
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Long getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
