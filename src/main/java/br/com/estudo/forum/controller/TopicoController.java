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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    //para funcionar a paginação dessa forma foi necessario acrescentar
    //EnableSpringDataWebSupport na classe que possui metodo main do spring
    //para pegar de forma automatico é
    //?page=&size=&sort=<field>,(asc or desc)
    //(pode ter diversos sort)
    //?page=0&size=3&sort=id,desc&sort=dataCriacao,asc
//    @Cacheable(value = é o id desse meu cache)
    @Cacheable(value = "listaDeTopico")
    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso,
                                //valores padrão que por acaso não for enviado pelo cliente
                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable paginacao){

        Page<Topico> topicos;
        if (nomeCurso == null) {
//            topicos = topicoRepository.findAll();
            topicos = topicoRepository.findAll(paginacao);
        } else {
            topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
        }
        return TopicoDTO.converter(topicos);
    }
    //antiga forma para paginacao
    //    @ResponseBody
//    @GetMapping
//    //parametro se for enviado parametro
//    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso,
//                                 @RequestParam int pagina,
//                                 @RequestParam int qtd,
//                                 @RequestParam String ordenacao){
//        Pageable paginacao = PageRequest.of(pagina,
//                qtd,
//                Sort.Direction.ASC,
//                ordenacao);
//
//        Page<Topico> topicos;
//        if (nomeCurso == null) {
////            topicos = topicoRepository.findAll();
//            topicos = topicoRepository.findAll(paginacao);
//        } else {
//            topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
//        }
//        return TopicoDTO.converter(topicos);
//    }

    @PostMapping
    //limpar esse cache  (assim irá atualizar)
    //allEntries  - limpar todos os registros
    @CacheEvict(value = "listaDeTopico", allEntries = true)
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
    //limpar esse cache  (assim irá atualizar)
    @CacheEvict(value = "listaDeTopico", allEntries = true)
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
    //limpar esse cache  (assim irá atualizar)
    @CacheEvict(value = "listaDeTopico", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id){Optional<Topico> opcional = topicoRepository.findById(id);
        if(opcional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
