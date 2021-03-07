package br.com.estudo.forum.controller;

import br.com.estudo.forum.controller.dto.DetalheTopicoDTO;
import br.com.estudo.forum.controller.dto.TopicoDTO;
import br.com.estudo.forum.controller.form.AtualizacaoTopicoForm;
import br.com.estudo.forum.controller.form.TopicoForm;
import br.com.estudo.forum.model.Curso;
import br.com.estudo.forum.model.Topico;
import br.com.estudo.forum.repository.CursoRepository;
import br.com.estudo.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<DetalheTopicoDTO> detalhar(@PathVariable("id") Long codigo){
        Optional<Topico> topico = topicoRepository.findById(codigo);
        if (topico.isPresent()){
            return ResponseEntity.ok(new DetalheTopicoDTO(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    //fazer commit no final
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm atualizacaoTopicoForm){
        Optional<Topico> opcional = topicoRepository.findById(id);
        if(opcional.isPresent()){
            //por setar os valores já é atualizado no banco
            Topico topico = atualizacaoTopicoForm.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDTO(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id){Optional<Topico> opcional = topicoRepository.findById(id);
        if(opcional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
