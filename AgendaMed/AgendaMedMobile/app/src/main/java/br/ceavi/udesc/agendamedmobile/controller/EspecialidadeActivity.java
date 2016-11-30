package br.ceavi.udesc.agendamedmobile.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.ceavi.udesc.agendamedmobile.R;
import br.ceavi.udesc.agendamedmobile.model.Especialidade;
import br.ceavi.udesc.agendamedmobile.model.Medico;
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class EspecialidadeActivity extends AppCompatActivity {
    AlertDialog.Builder dialog = null;
    private ListView listViewEspecialidades;
    private Button btnAgendarEsp;
    private List<Medico> medicos = new ArrayList<>();
    private List<Especialidade> especialidades = new ArrayList<>();
    int idMedico = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidade);
        int postoID = 0;
        Intent intent = getIntent();
        if (intent.hasExtra("postoID")) {
            postoID = intent.getIntExtra("postoID", postoID);
        }

        System.out.println("ID POSTO: " + postoID);
        JSONObject parametro = new JSONObject();

        try {
            parametro.put("token", Invoker.token);
            parametro.put("id_posto_saude", postoID);
            JSONObject j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "medico/lista", parametro.toString()));
            System.out.println(j_resposta.toString());
            if (j_resposta.has("itens")) {
                JSONArray j = j_resposta.getJSONArray("itens");
                for (int i = 0; i < j.length(); i++) {
                    medicos.add(new Medico(j.getJSONObject(i).getString("crm"), j.getJSONObject(i).getInt("id"), j.getJSONObject(i).getString("nome")));
                    JSONArray h = j.getJSONObject(i).getJSONArray("especialidades");
                    for (int ii = 0; ii < h.length(); ii++) {
                        medicos.get(i).getEspecialidades().add(new Especialidade(h.getJSONObject(ii).getInt("id"), h.getJSONObject(ii).getString("descricao")));
                        especialidades.add(new Especialidade(h.getJSONObject(ii).getInt("id"), h.getJSONObject(ii).getString("descricao")));
                        //System.out.println(h.get(ii));
                    }

                }
            } else {
                mostrarMensagem("Este posto não possui médicos");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        this.btnAgendarEsp = (Button) findViewById(R.id.btnAgendarEsp);
        this.listViewEspecialidades = (ListView) findViewById(R.id.lvEspecialidades);

        ArrayAdapter<Especialidade> adapter = new ArrayAdapter<Especialidade>(this, android.R.layout.simple_list_item_1, especialidades);
        this.listViewEspecialidades.setAdapter(adapter);
        this.listViewEspecialidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Especialidade especialidade = (Especialidade) listViewEspecialidades.getItemAtPosition(position);

                for (Medico m : medicos) {
                    for (Especialidade e : m.getEspecialidades()) {
                        if (especialidade.getId() == e.getId()) {
                            idMedico = m.getId();
                            break;
                        }
                    }
                }

                // Toast.makeText(getApplicationContext(), "Nome: " + dis.getNome() + "\n" + "Especialiade: " + dis.getProfissao(), Toast.LENGTH_SHORT).show();

            }
        });

        this.btnAgendarEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaDialog(v);
            }
        });
    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void criaDialog(View view) {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Aviso");
        dialog.setMessage("Que maneira deseja agendar?");
        dialog.setNegativeButton("Por Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //mostrarMensagem("Implementar isso!!");
                Intent intent = new Intent(EspecialidadeActivity.this, AgendamentoDataActivity.class);
                //finish();
                intent.putExtra("medicoID", idMedico);
                startActivity(intent);

            }
        });
        dialog.setPositiveButton("Por Data Mais Proxima", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EspecialidadeActivity.this, AgendamentoActivity.class);
                //finish();
                intent.putExtra("medicoID", idMedico);
                startActivity(intent);
            }
        });
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.create();
        dialog.show();
    }
}
