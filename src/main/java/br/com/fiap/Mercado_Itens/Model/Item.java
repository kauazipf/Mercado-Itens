package br.com.fiap.Mercado_Itens.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotNull(message = "Raridade é obrigatória")
    @Enumerated(EnumType.STRING)
    private Raridade raridade;

    @Positive(message = "Preço deve ser positivo")
    private double preco;

    @ManyToOne
    @JoinColumn(name = "personagem_id")
    private Personagem dono;

    @Version // Adiciona controle de concorrência
    private Integer version;

    public enum Tipo {
        ARMA, ARMADURA, POCAO, ACESSORIO
    }

    public enum Raridade {
        COMUM, RARO, EPICO, LENDARIO
    }
}