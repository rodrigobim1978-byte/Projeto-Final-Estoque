package br.senai.sistema.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidade Movimentacao: representa as entradas e saídas
 * de produtos do estoque da fazenda agropecuária.
 *
 * Cada objeto desta classe vira UMA LINHA da tabela
 * "movimentacoes" no banco de dados.
 * Cada atributo vira UMA COLUNA dessa tabela.
 */
@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo da movimentação: ENTRADA ou SAIDA. */
    @Column(nullable = false, length = 10)
    private String tipo;

    /** Quantidade de produtos movimentada. */
    @Column(nullable = false)
    private Integer quantidade;

    /** Data e hora em que a movimentação foi realizada. */
    @Column(nullable = false)
    private LocalDateTime dataHora;

    /** Motivo da movimentação, como compra ou uso na fazenda. */
    @Column(length = 200)
    private String observacao;

    /** Valor de custo por unidade do produto. */
    @Column(precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    /** Valor total da movimentação. */
    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

    /** Produto relacionado à movimentação. */
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /** Construtor vazio: obrigatório para o JPA criar objetos ao ler do banco. */
    public Movimentacao() {
    }

    /** Construtor com os dados principais da movimentação. */
    public Movimentacao(
            String tipo,
            Integer quantidade,
            LocalDateTime dataHora,
            String observacao,
            BigDecimal valorUnitario,
            Produto produto) {

        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
        this.observacao = observacao;
        this.valorUnitario = valorUnitario;
        this.produto = produto;

        if (quantidade != null && valorUnitario != null) {
            this.valorTotal = valorUnitario.multiply(
                BigDecimal.valueOf(quantidade)
            );
        }
    }

    // ---- Getters e setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
