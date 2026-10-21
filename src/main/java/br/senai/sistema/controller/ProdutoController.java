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

import br.senai.sistema.model.Produto;
import br.senai.sistema.service.CategoriaService;
import br.senai.sistema.service.ProdutoService;
import jakarta.validation.Valid;

/**
 * Controller de Produto: recebe as requisições do navegador para /produtos.
 */
@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;

    public ProdutoController(ProdutoService produtoService,
                              CategoriaService categoriaService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
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
        carregarListas(model);
        return "produtos/form";
    }

    /**
     * POST /produtos/salvar -> recebe o formulário (cadastro ou edição).
     * @Valid confere as regras da entidade; os erros ficam em "resultado".
     */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("produto") Produto produto, BindingResult resultado,
                         Model model, RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            carregarListas(model);
            return "produtos/form";   // volta para o formulário mostrando os erros
        }
        try {
            produtoService.salvar(produto);
        } catch (DataIntegrityViolationException e) {
            // O banco recusou: já existe outro registro com o mesmo código.
            resultado.rejectValue("codigo", "duplicado", "Já existe um registro com este código.");
            carregarListas(model);
            return "produtos/form";
        }
        atributos.addFlashAttribute("mensagem", "Produto salvo com sucesso.");
        return "redirect:/produtos";
    }

    /** GET /produtos/5/editar -> abre o formulário preenchido com o registro de id 5. */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoService.buscarPorId(id));
        carregarListas(model);
        return "produtos/form";
    }

    /** POST /produtos/5/excluir -> exclui o registro de id 5. */
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes atributos) {
        produtoService.excluir(id);
        atributos.addFlashAttribute("mensagem", "Produto excluído com sucesso.");
        return "redirect:/produtos";
    }

    /** Coloca no model as listas usadas nas listas de escolha (select) do formulário. */
    private void carregarListas(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodos());
    }
}