package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.senai.sistema.model.Produto;
import br.senai.sistema.repository.ProdutoRepository;

/**
 * Camada de serviço de Produto: é aqui que ficarão as regras de negócio.
 * Por enquanto, ela só busca os dados no repositório.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Injeção de dependência: o Spring entrega o repositório pronto pelo construtor.
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /** Lista todos os registros, em ordem alfabética. */
    public List<Produto> listarTodos() {
        return produtoRepository.findAll(Sort.by("nome"));
    }
}