package br.senai.sistema.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.Fornecedor;
import br.senai.sistema.service.FornecedorService;
import jakarta.validation.Valid;

/**
 * Controller de Fornecedor: recebe as requisições do navegador para /fornecedores.
 */
@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    /** GET /fornecedores -> mostra a lista. */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
        return "fornecedores/lista";
    }

    /** GET /fornecedores/novo -> abre o formulário vazio. */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("fornecedor", new Fornecedor());
        return "fornecedores/form";
    }

    /**
     * POST /fornecedores/salvar -> recebe o formulário (cadastro ou edição).
     * @Valid confere as regras da entidade; os erros ficam em "resultado".
     */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("fornecedor") Fornecedor fornecedor, BindingResult resultado,
                         RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            return "fornecedores/form";   // volta para o formulário mostrando os erros
        }
        fornecedorService.salvar(fornecedor);
        atributos.addFlashAttribute("mensagem", "Fornecedor salvo com sucesso.");
        return "redirect:/fornecedores";
    }

    /** GET /fornecedores/5/editar -> abre o formulário preenchido com o registro de id 5. */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("fornecedor", fornecedorService.buscarPorId(id));
        return "fornecedores/form";
    }

    /**
     * POST /fornecedores/5/excluir -> exclui o registro de id 5.
     * Se houver registros ligados a ele, o banco recusa a exclusão (integridade referencial)
     * e o sistema mostra um aviso, em vez de uma página de erro.
     */
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            fornecedorService.excluir(id);
            atributos.addFlashAttribute("mensagem", "Fornecedor excluído com sucesso.");
        } catch (DataIntegrityViolationException e) {
            atributos.addFlashAttribute("erro", "Não é possível excluir: existem produtos ligados a este fornecedor.");
        }
        return "redirect:/fornecedores";
    }
}