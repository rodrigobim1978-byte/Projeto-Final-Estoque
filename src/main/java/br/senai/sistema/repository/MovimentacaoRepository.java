package br.senai.sistema.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.model.TipoMovimentacao;

/**
 * Acesso ao banco de dados para a entidade Movimentacao.
 *
 * Basta declarar a interface: o Spring Data cria a implementação sozinho,
 * com métodos prontos como findAll, findById, save, deleteById e count.
 * JpaRepository<Movimentacao, Long>  ->  entidade Movimentacao, chave primária do tipo Long.
 */
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Filtro: registros de um tipo, na ordem indicada
    List<Movimentacao> findByTipo(TipoMovimentacao tipo, Sort ordem);
}