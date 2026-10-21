package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.repository.MovimentacaoRepository;

/**
 * Camada de serviço de Movimentacao: concentra as operações e, mais adiante, as regras de negócio.
 */
@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;

    // Injeção de dependência: o Spring entrega o repositório pronto pelo construtor.
    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /** Lista todos os registros, em ordem de data. */
    public List<Movimentacao> listarTodos() {
        return movimentacaoRepository.findAll(Sort.by("data"));
    }

    /** Busca um registro pelo id. Se não existir, gera um erro com mensagem clara. */
    public Movimentacao buscarPorId(Long id) {
        return movimentacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimentação não encontrada: " + id));
    }

    /** Salva: se o id for vazio, cadastra um novo; se tiver id, atualiza o existente. */
    public Movimentacao salvar(Movimentacao movimentacao) {
        return movimentacaoRepository.save(movimentacao);
    }

    /** Exclui pelo id. */
    public void excluir(Long id) {
        movimentacaoRepository.deleteById(id);
    }
}