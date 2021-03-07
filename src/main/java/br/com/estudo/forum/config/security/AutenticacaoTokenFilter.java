package br.com.estudo.forum.config.security;


import br.com.estudo.forum.model.Usuario;
import br.com.estudo.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//validar token
//serve como pipeline na requisição
//OncePerRequestFilter - é um filter que é chamada uma vez em cada requisição
public class AutenticacaoTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UsuarioRepository repository;

    public AutenticacaoTokenFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);

        if (valido){
            autenticarCliente(token);
        }


        //obrigatorio chamar esse metodo no final
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    //aqui só é atutentique de acordo com o token
    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token);
        Optional<Usuario> optionalUsuario = repository.findById(idUsuario);
        Usuario usuario = (Usuario) optionalUsuario.get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        //para pegar somente o token sem o "Bearer "
        return token.substring(7, token.length());
    }
}
