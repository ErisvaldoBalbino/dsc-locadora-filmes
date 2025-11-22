package ifrn.edu.br.locadora_filmes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nome do gênero é obrigatório")
    @Size(min = 1, max = 50, message = "Nome do gênero deve ter entre 1 e 50 caracteres")
    private String nome;

    @OneToMany(mappedBy = "genero")
    private List<Filme> filmes;
}
