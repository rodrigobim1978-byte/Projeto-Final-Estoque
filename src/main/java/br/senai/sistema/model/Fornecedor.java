package br.senai.sistema.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidade Fornecedor: representa o fornecedor que o sistema guarda.
 *
 * Cada objeto desta classe vira UMA LINHA da tabela "fornecedores" no banco.
 * As anotações de validação (@NotBlank, @Size...) definem as regras dos campos do formulário.
 */
@Entity
@Table(name = "fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Razão social ou nome do fornecedor. */
    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 100, message = "Use no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    /** CNPJ do fornecedor (opcional). */
    @Size(max = 18, message = "Use no máximo 18 caracteres.")
    @Column(length = 18)
    private String cnpj;

    /** Telefone para contato (opcional). */
    @Size(max = 20, message = "Use no máximo 20 caracteres.")
    @Column(length = 20)
    private String telefone;

    /** Construtor vazio: obrigatório para o JPA e para o formulário criarem objetos. */
    public Fornecedor() {
    }

    /** Construtor com os dados principais: facilita criar objetos no código. */
    public Fornecedor(String nome, String cnpj, String telefone) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
    }

    // ---- Getters e setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}