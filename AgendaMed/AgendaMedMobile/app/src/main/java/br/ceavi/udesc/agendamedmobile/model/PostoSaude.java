package br.ceavi.udesc.agendamedmobile.model;

/**
 * Created by marcos on 10/11/16.
 */

public class PostoSaude {
    private int id;
    private String cnpj;
    private String email;
    private String nome;
    private int telefone;

    public PostoSaude(String cnpj, String email, int id, String nome, int telefone) {
        this.cnpj = cnpj;
        this.email = email;
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
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
