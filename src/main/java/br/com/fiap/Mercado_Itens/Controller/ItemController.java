// ItemController atualizado com Specification e filtro completo
package br.com.fiap.Mercado_Itens.Controller;

import br.com.fiap.Mercado_Itens.Model.Item;
import br.com.fiap.Mercado_Itens.Repository.ItemRepository;
import br.com.fiap.Mercado_Itens.Specification.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public Page<Item> index(
        ItemFilter filter,
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable
    ) {
        var specification = Specifications.withFilters(filter);
        return repository.findAll(specification, pageable); 
    }
}