package br.senai.sistema.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.model.Produto;
import br.senai.sistema.model.TipoMovimentacao;
import br.senai.sistema.repository.MovimentacaoRepository;
import br.senai.sistema.repository.ProdutoRepository;

/**
 * Camada de serviço de Movimentacao: concentra as operações e as REGRAS DE NEGÓCIO.
 * Quando uma regra não é cumprida, o método lança RegraNegocioException com a mensagem para o usuário.
 */
@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    // Injeção de dependência: o Spring entrega os repositórios prontos pelo construtor.
    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
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

    /**
     * Registra uma movimentação e atualiza o saldo do produto, conferindo as regras de negócio.
     * @Transactional: o saldo e a movimentação são gravados juntos (ou nenhum dos dois).
     */
    @Transactional
    public Movimentacao salvar(Movimentacao movimentacao) {
        if (movimentacao.getId() != null) {                                               // regra 3
            throw new RegraNegocioException("Movimentação registrada não pode ser alterada. Para corrigir, registre uma movimentação contrária.");
        }
        Produto produto = produtoRepository.findById(movimentacao.getProduto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        int quantidade = movimentacao.getQuantidade();
        if (movimentacao.getTipo() == TipoMovimentacao.SAIDA) {
            if (quantidade > produto.getSaldo()) {                                 // regra 1
                throw new RegraNegocioException("Saída maior que o saldo: " + produto.getNome() + " tem apenas "
                        + produto.getSaldo() + " " + produto.getUnidade() + " em estoque.");
            }
            produto.setSaldo(produto.getSaldo() - quantidade);
        } else {
            produto.setSaldo(produto.getSaldo() + quantidade);
        }
        produtoRepository.save(produto);
        movimentacao.setProduto(produto);
        return movimentacaoRepository.save(movimentacao);
    }

    /** Regra de negócio 2 (alerta): o saldo do produto está abaixo do estoque mínimo? */
    public boolean abaixoDoMinimo(Produto produto) {
        return produto.getSaldo() < produto.getEstoqueMinimo();
    }
}