package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.senai.sistema.model.Produto;
import br.senai.sistema.repository.ProdutoRepository;

/**
 * Camada de serviço de Produto: concentra as operações e, mais adiante, as regras de negócio.
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

    /** Busca um registro pelo id. Se não existir, gera um erro com mensagem clara. */
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
    }

    /** Salva: se o id for vazio, cadastra um novo; se tiver id, atualiza o existente. */
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    /** Exclui pelo id. */
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}