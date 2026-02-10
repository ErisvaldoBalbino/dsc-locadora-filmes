package ifrn.edu.br.locadora_filmes.repository;

import ifrn.edu.br.locadora_filmes.model.Cliente;
import ifrn.edu.br.locadora_filmes.model.Locacao;
import ifrn.edu.br.locadora_filmes.model.StatusLocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    List<Locacao> findByCliente(Cliente cliente);

    List<Locacao> findByClienteId(Long clienteId);

    List<Locacao> findByStatus(StatusLocacao status);

    @Query("SELECT l FROM Locacao l WHERE l.status = 'ATIVA' AND l.data_devolucao_prevista < :dataAtual")
    List<Locacao> findLocacoesAtrasadas(@Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT l FROM Locacao l WHERE l.status = 'ATRASADA'")
    List<Locacao> findByStatusAtrasada();

    boolean existsByClienteIdAndStatus(Long clienteId, StatusLocacao status);
}
