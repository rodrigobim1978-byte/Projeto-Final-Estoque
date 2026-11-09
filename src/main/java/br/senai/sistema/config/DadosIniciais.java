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
        // Novo bloco: adiciona a Maria de forma isolada, verificando se o login já existe
        if (usuarioRepository.findByLogin("Rodrigo").isEmpty()) {
            usuarioRepository.save(new Usuario("Rodrigo Correia Bim", "Rodrigo",
                    passwordEncoder.encode("rcb123"), Perfil.OPERADOR));
        }

        if (categoriaRepository.count() == 0) {
            Categoria categoria1 = new Categoria("Rações", "Ração Bovina, Milho e Farelo de Soja");
            Categoria categoria2 = new Categoria("Suplementos", "Sal Mineral e Suplemento Proteico");
            Categoria categoria3 = new Categoria("Medicamentos", "Vermifugo e Antibiótico veterinário");
            Categoria categoria4 = new Categoria("Vacinas", "Brucelose, Febre Aftósa e Raiva");
            Categoria categoria5 = new Categoria("Produtos Veterinários", "Seringa, Agulha e Luva");
            Categoria categoria6 = new Categoria("Ferramentas e Equipamentos", "Alicate, Enxada e Martelo");
            Categoria categoria7 = new Categoria("Materiais Manutenção", "Arame Liso e Arame Farpado");
            Categoria categoria8 = new Categoria("Produtos de Patagem", "Herbicida e Semente de Capim");
            categoriaRepository.save(categoria1);
            categoriaRepository.save(categoria2);
            categoriaRepository.save(categoria3);
            categoriaRepository.save(categoria4);
            categoriaRepository.save(categoria5);
            categoriaRepository.save(categoria6);
            categoriaRepository.save(categoria7);
            categoriaRepository.save(categoria8);
        }

        if (produtoRepository.count() == 0) {
            Produto produto1 = new Produto("PRD-001", "Ração Bovina", "SC", new BigDecimal("60.45"), 100, 350);
            Produto produto2 = new Produto("PRD-002", "Milho", "SC", new BigDecimal("53,90"), 10, 8);
            Produto produto3 = new Produto("PRD-003", "Farelo de Soja", "SC", new BigDecimal("45,50"), 20, 42);
            Produto produto4 = new Produto("PRD-004", "Sal Mineral", "SC", new BigDecimal("98,50"), 20, 42);
            Produto produto5 = new Produto("PRD-005", "Suplemento Proteico", "SC", new BigDecimal("89,90"), 10, 42);
            Produto produto6 = new Produto("PRD-006", "Vermifugo", "FR", new BigDecimal("35,00"), 05, 10);
            Produto produto7 = new Produto("PRD-007", "Antibiótico", "FR", new BigDecimal("50,00"), 05, 10);
            Produto produto8 = new Produto("PRD-008", "Febre Aftósa", "DS", new BigDecimal("15,00"), 10, 100);
            Produto produto9 = new Produto("PRD-009", "Raiva", "DS", new BigDecimal("7,50"), 10, 100);
            Produto produto10 = new Produto("PRD-010", "Brucelose", "FR", new BigDecimal("53,50"), 05, 10);
            Produto produto11 = new Produto("PRD-011", "Seringa", "UN", new BigDecimal("1,20"), 05, 10);
            Produto produto12 = new Produto("PRD-012", "Agulha", "UN", new BigDecimal("0,30"), 05, 100);
            Produto produto13 = new Produto("PRD-013", "Alicate", "UN", new BigDecimal("15,00"), 01, 02);
            Produto produto14 = new Produto("PRD-014", "Enxada", "UN", new BigDecimal("27,50"), 01, 03);
            Produto produto15 = new Produto("PRD-015", "Arame Farpado", "RL", new BigDecimal("450,00"), 01, 03);
            Produto produto16 = new Produto("PRD-016", "Arame Liso", "RL", new BigDecimal("430,00"), 01, 03);
            produto1.setLocalizacao("Prateleira A1");
            produto2.setLocalizacao("Armário 2");
            produto3.setLocalizacao("Prateleira B3");
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
            produtoRepository.save(produto4);
            produtoRepository.save(produto5);
            produtoRepository.save(produto6);
            produtoRepository.save(produto7);
            produtoRepository.save(produto8);
            produtoRepository.save(produto9);
            produtoRepository.save(produto10);
            produtoRepository.save(produto11);
            produtoRepository.save(produto12);
            produtoRepository.save(produto13);
            produtoRepository.save(produto14);
            produtoRepository.save(produto15);
            produtoRepository.save(produto16);
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