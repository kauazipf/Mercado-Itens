package br.com.fiap.Mercado_Itens.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.Mercado_Itens.Model.Personagem;

public interface PersonagemRepository extends JpaRepository<Personagem, Long> {

}
