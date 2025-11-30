package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ifrn.edu.br.locadora_filmes.service.FilmeService;
import ifrn.edu.br.locadora_filmes.service.GeneroService;
import ifrn.edu.br.locadora_filmes.repository.FilmeRepository;
import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Genero;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FilmeServiceTest {
    
    @InjectMocks
    private FilmeService filmeService;

    @Mock
    private FilmeRepository filmeRepository;

    @Test
    void getFilmeById() {
        long filmeId = 1L;

        Genero mockGenero = new Genero();
        mockGenero.setId(1L);
        mockGenero.setNome("Ação");

        Filme mockFilme = new Filme();
        mockFilme.setId(filmeId);
        mockFilme.setTitulo("Filme teste");
        mockFilme.setAno(2025);
        mockFilme.setGenero(mockGenero);
        mockFilme.setQuantidade_total(10);

        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.of(mockFilme));

        Filme resultado = filmeService.buscarPorId(filmeId);


        assertNotNull(resultado);
        assertEquals("Filme teste", resultado.getTitulo());
        assertEquals(2025, resultado.getAno());
        assertEquals(mockGenero, resultado.getGenero());
        assertEquals(10, resultado.getQuantidade_total());

    }
}
