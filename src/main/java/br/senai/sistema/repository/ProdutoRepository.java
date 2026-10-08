package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.Produto;

/**
 * Acesso ao banco de dados para a entidade Produto.
 *
 * Basta declarar a interface: o Spring Data cria a implementação sozinho,
 * com métodos prontos como findAll, findById, save, deleteById e count.
 * JpaRepository<Produto, Long>  ->  entidade Produto, chave primária do tipo Long.
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}