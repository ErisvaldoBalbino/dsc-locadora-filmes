package ifrn.edu.br.locadora_filmes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String titulo;

    @NotNull
    private int ano;

    @NotNull
    private int quantidade_total;

    @NotNull
    @ManyToOne
    private Genero genero;
}
