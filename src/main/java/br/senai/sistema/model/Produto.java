package br.senai.sistema.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade Produto: representa o produto que o sistema guarda.
 *
 * Cada objeto desta classe vira UMA LINHA da tabela "produtos" no banco.
 * Cada atributo vira UMA COLUNA dessa tabela.
 */
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código interno do produto (não se repete). */
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    /** Descrição do produto. */
    @Column(nullable = false, length = 100)
    private String nome;

    /** Unidade de medida (UN, CX, KG, PAR...). */
    @Column(nullable = false, length = 10)
    private String unidade;

    /** Valor pago por unidade. */
    @Column(precision = 10, scale = 2)
    private BigDecimal precoCusto;

    /** Quantidade mínima desejada em estoque. */
    @Column(nullable = false)
    private Integer estoqueMinimo;

    /** Quantidade atual (será atualizada pelas movimentações). */
    @Column(nullable = false)
    private Integer saldo;

    /** Construtor vazio: obrigatório para o JPA criar objetos ao ler do banco. */
    public Produto() {
    }

    /** Construtor com os dados principais: facilita criar objetos no código. */
    public Produto(String codigo, String nome, String unidade, BigDecimal precoCusto, Integer estoqueMinimo, Integer saldo) {
        this.codigo = codigo;
        this.nome = nome;
        this.unidade = unidade;
        this.precoCusto = precoCusto;
        this.estoqueMinimo = estoqueMinimo;
        this.saldo = saldo;
    }

    // ---- Getters e setters: a forma de ler e alterar os atributos privados ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }
}