package br.com.estudo.forum.controller;

import br.com.estudo.forum.controller.dto.TopicoDTO;
import br.com.estudo.forum.model.Curso;
import br.com.estudo.forum.model.Topico;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

//@Controller
//RestController - aí já por padrão os métodos dessa classe não necessita no ResponseBody
@RestController
public class ForumController {

//    @ResponseBody
    @RequestMapping("/topicos")
    public List<TopicoDTO> lista(){
        List<Topico> topicos = Arrays.asList(
                new Topico("Duvida", "Duvida spring",  new Curso("Spring", "Programacao")),
                new Topico("Duvida", "Duvida spring",  new Curso("Spring", "Programacao")),
                new Topico("Duvida", "Duvida spring",  new Curso("Spring", "Programacao"))
        );

        return TopicoDTO.converter(topicos);
    }
}
