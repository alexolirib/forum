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
@Profile("dev")
public class DevSecurityConfiguration extends WebSecurityConfigurerAdapter {

    //configurações de autorizações
    //controle de acesso (publico ou privado
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //permitir tudo por ser dev
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/**").permitAll()
                .and().csrf().disable();
        //permite tudo
//        http.authorizeRequests()
//                .antMatchers("/topicos").permitAll();
    }
}
