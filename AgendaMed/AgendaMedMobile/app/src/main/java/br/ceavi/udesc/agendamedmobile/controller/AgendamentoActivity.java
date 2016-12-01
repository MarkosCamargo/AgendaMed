package br.ceavi.udesc.agendamedmobile.controller;

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
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class AgendamentoActivity extends AppCompatActivity {

    private ListView listViewDatas;
    private Button btnConfirmar;
    private List<String> datas = new ArrayList<>();
    int idHorario = 0;
    String data;
    String hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);
        int medicoID = 0;
        Intent intent = getIntent();
        if (intent.hasExtra("medicoID")) {
            medicoID = intent.getIntExtra("medicoID", medicoID);
        }
        //micro put id_medico
        JSONObject parametro = new JSONObject();
        JSONObject j_resposta = new JSONObject();

        try {
            parametro.put("token", Invoker.token);
            parametro.put("id_medico", medicoID);
            j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "agenda/disponibilidade", parametro.toString()));
            System.out.println(j_resposta.toString());
            if (j_resposta.has("itens")) {
                JSONArray j = j_resposta.getJSONArray("itens");
                for (int i = 0; i < j.length(); i++) {
//                    datas.add(j.getJSONObject(i).getString("date"));
                    if (j.getJSONObject(i).has("horas")) {
                        JSONArray h = j.getJSONObject(i).getJSONArray("horas");
                        for (int ii = 0; ii < h.length(); ii++) {
                            String[] mudarOrdem = j.getJSONObject(i).getString("date").split("-");
                            datas.add(j.getJSONObject(i).get("id_horario") + "-" + mudarOrdem[2] + "/" + mudarOrdem[1] + "/" + mudarOrdem[0] + "-" + h.get(ii));
                        }
                    }
                }
            } else {
                mostrarMensagem("Este Médico não possui HORÁRIOS");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        this.btnConfirmar = (Button) findViewById(R.id.btnConf);
        this.listViewDatas = (ListView) findViewById(R.id.lvDatas);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        this.listViewDatas.setAdapter(adapter);
        this.listViewDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String medico = (String) listViewDatas.getItemAtPosition(position);
                mostrarMensagem(medico);
                //idMedico = medico.getId();
                String[] coleta = medico.split("-");
                idHorario = Integer.parseInt(coleta[0]);
                data = coleta[1];
                hora = coleta[2];

                // Toast.makeText(getApplicationContext(), "Nome: " + dis.getNome() + "\n" + "Especialiade: " + dis.getProfissao(), Toast.LENGTH_SHORT).show();

            }
        });

        this.btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirma(v);
            }
        });

    }

    private void confirma(View v) {
        if (idHorario != 0) {
            try {
                String[] mudarOrdem = data.split("/");
                //"2016-11-28"
                data = mudarOrdem[2] + "-" + mudarOrdem[1] + "-" + mudarOrdem[0];

                JSONObject parametro;
                JSONObject j_resposta;

                parametro = new JSONObject();
//if (has("id_paciente"))
                parametro.put("token", Invoker.token);
                parametro.put("data", data);
                parametro.put("hora", hora);
                parametro.put("id_horario", idHorario);
                parametro.put("id_usuario", Invoker.id);
                parametro.put("situacao", 1);
//    SOLICITADO(1),
//    AGENDADO(2),
//    CANCELADO(3),
//    ATENDIDO(4),;

                j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "agenda/insere", parametro.toString()));
                if (j_resposta.has("id")) {
                    mostrarMensagem("Horário Solicitado com sucesso!!");
                    Intent intent = new Intent(this, OpcoesActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    mostrarMensagem("acho q deu merda");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            mostrarMensagem("Selecione um Horario para agendar");

        }
    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
