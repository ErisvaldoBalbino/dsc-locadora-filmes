package ifrn.edu.br.locadora_filmes.dto.responses;

import lombok.Data;

@Data
public class FilmeResponseDTO {
    private Long id;
    private String titulo;
    private int ano;
    private int quantidade_total;
    private String genero;
}
