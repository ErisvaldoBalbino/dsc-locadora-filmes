package ifrn.edu.br.locadora_filmes.repository;

import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    
    List<Filme> findByGenero(Genero genero);
    
    List<Filme> findByTituloContainingIgnoreCase(String titulo);
    
    List<Filme> findByAno(int ano);
    
    @Query("SELECT f FROM Filme f WHERE f.quantidade_total > 0")
    List<Filme> findFilmesDisponiveis();
    
    @Query("SELECT f FROM Filme f WHERE f.genero = :genero")
    List<Filme> findByGeneroId(@Param("genero") Genero genero);
    
    Optional<Filme> findByTituloAndAno(String titulo, int ano);
}

