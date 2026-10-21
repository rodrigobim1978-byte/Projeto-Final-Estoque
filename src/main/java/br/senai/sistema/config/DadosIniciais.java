package br.senai.sistema.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.senai.sistema.model.Categoria;
import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.model.Perfil;
import br.senai.sistema.model.Produto;
import br.senai.sistema.model.TipoMovimentacao;
import br.senai.sistema.model.Usuario;
import br.senai.sistema.repository.CategoriaRepository;
import br.senai.sistema.repository.MovimentacaoRepository;
import br.senai.sistema.repository.ProdutoRepository;
import br.senai.sistema.repository.UsuarioRepository;

/**
 * Carga inicial de dados: executa uma vez sempre que a aplicação sobe.
 * Cada bloco só insere os dados se a tabela ainda estiver vazia (por isso não duplica).
 */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public DadosIniciais(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                         ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository,
                         MovimentacaoRepository movimentacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario("Administrador", "admin",
                    passwordEncoder.encode("admin123"), Perfil.ADMIN));
            usuarioRepository.save(new Usuario("Operador", "operador",
                    passwordEncoder.encode("operador123"), Perfil.OPERADOR));
        }

        if (categoriaRepository.count() == 0) {
            Categoria categoria1 = new Categoria("Ferragens", "Parafusos, porcas e fixadores");
            Categoria categoria2 = new Categoria("EPI", "Equipamentos de proteção individual");
            Categoria categoria3 = new Categoria("Elétrica", "Fios, fitas e materiais elétricos");
            categoriaRepository.save(categoria1);
            categoriaRepository.save(categoria2);
            categoriaRepository.save(categoria3);
        }

        if (produtoRepository.count() == 0) {
            Produto produto1 = new Produto("PRD-001", "Parafuso sextavado M8", "UN", new BigDecimal("0.45"), 100, 350);
            Produto produto2 = new Produto("PRD-002", "Luva de proteção", "PAR", new BigDecimal("12.90"), 10, 8);
            Produto produto3 = new Produto("PRD-003", "Fita isolante 20m", "UN", new BigDecimal("7.50"), 20, 42);
            // Cada produto recebe uma categoria (relacionamento N:1)
            List<Categoria> categorias = categoriaRepository.findAll(Sort.by("id"));
            if (!categorias.isEmpty()) {
                produto1.setCategoria(categorias.get(0));
                produto2.setCategoria(categorias.get(1 % categorias.size()));
                produto3.setCategoria(categorias.get(2 % categorias.size()));
            }
            produtoRepository.save(produto1);
            produtoRepository.save(produto2);
            produtoRepository.save(produto3);
        }

        // Operação principal: cada registro aponta para registros que já existem (relacionamento).
        // O "%" (resto da divisão) evita erro se existirem menos de 3 registros cadastrados.
        if (movimentacaoRepository.count() == 0) {
            List<Produto> produtos = produtoRepository.findAll(Sort.by("id"));
            if (!produtos.isEmpty()) {
                Movimentacao movimentacao1 = new Movimentacao(TipoMovimentacao.ENTRADA, 200, LocalDate.of(2026, 10, 14),
                        produtos.get(0));
                movimentacao1.setObservacao("Compra do fornecedor");
                Movimentacao movimentacao2 = new Movimentacao(TipoMovimentacao.SAIDA, 2, LocalDate.of(2026, 10, 15),
                        produtos.get(1 % produtos.size()));
                movimentacao2.setObservacao("Retirada pela equipe de manutenção");
                Movimentacao movimentacao3 = new Movimentacao(TipoMovimentacao.ENTRADA, 10, LocalDate.of(2026, 10, 16),
                        produtos.get(2 % produtos.size()));
                movimentacaoRepository.save(movimentacao1);
                movimentacaoRepository.save(movimentacao2);
                movimentacaoRepository.save(movimentacao3);
            }
        }
    }
}