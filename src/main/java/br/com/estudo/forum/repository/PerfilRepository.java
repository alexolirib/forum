package br.com.estudo.forum.repository;

import br.com.estudo.forum.model.Curso;
import br.com.estudo.forum.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

// model, Tipo do id
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}
