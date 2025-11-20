package ifrn.edu.br.locadora_filmes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Locacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Cliente cliente;

    @NotNull
    private LocalDate data_locacao;

    @NotNull
    private LocalDate data_devolucao_prevista;
    private LocalDate data_devolucao_real; // pode ser null pq nao sei quando vai ser devolvido
    // tambem pode ser usado para saber se esta atrasada comparando a data_devolucao_prevista e real

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusLocacao status;

    @OneToMany(mappedBy = "locacao")
    private List<LocacaoItem> itens;
}
