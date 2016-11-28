package br.ceavi.udesc.agendamedmobile.model;

/**
 * Created by marcos on 10/11/16.
 */

public class PostoSaude {
    private int id;
    private long cnpj;
    //    private String email;
    private String nome;
    //    private int telefone;
    private Endereco endereco;

    public PostoSaude(long cnpj, int id, String nome, Endereco endereco) {
        this.cnpj = cnpj;
//        this.email = email;
        this.id = id;
        this.nome = nome;
//        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }


}
