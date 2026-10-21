package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.Movimentacao;

/**
 * Acesso ao banco de dados para a entidade Movimentacao.
 *
 * Basta declarar a interface: o Spring Data cria a implementação sozinho,
 * com métodos prontos como findAll, findById, save, deleteById e count.
 * JpaRepository<Movimentacao, Long>  ->  entidade Movimentacao, chave primária do tipo Long.
 */
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}