package br.com.fiap.Mercado_Itens.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Classe é obrigatória")
    @Enumerated(EnumType.STRING)
    private Classe characterClass;

    @Min(value = 1, message = "Nível mínimo é 1")
    @Max(value = 99, message = "Nível máximo é 99")
    private int level;

    @PositiveOrZero(message = "Moedas não podem ser negativas")
    private double coins;

    public enum Classe {
        GUERREIRO, MAGO, ARQUEIRO
    }
}