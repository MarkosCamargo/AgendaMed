/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ceavi.udesc.agendamedmobile.model;

/**
 * @author Ricardo Augusto Küstner
 */
public enum SituacaoAgenda {
    SOLICITADO(1),
    AGENDADO(2),
    CANCELADO(3),
    ATENDIDO(4),;

    private final int value;

    private SituacaoAgenda(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SituacaoAgenda getSituacaoAgenda(int situacao) {
        for (SituacaoAgenda item : values()) {
            if (item.getValue() == situacao) {
                return item;
            }
        }
        return null;
    }
}
