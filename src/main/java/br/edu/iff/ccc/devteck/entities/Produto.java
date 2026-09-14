package br.edu.iff.ccc.devteck.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "produtos_cadastrados")
public class Produto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private long codigoBarras;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;
    private String categoria;
    private String imagem;
    private boolean ativo;

    public Produto() {
    }

    public Produto(Long id, String nome, long codigoBarras, String descricao, double preco,
                   int quantidadeEstoque, String categoria, String imagem, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
        this.imagem = imagem;
        this.ativo = ativo;
    }

    public void atualizarPreco(double novoPreco) {
        this.preco = novoPreco;
    }

    public void atualizarEstoque(int novaQuantidade) {
        this.quantidadeEstoque = novaQuantidade;
    }

    public void alterarStatus(boolean ativo) {
        this.ativo = ativo;
    }

    //Get e Set
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

    public long getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(long codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
