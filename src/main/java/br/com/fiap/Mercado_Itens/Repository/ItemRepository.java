package br.com.fiap.Mercado_Itens.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.Mercado_Itens.Model.Item;
import br.com.fiap.Mercado_Itens.Model.Personagem;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    @Query("SELECT p FROM Personagem p WHERE p.id = :id")
    Optional<Personagem> findPersonagemById(@Param("id") Long id);
}