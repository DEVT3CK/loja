package br.edu.iff.ccc.devteck.dto;

public class ItemCarrinhoView {

    private final Long produtoId;
    private final String nome;
    private final double preco;
    private final int quantidade;
    private final double subtotal;

    public ItemCarrinhoView(Long produtoId, String nome, double preco, int quantidade) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.subtotal = preco * quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }

}
