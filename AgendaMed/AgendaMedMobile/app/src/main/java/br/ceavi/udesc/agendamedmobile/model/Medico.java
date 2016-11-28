package br.ceavi.udesc.agendamedmobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos on 10/11/16.
 */

public class Medico {
    private int id;
    private String crm;
    private String nome;

    private List<Especialidade> especialidades = new ArrayList<>();

    public Medico(String crm, int id, String nome) {
        this.crm = crm;
        this.id = id;
        this.nome = nome;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nome;
    }
}
