package br.senai.sistema.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Entidade Produto: representa o produto que o sistema guarda.
 *
 * Cada objeto desta classe vira UMA LINHA da tabela "produtos" no banco.
 * As anotações de validação (@NotBlank, @Size...) definem as regras dos campos do formulário.
 * Os atributos com @ManyToOne guardam, no banco, só o id do registro relacionado (chave estrangeira).
 */
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código interno do produto (não se repete). */
    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 20, message = "Use no máximo 20 caracteres.")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    /** Descrição do produto. */
    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 100, message = "Use no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    /** Unidade de medida (UN, CX, KG, PAR...). */
    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 10, message = "Use no máximo 10 caracteres.")
    @Column(nullable = false, length = 10)
    private String unidade;

    /** Valor pago por unidade. */
    @DecimalMin(value = "0.00", message = "O valor não pode ser negativo.")
    @Column(precision = 10, scale = 2)
    private BigDecimal precoCusto;

    /** Quantidade mínima desejada em estoque. */
    @NotNull(message = "Campo obrigatório.")
    @PositiveOrZero(message = "O valor não pode ser negativo.")
    @Column(nullable = false)
    private Integer estoqueMinimo;

    /** Quantidade atual (será atualizada pelas movimentações). */
    @NotNull(message = "Campo obrigatório.")
    @PositiveOrZero(message = "O valor não pode ser negativo.")
    @Column(nullable = false)
    private Integer saldo;

    /** Categoria do produto (opcional): relacionamento N:1 (muitos registros daqui apontam para uma Categoria). */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /** Construtor vazio: obrigatório para o JPA e para o formulário criarem objetos. */
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

    // ---- Getters e setters ----

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}