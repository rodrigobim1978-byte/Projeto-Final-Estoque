package br.senai.sistema.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import br.senai.sistema.model.Indicador;
import br.senai.sistema.model.Produto;
import br.senai.sistema.model.TipoMovimentacao;
import br.senai.sistema.repository.MovimentacaoRepository;
import br.senai.sistema.repository.ProdutoRepository;

/**
 * Calcula os indicadores do painel (página inicial).
 * Usa consultas de contagem do Spring Data (count, countBy...) e laços (for) para contar e somar.
 */
@Service
public class PainelService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public PainelService(ProdutoRepository produtoRepository,
                         MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /** Data de hoje no horário de Brasília (o Codespace usa o horário de Londres). */
    private LocalDate hoje() {
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    /** Os indicadores do painel, na ordem em que aparecem. */
    public List<Indicador> indicadores() {
        List<Produto> produtos = produtoRepository.findAll();

        // Percorre a lista para contar e somar (como no aquecimento Java)
        int abaixoDoMinimo = 0;
        BigDecimal valorEmEstoque = BigDecimal.ZERO;
        for (Produto produto : produtos) {
            if (produto.getSaldo() < produto.getEstoqueMinimo()) {
                abaixoDoMinimo = abaixoDoMinimo + 1;
            }
            if (produto.getPrecoCusto() != null) {
                BigDecimal valorDoProduto = produto.getPrecoCusto().multiply(BigDecimal.valueOf(produto.getSaldo()));
                valorEmEstoque = valorEmEstoque.add(valorDoProduto);
            }
        }

        LocalDate inicioDoMes = hoje().withDayOfMonth(1);
        long movimentacoesDoMes = movimentacaoRepository.countByDataBetween(inicioDoMes, hoje());
        NumberFormat dinheiro = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

        List<Indicador> lista = new ArrayList<>();
        lista.add(new Indicador("Produtos", String.valueOf(produtos.size()), "cadastrados", "bi-box-seam", "primary"));
        lista.add(new Indicador("Abaixo do mínimo", String.valueOf(abaixoDoMinimo), "precisam de reposição", "bi-exclamation-triangle", "danger"));
        lista.add(new Indicador("Movimentações do mês", String.valueOf(movimentacoesDoMes), "entradas e saídas", "bi-arrow-left-right", "success"));
        lista.add(new Indicador("Valor em estoque", dinheiro.format(valorEmEstoque), "saldo × preço de custo", "bi-cash-stack", "warning"));
        return lista;
    }

    /** Lista de atenção: os produtos com saldo abaixo do estoque mínimo. */
    public List<Produto> atencao() {
        List<Produto> abaixo = new ArrayList<>();
        for (Produto produto : produtoRepository.findAll()) {
            if (produto.getSaldo() < produto.getEstoqueMinimo()) {
                abaixo.add(produto);
            }
        }
        return abaixo;
    }
}