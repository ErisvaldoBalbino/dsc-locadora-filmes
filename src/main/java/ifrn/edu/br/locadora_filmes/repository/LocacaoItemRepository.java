package ifrn.edu.br.locadora_filmes.repository;

import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Locacao;
import ifrn.edu.br.locadora_filmes.model.LocacaoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocacaoItemRepository extends JpaRepository<LocacaoItem, Long> {

    List<LocacaoItem> findByLocacao(Locacao locacao);

    List<LocacaoItem> findByFilme(Filme filme);

    boolean existsByFilmeId(Long filmeId);
}
