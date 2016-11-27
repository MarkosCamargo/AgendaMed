package br.ceavi.udesc.agendamedmobile.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.ceavi.udesc.agendamedmobile.R;
import br.ceavi.udesc.agendamedmobile.model.Medico;

public class MedicoActivity extends AppCompatActivity {
    private ListView listViewMedicos;
    private Button btnAgendar;
    private List<Medico> medicos = new ArrayList<>();
    //desconsiderar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);

        this.btnAgendar = (Button) findViewById(R.id.btnAgendar);
        this.listViewMedicos = (ListView) findViewById(R.id.lvMedicos);

        ArrayAdapter<Medico> adapter = new ArrayAdapter<Medico>(this, android.R.layout.simple_list_item_1, medicos);
        this.listViewMedicos.setAdapter(adapter);
        this.listViewMedicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medico medico = (Medico) listViewMedicos.getItemAtPosition(position);
                // Toast.makeText(getApplicationContext(), "Nome: " + dis.getNome() + "\n" + "Especialiade: " + dis.getProfissao(), Toast.LENGTH_SHORT).show();

            }
        });

        this.btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrar horarios disponiveis para o paciente realizar um agendamento
                //pode ser em uma dialog
            }
        });
    }
}
