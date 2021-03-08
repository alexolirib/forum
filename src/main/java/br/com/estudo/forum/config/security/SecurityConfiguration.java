package br.com.estudo.forum.config.security;

import br.com.estudo.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//habilitar security
@EnableWebSecurity
//pois é uma classe de configuração
@Configuration
@Profile( value = {"prod", "test"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private UsuarioRepository repository;

    //como é classe de configuração conseguimos fazer a injeção
    @Autowired
    private TokenService tokenService;

    //para fazer a injeção de dependencia
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //método que serve para configurar a autenticação (login)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //userDetailsService - qual é a service que tem a lógica de autenticação
        //criptografar a senha - informando qual é a criptografia que é utilizado no sistema
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //configurações de autorizações
    //controle de acesso (publico ou privado
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //quais requests vamos autorizar
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
//                importante deixar privado
                .antMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                //configurar autorização por perfil
                .antMatchers(HttpMethod.DELETE,"/topicos/*").hasRole("MODERADOR")
                //o resto precisa está autenticado
                .anyRequest().authenticated()
                //criar formulario de login (porém cria sessão )
//                .and().formLogin()
                //por ser via token
                .and().csrf().disable()
                //qual será a política de autenticação (informando não é para criar estado)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //meu filter será executado apos o filter(UsernamePasswordAuthenticationFilter)
                .and().addFilterBefore(new AutenticacaoTokenFilter(tokenService, repository), UsernamePasswordAuthenticationFilter.class)
        ;
        //permite tudo
//        http.authorizeRequests()
//                .antMatchers("/topicos").permitAll();
    }

    //configurações de recursos estaticos(js, css, images, etc.)
    @Override
    public void configure(WebSecurity web) throws Exception {
        //para swagger
        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**", "/**.ico");
    }
}
