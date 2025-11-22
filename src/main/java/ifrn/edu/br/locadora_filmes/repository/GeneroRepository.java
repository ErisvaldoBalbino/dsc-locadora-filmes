package ifrn.edu.br.locadora_filmes.repository;

import ifrn.edu.br.locadora_filmes.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    
    Optional<Genero> findByNome(String nome);
    
    boolean existsByNome(String nome);
}

