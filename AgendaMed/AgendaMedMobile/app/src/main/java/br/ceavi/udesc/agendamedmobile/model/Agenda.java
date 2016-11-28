/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ceavi.udesc.agendamedmobile.model;


/**
 * @author Ricardo Augusto Küstner
 */
public class Agenda {
    //private static final long serialVersionUID = 1L;
    private int id;
    private Horario horario;
    private Paciente paciente;
    private String hora;
    private String data;
    private SituacaoAgenda situacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Horario getHorario() {
        if (horario == null) {
            horario = new Horario();
        }
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Paciente getPaciente() {
        if (paciente == null) {
            paciente = new Paciente();
        }
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSituacao() {
        return situacao.getValue();
    }

    public SituacaoAgenda situacao() {
        return situacao;
    }

    public void setSituacao(SituacaoAgenda situacao) {
        this.situacao = situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = SituacaoAgenda.getSituacaoAgenda(situacao);
    }

}
