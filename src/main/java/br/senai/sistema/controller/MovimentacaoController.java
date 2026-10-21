package br.senai.sistema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.model.TipoMovimentacao;
import br.senai.sistema.service.MovimentacaoService;
import br.senai.sistema.service.ProdutoService;
import br.senai.sistema.service.RegraNegocioException;
import jakarta.validation.Valid;

/**
 * Controller de Movimentacao: recebe as requisições do navegador para /movimentacoes.
 * Os erros de regra de negócio (RegraNegocioException) viram o aviso vermelho da tela.
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

    /** GET /movimentacoes -> mostra a lista. Com ?tipo=..., mostra só os registros daquele tipo. */
    @GetMapping
    public String listar(@RequestParam(required = false) TipoMovimentacao tipo, Model model) {
        model.addAttribute("movimentacoes", movimentacaoService.listar(tipo));
        model.addAttribute("tipos", TipoMovimentacao.values());   // opções do filtro
        model.addAttribute("tipo", tipo);                  // mantém a opção escolhida no filtro
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
     * POST /movimentacoes/salvar -> recebe o formulário.
     * Primeiro as validações dos campos (@Valid); depois, no service, as regras de negócio.
     */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("movimentacao") Movimentacao movimentacao, BindingResult resultado,
                         Model model, RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            carregarListas(model);
            return "movimentacoes/form";   // volta para o formulário mostrando os erros dos campos
        }
        try {
            Movimentacao salva = movimentacaoService.salvar(movimentacao);
            String texto = "Movimentação registrada com sucesso.";
            if (movimentacaoService.abaixoDoMinimo(salva.getProduto())) {                          // regra 2: alerta
                texto += " Atenção: o saldo de " + salva.getProduto().getNome() + " está abaixo do estoque mínimo.";
            }
            atributos.addFlashAttribute("mensagem", texto);
        } catch (RegraNegocioException e) {
            model.addAttribute("erro", e.getMessage());   // aparece no aviso vermelho, acima do formulário
            carregarListas(model);
            return "movimentacoes/form";
        }
        return "redirect:/movimentacoes";
    }

    /** Coloca no model as listas usadas nas listas de escolha (select) do formulário. */
    private void carregarListas(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("tipos", TipoMovimentacao.values());
    }
}