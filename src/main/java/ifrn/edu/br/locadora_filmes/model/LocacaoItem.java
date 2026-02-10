package ifrn.edu.br.locadora_filmes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class LocacaoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Locacao locacao;

    @NotNull
    @ManyToOne
    private Filme filme;
}
