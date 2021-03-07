package br.com.estudo.forum.repository;

import br.com.estudo.forum.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// model, Tipo do id
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    //atributo curso do atributo nome
    List<Topico> findByCurso_Nome(String nomeCurso);

//    @Query("SELECT T FROM TOPICO T WHERE T.curso.nome = :nomeCurso")
//    List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
}
