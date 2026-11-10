package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.senai.sistema.model.Fornecedor;
import br.senai.sistema.repository.FornecedorRepository;

/**
 * Camada de serviço de Fornecedor: concentra as operações e, mais adiante, as regras de negócio.
 */
@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    // Injeção de dependência: o Spring entrega o repositório pronto pelo construtor.
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    /** Lista todos os registros, em ordem alfabética. */
    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll(Sort.by("nome"));
    }

    /** Busca um registro pelo id. Se não existir, gera um erro com mensagem clara. */
    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado: " + id));
    }

    /** Salva: se o id for vazio, cadastra um novo; se tiver id, atualiza o existente. */
    public Fornecedor salvar(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    /** Exclui pelo id. */
    public void excluir(Long id) {
        fornecedorRepository.deleteById(id);
    }
}