package br.senai.sistema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.service.MovimentacaoService;
import br.senai.sistema.service.ProdutoService;
import jakarta.validation.Valid;

/**
 * Controller de Movimentacao: recebe as requisições do navegador para /movimentacoes.
 */
@Controller
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final ProdutoService produtoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService,
                                   ProdutoService produtoService) {
        this.movimentacaoService = movimentacaoService;
        this.produtoService = produtoService;
    }

    /** GET /movimentacoes -> mostra a lista. */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimentacoes", movimentacaoService.listarTodos());
        return "movimentacoes/lista";
    }

    /** GET /movimentacoes/novo -> abre o formulário vazio. */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("movimentacao", new Movimentacao());
        carregarListas(model);
        return "movimentacoes/form";
    }

    /**
     * POST /movimentacoes/salvar -> recebe o formulário (cadastro ou edição).
     * @Valid confere as regras da entidade; os erros ficam em "resultado".
     */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("movimentacao") Movimentacao movimentacao, BindingResult resultado,
                         Model model, RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            carregarListas(model);
            return "movimentacoes/form";   // volta para o formulário mostrando os erros
        }
        movimentacaoService.salvar(movimentacao);
        atributos.addFlashAttribute("mensagem", "Movimentação salva com sucesso.");
        return "redirect:/movimentacoes";
    }

    /** GET /movimentacoes/5/editar -> abre o formulário preenchido com o registro de id 5. */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("movimentacao", movimentacaoService.buscarPorId(id));
        carregarListas(model);
        return "movimentacoes/form";
    }

    /** POST /movimentacoes/5/excluir -> exclui o registro de id 5. */
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes atributos) {
        movimentacaoService.excluir(id);
        atributos.addFlashAttribute("mensagem", "Movimentação excluída com sucesso.");
        return "redirect:/movimentacoes";
    }

    /** Coloca no model as listas usadas nas listas de escolha (select) do formulário. */
    private void carregarListas(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
    }
}