package br.com.estudo.forum.controller;

import br.com.estudo.forum.controller.dto.TopicoDTO;
import br.com.estudo.forum.controller.form.TopicoForm;
import br.com.estudo.forum.model.Curso;
import br.com.estudo.forum.model.Topico;
import br.com.estudo.forum.repository.CursoRepository;
import br.com.estudo.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

//@Controller
//RestController - aí já por padrão os métodos dessa classe não necessita no ResponseBody
@RestController
@RequestMapping("/topicos")
public class TopicoController {

    //injeção de dependencia
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

//    @ResponseBody
    @GetMapping
    //parametro se for enviado parametro
    public List<TopicoDTO> lista(String nomeCurso){
        List<Topico> topicos;
        if (nomeCurso == null) {
            topicos = topicoRepository.findAll();
        } else {

            topicos = topicoRepository.findByCurso_Nome(nomeCurso);
        }
        return TopicoDTO.converter(topicos);
    }

    @PostMapping
    //o @valid faz a validação de acordo com as validecions que foi definido
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        //ResponseEntity.created(uri);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }
}
