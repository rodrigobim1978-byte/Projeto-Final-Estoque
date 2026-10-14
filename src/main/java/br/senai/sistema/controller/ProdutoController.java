package br.senai.sistema.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.Produto;
import br.senai.sistema.service.ProdutoService;
import jakarta.validation.Valid;

/**
 * Controller de Produto: recebe as requisições do navegador para /produtos.
 */
@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /** GET /produtos -> mostra a lista. */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produtos/lista";
    }

    /** GET /produtos/novo -> abre o formulário vazio. */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "produtos/form";
    }

    /**
     * POST /produtos/salvar -> recebe o formulário (cadastro ou edição).
     * @Valid confere as regras da entidade; os erros ficam em "resultado".
     */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("produto") Produto produto, BindingResult resultado,
                         RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            return "produtos/form";   // volta para o formulário mostrando os erros
        }
        try {
            produtoService.salvar(produto);
        } catch (DataIntegrityViolationException e) {
            // O banco recusou: já existe outro registro com o mesmo código.
            resultado.rejectValue("codigo", "duplicado", "Já existe um registro com este código.");
            return "produtos/form";
        }
        atributos.addFlashAttribute("mensagem", "Produto salvo com sucesso.");
        return "redirect:/produtos";
    }
}