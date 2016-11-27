package br.ceavi.udesc.agendamedmobile.model;

/**
 * Created by marcos on 10/11/16.
 */

public class Endereco {
    private int id;
    private String descricao;
    private double latitude;
    private double longitude;

    public Endereco(String descricao, int id, double latitude, double longitude) {
        this.descricao = descricao;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
