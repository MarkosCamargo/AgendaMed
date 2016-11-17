package br.ceavi.udesc.agendamedmobile.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ceavi.udesc.agendamedmobile.R;

public class OpcoesActivity extends AppCompatActivity {
    private Button btnSolicitarAgendamento;
    private Button btnCancelarAgendamento;
    private Button btnHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        this.btnSolicitarAgendamento = (Button) findViewById(R.id.btnSolicitarAgendamento);
        this.btnSolicitarAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarAgendamento(v);
            }
        });

        this.btnCancelarAgendamento = (Button) findViewById(R.id.btnCancelarAgendamento);
        this.btnCancelarAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarAgendamento(v);
            }
        });

        this.btnHistorico = (Button) findViewById(R.id.btnHistotico);
        this.btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historicoAgendamento(v);
            }
        });


    }

    private void historicoAgendamento(View v) {
        Intent intent = new Intent(this, HistoricoActivity.class);
        //finish();
        startActivity(intent);
    }

    private void cancelarAgendamento(View v) {
        Intent intent = new Intent(this, CancelarAgendamentoActivity.class);
        //finish();
        startActivity(intent);
    }

    private void solicitarAgendamento(View v) {
        Intent intent = new Intent(this, PostoActivity.class);
        //finish();
        startActivity(intent);
    }
}
