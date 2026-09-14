package br.edu.iff.ccc.devteck.entities;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrinhos")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", unique = true)
    private Long clienteId;

    @ElementCollection
    @CollectionTable(name = "carrinho_itens", joinColumns = @JoinColumn(name = "carrinho_id"))
    @MapKeyColumn(name = "produto_id")
    @Column(name = "quantidade")
    private Map<Long, Integer> itens;

    private double valorTotal;

    public Carrinho() {
        this.itens = new LinkedHashMap<>();
    }

    public Carrinho(Long id, Long clienteId) {
        this();
        this.id = id;
        this.clienteId = clienteId;
    }

    public void adicionarProduto(Long produtoId, int quantidade) {
        
        itens.merge(produtoId, quantidade, Integer::sum);
    }

    public void removerProduto(Long produtoId) {
        itens.remove(produtoId);
    }

    public void definirQuantidade(Long produtoId, int quantidade) {
    
        if (quantidade <= 0) {
            itens.remove(produtoId);
        } else {
            itens.put(produtoId, quantidade);
        }
    }

    public void limparCarrinho() {
        itens.clear();
        valorTotal = 0;
    }

    //Get e Set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Map<Long, Integer> getItens() {
        return itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

}
