package br.senai.sistema.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.senai.sistema.model.Categoria;
import br.senai.sistema.model.Produto;
import br.senai.sistema.repository.CategoriaRepository;
import br.senai.sistema.repository.ProdutoRepository;

/**
 * Conversores: transformam o id que vem do formulário (um texto, por exemplo "3")
 * no objeto correspondente do banco. São usados pelas listas de escolha (select)
 * dos formulários que têm relacionamento.
 */
@Configuration
public class Conversores implements WebMvcConfigurer {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public Conversores(ProdutoRepository produtoRepository,
                       CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // "3" -> produto de id 3 (texto vazio -> nenhum, e a validação avisa se for obrigatório)
        registry.addConverter(String.class, Produto.class,
                id -> id.isBlank() ? null : produtoRepository.findById(Long.valueOf(id)).orElse(null));

        // "3" -> categoria de id 3 (texto vazio -> nenhum, e a validação avisa se for obrigatório)
        registry.addConverter(String.class, Categoria.class,
                id -> id.isBlank() ? null : categoriaRepository.findById(Long.valueOf(id)).orElse(null));
    }
}