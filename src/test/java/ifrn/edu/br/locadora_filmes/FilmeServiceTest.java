package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ifrn.edu.br.locadora_filmes.service.FilmeService;
import ifrn.edu.br.locadora_filmes.repository.FilmeRepository;
import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.FilmeCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.FilmeUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.FilmeResponseDTO;
import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Genero;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FilmeServiceTest {
    
    @InjectMocks
    private FilmeService filmeService;

    @Mock
    private GeneroRepository generoRepository;

    @Mock
    private FilmeRepository filmeRepository;

    @Test
    void testSalvarFilme_ShouldPass() {
        FilmeCreateDTO filmeDTO = new FilmeCreateDTO();

        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");

        filmeDTO.setTitulo("Filme teste");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(1L);

        Filme filmeSalvo = new Filme();
        filmeSalvo.setId(1L);
        filmeSalvo.setTitulo("Filme teste");
        filmeSalvo.setAno(2025);
        filmeSalvo.setQuantidade_total(10);
        filmeSalvo.setGenero(genero);

        Mockito.when(filmeRepository.existsByTitulo("Filme teste")).thenReturn(false);
        Mockito.when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        Mockito.when(filmeRepository.save(Mockito.any(Filme.class))).thenReturn(filmeSalvo);

        FilmeResponseDTO resultado = filmeService.salvar(filmeDTO);

        assertNotNull(resultado);
        assertEquals("Filme teste", resultado.getTitulo());
        assertEquals(2025, resultado.getAno());
        assertEquals(10, resultado.getQuantidade_total());
        assertEquals("Ação", resultado.getGenero());
    }

    @Test
    void testSalvarFilme_ShouldFail_TituloJaExistente() {
        FilmeCreateDTO filmeDTO = new FilmeCreateDTO();
        filmeDTO.setTitulo("Filme teste");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(1L);
        
        Mockito.when(filmeRepository.existsByTitulo("Filme teste")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> filmeService.salvar(filmeDTO));
    }

    @Test
    void testAtualizarFilme_ShouldPass() {
        long filmeId = 1L;

        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");

        FilmeUpdateDTO filmeDTO = new FilmeUpdateDTO();
        filmeDTO.setTitulo("Filme atualizado");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(1L);

        Filme filmeExistente = new Filme();
        filmeExistente.setId(1L);
        filmeExistente.setTitulo("Filme antigo"); 
        filmeExistente.setAno(2020);
        filmeExistente.setQuantidade_total(5);
        filmeExistente.setGenero(genero);

        Filme filmeSalvo = new Filme();
        filmeSalvo.setId(1L);
        filmeSalvo.setTitulo("Filme atualizado");
        filmeSalvo.setAno(2025);
        filmeSalvo.setQuantidade_total(10);
        filmeSalvo.setGenero(genero);

        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.of(filmeExistente));
        Mockito.when(filmeRepository.existsByTitulo("Filme atualizado")).thenReturn(false);
        Mockito.when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        Mockito.when(filmeRepository.save(Mockito.any(Filme.class))).thenReturn(filmeSalvo);

        FilmeResponseDTO resultado = filmeService.atualizar(filmeId, filmeDTO);

        assertNotNull(resultado);
        assertEquals("Filme atualizado", resultado.getTitulo());
        assertEquals(2025, resultado.getAno());
        assertEquals(10, resultado.getQuantidade_total());
    }

    @Test
    void testAtualizarFilme_ShouldFail_TituloJaExistente() {
        long filmeId = 1L;

        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");

        FilmeUpdateDTO filmeDTO = new FilmeUpdateDTO();
        filmeDTO.setTitulo("Filme duplicado");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(1L);

        Filme filmeExistente = new Filme();
        filmeExistente.setId(filmeId);
        filmeExistente.setTitulo("Filme antigo");
        filmeExistente.setAno(2020);
        filmeExistente.setGenero(genero);

        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.of(filmeExistente));
        Mockito.when(filmeRepository.existsByTitulo("Filme duplicado")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> filmeService.atualizar(filmeId, filmeDTO));
    }

    @Test
    void testAtualizarFilme_ShouldFail_GeneroNaoEncontrado() {
        long filmeId = 1L;

        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");

        FilmeUpdateDTO filmeDTO = new FilmeUpdateDTO();
        filmeDTO.setTitulo("Filme atualizado");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(999L);

        Filme filmeExistente = new Filme();
        filmeExistente.setId(filmeId);
        filmeExistente.setTitulo("Filme antigo");
        filmeExistente.setGenero(genero);

        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.of(filmeExistente));
        Mockito.when(filmeRepository.existsByTitulo("Filme atualizado")).thenReturn(false);
        Mockito.when(generoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> filmeService.atualizar(filmeId, filmeDTO));
    }

    @Test
    void testAtualizarFilme_ShouldFail_FilmeNaoEncontrado() {
        long filmeId = 999L;

        FilmeUpdateDTO filmeDTO = new FilmeUpdateDTO();
        filmeDTO.setTitulo("Filme atualizado");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(1L);

        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> filmeService.atualizar(filmeId, filmeDTO));
    }

    @Test
    void testSalvarFilme_ShouldFail_GeneroNaoEncontrado() {
        FilmeCreateDTO filmeDTO = new FilmeCreateDTO();
        filmeDTO.setTitulo("Filme teste");
        filmeDTO.setAno(2025);
        filmeDTO.setQuantidade_total(10);
        filmeDTO.setGeneroId(1L);

        Mockito.when(generoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> filmeService.salvar(filmeDTO));
    }

    @Test
    void testBuscarPorId_ShouldPass() {
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

    @Test
    void testBuscarPorId_ShouldFail_FilmeNaoEncontrado() {
        long filmeId = 1L;
        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> filmeService.buscarPorId(filmeId));
    }

    @Test
    void testBuscarTodos_ShouldPass() {
        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");

        Filme filme1 = new Filme();
        filme1.setId(1L);
        filme1.setTitulo("Filme 1");
        filme1.setAno(2025);
        filme1.setQuantidade_total(10);
        filme1.setGenero(genero);

        Filme filme2 = new Filme();
        filme2.setId(2L);
        filme2.setTitulo("Filme 2");
        filme2.setAno(2024);
        filme2.setQuantidade_total(5);
        filme2.setGenero(genero);

        Mockito.when(filmeRepository.findAll()).thenReturn(List.of(filme1, filme2));

        List<FilmeResponseDTO> resultado = filmeService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Filme 1", resultado.get(0).getTitulo());
        assertEquals("Filme 2", resultado.get(1).getTitulo());
        assertEquals("Ação", resultado.get(0).getGenero());
        assertEquals("Ação", resultado.get(1).getGenero());
    }

    @Test
    void testBuscarTodos_ShouldReturnEmptyList() {
        Mockito.when(filmeRepository.findAll()).thenReturn(List.of());

        List<FilmeResponseDTO> resultado = filmeService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    void testDeletar_ShouldPass() {
        long filmeId = 1L;

        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");

        Filme filme = new Filme();
        filme.setId(filmeId);
        filme.setTitulo("Filme teste");
        filme.setAno(2025);
        filme.setQuantidade_total(10);
        filme.setGenero(genero);

        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.of(filme));
        Mockito.doNothing().when(filmeRepository).delete(filme);

        filmeService.deletar(filmeId);

        Mockito.verify(filmeRepository, Mockito.times(1)).delete(filme);
    }

    @Test
    void testDeletar_ShouldFail_FilmeNaoEncontrado() {
        long filmeId = 999L;
        Mockito.when(filmeRepository.findById(filmeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> filmeService.deletar(filmeId));
    }
}
