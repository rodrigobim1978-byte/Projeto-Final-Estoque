package br.senai.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sistema.model.Fornecedor;

/**
 * Acesso ao banco de dados para a entidade Fornecedor.
 *
 * Basta declarar a interface: o Spring Data cria a implementação sozinho,
 * com métodos prontos como findAll, findById, save, deleteById e count.
 * JpaRepository<Fornecedor, Long>  ->  entidade Fornecedor, chave primária do tipo Long.
 */
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
}