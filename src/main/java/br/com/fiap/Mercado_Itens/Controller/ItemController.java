// ItemController atualizado com Specification e filtro completo
package br.com.fiap.Mercado_Itens.Controller;

import br.com.fiap.Mercado_Itens.Repository.ItemRepository;
import br.com.fiap.Mercado_Itens.Model.Item;
import br.com.fiap.Mercado_Itens.Model.Personagem;
import br.com.fiap.Mercado_Itens.Specification.Specifications;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("itens")
@Slf4j
public class ItemController {

    public record ItemFilter(
        String nome,
        Item.Tipo tipo,
        Item.Raridade raridade,
        Double precoMin,
        Double precoMax
    ) {}

    @Autowired
    private ItemRepository repository;

    @PostMapping
    @CacheEvict(value = "itens", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Item> create(@RequestBody @Valid Item item) {
        try {
            // Verifica se o personagem existe
            if (item.getDono() != null && item.getDono().getId() != null) {
                Personagem dono = repository.findPersonagemById(item.getDono().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Personagem não encontrado"));
                item.setDono(dono); 
            }
    
            log.info("Cadastrando item: {}", item.getNome());
            Item savedItem = repository.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Erro de concorrência ao salvar o item: {}", item.getNome());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    @Cacheable("itens")
    @Operation(description = "Listar todos os itens com filtros e paginação")
    public Page<Item> index(
        ItemFilter filter,
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable
    ) {
        log.info("Buscando itens com filtros: {}", filter);
        var specification = Specifications.withFilters(filter);
        return repository.findAll(specification, pageable);
    }

    @GetMapping("{id}")
    @Operation(description = "Buscar item por ID")
    public Item get(@PathVariable Long id) {
        log.info("Buscando item com ID: {}", id);
        return getItem(id);
    }

    @PutMapping("{id}")
    @CacheEvict(value = "itens", allEntries = true)
    @Operation(description = "Atualizar um item existente")
    public Item update(@PathVariable Long id, @RequestBody @Valid Item item) {
        log.info("Atualizando item com ID: {}", id);
        getItem(id);
        item.setId(id); 
        return repository.save(item);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "itens", allEntries = true)
    @Operation(description = "Excluir um item")
    public void destroy(@PathVariable Long id) {
        log.info("Apagando item com ID: {}", id);
        repository.delete(getItem(id));
    }

    private Item getItem(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
    }
}
