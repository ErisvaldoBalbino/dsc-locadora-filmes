package ifrn.edu.br.locadora_filmes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Locacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne
    private Cliente cliente;

    @NotNull(message = "Data de locação é obrigatória")
    private LocalDate data_locacao;

    @NotNull(message = "Data de devolução prevista é obrigatória")
    private LocalDate data_devolucao_prevista;
    
    private LocalDate data_devolucao_real;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusLocacao status;

    @OneToMany(mappedBy = "locacao")
    private List<LocacaoItem> itens;
}
