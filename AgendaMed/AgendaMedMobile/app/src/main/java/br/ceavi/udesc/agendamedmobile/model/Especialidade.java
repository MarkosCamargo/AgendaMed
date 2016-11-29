package br.ceavi.udesc.agendamedmobile.model;

/**
 * Created by marcos on 10/11/16.
 */

public class Especialidade {
    //CARDIOLOGISTA, PSIQUIATRA, ORTOPEDISTA, OFTAMOLOGISTA, UROLOGISTA, NEUROLOGISTA, INFECTOLOGISTA, DERMATOLOGISTA;
    private int id;
    private String descricao;

    public Especialidade(int id, String descricao) {
        this.descricao = descricao;
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
