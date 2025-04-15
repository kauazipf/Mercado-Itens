// Classe Specification para filtro de itens e personagens
package br.com.fiap.Mercado_Itens.Specification;

import br.com.fiap.Mercado_Itens.Controller.ItemController.ItemFilter;
import br.com.fiap.Mercado_Itens.Model.Item;
import br.com.fiap.Mercado_Itens.Model.Personagem;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {

    public static Specification<Item> withFilters(ItemFilter filter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (filter.nome() != null && !filter.nome().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%%" + filter.nome().toLowerCase() + "%%"));
            }

            if (filter.tipo() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("tipo"), filter.tipo()));
            }

            if (filter.raridade() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("raridade"), filter.raridade()));
            }

            if (filter.precoMin() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("preco"), filter.precoMin()));
            }

            if (filter.precoMax() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("preco"), filter.precoMax()));
            }

            return predicate;
        };
    }

    public static Specification<Personagem> comNomeOuClasse(String nome, Personagem.Classe classe) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (nome != null && !nome.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("nome")), "%%" + nome.toLowerCase() + "%%"));
            }

            if (classe != null) {
                predicate = cb.and(predicate, cb.equal(root.get("classe"), classe));
            }

            return predicate;
        };
    }
}