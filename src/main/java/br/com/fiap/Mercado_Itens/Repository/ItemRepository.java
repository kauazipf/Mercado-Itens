package br.com.fiap.Mercado_Itens.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.Mercado_Itens.Model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
