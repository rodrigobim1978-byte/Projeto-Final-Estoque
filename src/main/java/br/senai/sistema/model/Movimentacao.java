package br.senai.sistema.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Entidade Movimentacao: representa a movimentação que o sistema guarda.
 *
 * Cada objeto desta classe vira UMA LINHA da tabela "movimentacoes" no banco.
 * As anotações de validação (@NotBlank, @Size...) definem as regras dos campos do formulário.
 * Os atributos com @ManyToOne guardam, no banco, só o id do registro relacionado (chave estrangeira).
 */
@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo da movimentação: entrada ou saída. */
    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 10, message = "Use no máximo 10 caracteres.")
    @Column(nullable = false, length = 10)
    private String tipo;

    /** Quantidade movimentada. */
    @NotNull(message = "Campo obrigatório.")
    @Positive(message = "Informe um valor maior que zero.")
    @Column(nullable = false)
    private Integer quantidade;

    /** Dia da movimentação. */
    @NotNull(message = "Campo obrigatório.")
    @PastOrPresent(message = "A data não pode ser futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate data;

    /** Observação (opcional). */
    @Size(max = 255, message = "Use no máximo 255 caracteres.")
    @Column(length = 255)
    private String observacao;

    /** Produto movimentado: relacionamento N:1 (muitos registros daqui apontam para um Produto). */
    @NotNull(message = "Selecione um produto.")
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /** Construtor vazio: obrigatório para o JPA e para o formulário criarem objetos. */
    public Movimentacao() {
    }

    /** Construtor com os dados principais: facilita criar objetos no código. */
    public Movimentacao(String tipo, Integer quantidade, LocalDate data, Produto produto) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
        this.produto = produto;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}