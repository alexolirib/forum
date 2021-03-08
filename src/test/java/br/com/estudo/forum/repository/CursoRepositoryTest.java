package br.com.estudo.forum.repository;

import br.com.estudo.forum.model.Curso;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


//para testar repository
@DataJpaTest
//se for usar um banco que não seja em memoria
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//será utilizado o profile de test
@ActiveProfiles("test")
public class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    //por conta do banco vazio vamos inserir os dados
    @Autowired
    private TestEntityManager em;

    @Test
    public void deveriaCarregarUmCursoPelaBuscaDoNome(){

        String nomeCurso = "HTML 5";
        Curso html5 = new Curso();
        html5.setNome(nomeCurso);
        html5.setCategoria("Programação");
        em.persist(html5);

        Curso curso = cursoRepository.findByNome(nomeCurso);
        Assert.assertNotNull(curso);
        Assert.assertEquals(nomeCurso, curso.getNome());
    }

    @Test
    public void naoDeveriaCarregarUmCursoQueNaoEstejaCadastrado(){
        String nomeCurso = "JPA";
        Curso curso = cursoRepository.findByNome(nomeCurso);
        Assert.assertNull(curso);
    }
}
