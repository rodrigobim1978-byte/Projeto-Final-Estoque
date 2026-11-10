package br.senai.sistema.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.senai.sistema.model.Categoria;
import br.senai.sistema.model.Fornecedor;
import br.senai.sistema.model.Movimentacao;
import br.senai.sistema.model.Perfil;
import br.senai.sistema.model.Produto;
import br.senai.sistema.model.TipoMovimentacao;
import br.senai.sistema.model.Usuario;
import br.senai.sistema.repository.CategoriaRepository;
import br.senai.sistema.repository.FornecedorRepository;
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
    private final FornecedorRepository fornecedorRepository;

    public DadosIniciais(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                         ProdutoRepository produtoRepository,
                         CategoriaRepository categoriaRepository,
                         MovimentacaoRepository movimentacaoRepository,
                         FornecedorRepository fornecedorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario("Administrador", "admin",
                    passwordEncoder.encode("admin123"), Perfil.ADMIN));
            usuarioRepository.save(new Usuario("Almoxarife", "operador",
                    passwordEncoder.encode("operador123"), Perfil.ALMOXARIFE));
        }

        if (categoriaRepository.count() == 0) {

            categoriaRepository.save(new Categoria(
                    "Alimentação Animal",
                    "Rações, milho, farelo e suplementos"
            ));

            categoriaRepository.save(new Categoria(
                    "Medicamentos Veterinários",
                    "Medicamentos e produtos para saúde do gado"
            ));

            categoriaRepository.save(new Categoria(
                    "Vacinas",
                    "Vacinas para prevenção de doenças bovinas"
            ));

            categoriaRepository.save(new Categoria(
                    "Ferramentas e Materiais",
                    "Ferramentas e materiais utilizados na fazenda"
            ));
        }

        // 3. CADASTRO DOS PRODUTOS
        if (produtoRepository.count() == 0) {

            List<Categoria> categorias =
                    categoriaRepository.findAll(Sort.by("id"));

            if (categorias.size() < 4) {
                throw new IllegalStateException(
                        "Cadastre as quatro categorias antes dos produtos."
                );
            }

            Categoria alimentacao = categorias.get(0);
            Categoria medicamentos = categorias.get(1);
            Categoria vacinas = categorias.get(2);
            Categoria ferramentas = categorias.get(3);

            // ALIMENTAÇÃO ANIMAL
            Produto produto1 = new Produto(
                    "PRD-001", "Ração Bovina", "SC",
                    new BigDecimal("60.45"), 100, 350);

            Produto produto2 = new Produto(
                    "PRD-002", "Milho", "SC",
                    new BigDecimal("53.90"), 10, 8);

            Produto produto3 = new Produto(
                    "PRD-003", "Farelo de Soja", "SC",
                    new BigDecimal("45.50"), 20, 42);

            Produto produto4 = new Produto(
                    "PRD-004", "Sal Mineral", "SC",
                    new BigDecimal("98.50"), 20, 42);

            Produto produto5 = new Produto(
                    "PRD-005", "Suplemento Proteico", "SC",
                    new BigDecimal("89.90"), 10, 42);

            // MEDICAMENTOS VETERINÁRIOS
            Produto produto6 = new Produto(
                    "PRD-006", "Vermífugo", "FR",
                    new BigDecimal("35.00"), 5, 10);

            Produto produto7 = new Produto(
                    "PRD-007", "Antibiótico", "FR",
                    new BigDecimal("50.00"), 5, 10);

            // VACINAS
            Produto produto8 = new Produto(
                    "PRD-008", "Vacina Febre Aftosa", "DS",
                    new BigDecimal("15.00"), 10, 100);

            Produto produto9 = new Produto(
                    "PRD-009", "Vacina contra Raiva", "DS",
                    new BigDecimal("7.50"), 10, 100);

            Produto produto10 = new Produto(
                    "PRD-010", "Vacina Brucelose", "FR",
                    new BigDecimal("53.50"), 5, 10);

            // FERRAMENTAS E MATERIAIS
            Produto produto11 = new Produto(
                    "PRD-011", "Seringa", "UN",
                    new BigDecimal("1.20"), 5, 10);

            Produto produto12 = new Produto(
                    "PRD-012", "Agulha", "UN",
                    new BigDecimal("0.30"), 5, 100);

            Produto produto13 = new Produto(
                    "PRD-013", "Alicate", "UN",
                    new BigDecimal("15.00"), 1, 2);

            Produto produto14 = new Produto(
                    "PRD-014", "Enxada", "UN",
                    new BigDecimal("27.50"), 1, 3);

            Produto produto15 = new Produto(
                    "PRD-015", "Arame Farpado", "RL",
                    new BigDecimal("450.00"), 1, 3);

            Produto produto16 = new Produto(
                    "PRD-016", "Arame Liso", "RL",
                    new BigDecimal("430.00"), 1, 3);

            // RELACIONAMENTO DOS PRODUTOS COM AS CATEGORIAS

            produto1.setCategoria(alimentacao);
            produto2.setCategoria(alimentacao);
            produto3.setCategoria(alimentacao);
            produto4.setCategoria(alimentacao);
            produto5.setCategoria(alimentacao);

            produto6.setCategoria(medicamentos);
            produto7.setCategoria(medicamentos);

            produto8.setCategoria(vacinas);
            produto9.setCategoria(vacinas);
            produto10.setCategoria(vacinas);

            produto11.setCategoria(ferramentas);
            produto12.setCategoria(ferramentas);
            produto13.setCategoria(ferramentas);
            produto14.setCategoria(ferramentas);
            produto15.setCategoria(ferramentas);
            produto16.setCategoria(ferramentas);

            // LOCALIZAÇÃO DOS PRODUTOS
            produto1.setLocalizacao("Depósito A1");
            produto2.setLocalizacao("Depósito A2");
            produto3.setLocalizacao("Depósito A3");
            produto4.setLocalizacao("Depósito A4");
            produto5.setLocalizacao("Depósito A5");

            produto6.setLocalizacao("Armário Veterinário");
            produto7.setLocalizacao("Armário Veterinário");

            produto8.setLocalizacao("Geladeira de Vacinas");
            produto9.setLocalizacao("Geladeira de Vacinas");
            produto10.setLocalizacao("Geladeira de Vacinas");

            produto11.setLocalizacao("Armário de Ferramentas");
            produto12.setLocalizacao("Armário de Ferramentas");
            produto13.setLocalizacao("Galpão");
            produto14.setLocalizacao("Galpão");
            produto15.setLocalizacao("Depósito de Materiais");
            produto16.setLocalizacao("Depósito de Materiais");

            // SALVAR TODOS OS 16 PRODUTOS
            List<Produto> produtos = List.of(
                    produto1, produto2, produto3, produto4,
                    produto5, produto6, produto7, produto8,
                    produto9, produto10, produto11, produto12,
                    produto13, produto14, produto15, produto16
            );

            produtoRepository.saveAll(produtos);
        }

        // 4. CADASTRO DE MOVIMENTAÇÕES
        if (movimentacaoRepository.count() == 0) {

            List<Produto> produtos =
                    produtoRepository.findAll(Sort.by("id"));

            if (produtos.size() >= 3) {

                Movimentacao movimentacao1 = new Movimentacao(
                        TipoMovimentacao.ENTRADA,
                        200,
                        LocalDate.of(2026, 10, 14),
                        produtos.get(0)
                );

                movimentacao1.setObservacao("Compra de ração bovina");

                Movimentacao movimentacao2 = new Movimentacao(
                        TipoMovimentacao.SAIDA,
                        2,
                        LocalDate.of(2026, 10, 15),
                        produtos.get(1)
                );

                movimentacao2.setObservacao("Retirada de milho");

                Movimentacao movimentacao3 = new Movimentacao(
                        TipoMovimentacao.ENTRADA,
                        10,
                        LocalDate.of(2026, 10, 16),
                        produtos.get(2)
                );

                movimentacao3.setObservacao("Compra de farelo de soja");

                movimentacaoRepository.save(movimentacao1);
                movimentacaoRepository.save(movimentacao2);
                movimentacaoRepository.save(movimentacao3);
            }
        }
        
                // Nova entidade (guia de personalização)
        if (fornecedorRepository.count() == 0) {
            Fornecedor fornecedor1 = new Fornecedor("Distribuidora Paraná", "12.345.678/0001-90", "(43) 3321-1000");
            Fornecedor fornecedor2 = new Fornecedor("Casa do Parafuso", "23.456.789/0001-01", "(43) 3322-2000");
            Fornecedor fornecedor3 = new Fornecedor("EPI Sul", "34.567.890/0001-12", "(43) 3323-3000");
            Fornecedor fornecedor4 = new Fornecedor("COCAMAR COOPERATIVA AGROINDUSTRIAL LTDA", "79.114.450/0171-30", "(43) 3357-2008");
            Fornecedor fornecedor5 = new Fornecedor("COOPERATIVA AGROINDUSTRIAL DE LONDRINA - CATIVA", "78.597.085/0001-24", "(43) 3341-1905");
            Fornecedor fornecedor6 = new Fornecedor("PASTOREIO NORTE DO PARANA COM DE INSUMOS AGROPEC LTDA", "23.383.403/0001-62", "(43) 3324-7831");
            Fornecedor fornecedor7 = new Fornecedor("Cooperativa Agrária Agroindustrial", "77.890.846/0004-11", "(42) 3625-8000");
            Fornecedor fornecedor8 = new Fornecedor("MULTICAMPO PRODUTOS AGROPECUARIOS LTDA", "04.163.386/0001-03", "(43)3348-4040");
            fornecedorRepository.save(fornecedor1);
            fornecedorRepository.save(fornecedor2);
            fornecedorRepository.save(fornecedor3);
            fornecedorRepository.save(fornecedor4);
            fornecedorRepository.save(fornecedor5);
            fornecedorRepository.save(fornecedor6);
            fornecedorRepository.save(fornecedor7);
            fornecedorRepository.save(fornecedor8);
        }

     }
}