package br.com.estudo.forum.config.security;

import br.com.estudo.forum.model.Usuario;
import br.com.estudo.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//aqui fica a lógica de autenticação
@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = repository.findByEmail(username);
        //retornar usuario se existir e valida a senha
        if (usuario.isPresent()){
            return usuario.get();
        }
        throw  new UsernameNotFoundException("Dados inválidos!");
    }
}
