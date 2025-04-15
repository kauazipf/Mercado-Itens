package br.com.fiap.Mercado_Itens.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Pageable;

import br.com.fiap.Mercado_Itens.Model.Personagem;
import br.com.fiap.Mercado_Itens.Repository.PersonagemRepository;
import br.com.fiap.Mercado_Itens.Specification.Specifications;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/personagens")
@Slf4j
public class PersonagemController {

    @Autowired
    private PersonagemRepository repository;

   @GetMapping
    @Cacheable("personagens")
    public Page<Personagem> index(@PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
        log.info("Buscando todos personagens com paginação");
        return repository.findAll(pageable);
    }

    @PostMapping
    @CacheEvict(value = "personagens", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "Falha na validação")
    })
    public Personagem create(@RequestBody @Valid Personagem personagem) {
        log.info("Cadastrando personagem " + personagem.getName() + personagem.getCharacterClass() + personagem.getLevel() + personagem.getCoins());
        return repository.save(personagem);
    }

    @GetMapping("{id}")
    public Personagem get(@PathVariable Long id) {
        log.info("Buscando personagem " + id);
        return getPersonagem(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando personagem " + id);
        repository.delete(getPersonagem(id));
    }

    @PutMapping("{id}")
    public Personagem update(@PathVariable Long id, @RequestBody @Valid Personagem personagem) {
        log.info("Atualizando personagem " + id);
        getPersonagem(id); 
        personagem.setId(id); 
        return repository.save(personagem); 
    }

    @GetMapping("/search")
    public List<Personagem> search(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Personagem.Classe classe
    ) {
        log.info("Buscando personagens por nome: {} e classe: {}", nome, classe);
        return repository.findAll(Specifications.comNomeOuClasse(nome, classe));
    }

    private Personagem getPersonagem(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Personagem não encontrado"));
    }
}