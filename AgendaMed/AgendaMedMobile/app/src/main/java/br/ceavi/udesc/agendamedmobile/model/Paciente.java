package br.ceavi.udesc.agendamedmobile.model;

import java.util.Date;

/**
 * Created by marcos on 10/11/16.
 */

public class Paciente {
    private int id;
    private Date dataNascimento;
    private String nome;
    private int numeroSus;
    private int telefone;
    //private byte[] foto;
    private Endereco endereco;

    @Override
    public String toString() {
        return nome;
    }
}
