package br.ceavi.udesc.agendamedmobile.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import br.ceavi.udesc.agendamedmobile.model.Agenda;
import br.ceavi.udesc.agendamedmobile.model.PostoSaude;
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class CancelarAgendamentoActivity extends AppCompatActivity {
    private ListView lvAgendamentos;
    public List<Agenda> agendamentos = new ArrayList<>();
    private ArrayAdapter<Agenda> adapter;
    private Button btnCancelar;
    int idAgendamento = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_agendamento);
        try {
            JSONObject parametro = new JSONObject();
            parametro.put("token", Invoker.token);
            parametro.put("id_usuario", Invoker.id);
            JSONObject j_resposta = new JSONObject(Invoker.executeGet(Invoker.baseUrlAgenda + "agenda/lista", parametro.toString()));//id do usuario
            if (j_resposta.has("itens")) {
                JSONArray j = j_resposta.getJSONArray("itens");
//                SOLICITADO(1),
//                        AGENDADO(2),
//                        CANCELADO(3),
//                        ATENDIDO(4),;
                String situacao = "";
                for (int i = 0; i < j.length(); i++) {
                    switch (j.getJSONObject(i).getInt("situacao")) {
                        case 1:
                            situacao = "Solicitado";
                            break;
                        case 2:
                            situacao = "Agendado";
                            break;
                        case 3:
                            situacao = "Cancelado";
                            break;
                        case 4:
                            situacao = "Atendido";
                            break;
                    }
                    //(String data, String hora, int id, String situacao, String nomePosto) {
                    String[] mudarOrdem = j.getJSONObject(i).getString("data").split("-");
                    agendamentos.add(new Agenda(mudarOrdem[2] + "/" + mudarOrdem[1] + "/" + mudarOrdem[0], j.getJSONObject(i).getString("hora"), j.getJSONObject(i).getInt("id"), situacao, j.getJSONObject(i).getJSONObject("horario").getJSONObject("posto_saude").getString("nome")));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException ete) {
            ete.printStackTrace();
            mostrarMensagem("Não há agendamentos cadastrados no servidor");
        } catch (Exception et) {
            et.printStackTrace();
            mostrarMensagem("Você não está conectado a internet!");
        }

        if (agendamentos.isEmpty()) {
            mostrarMensagem("Não há agendamentos cadastrados no servidor");
        }

        this.lvAgendamentos = (ListView) findViewById(R.id.lvAgendamentos);
        this.btnCancelar = (Button) findViewById(R.id.btnCancelar);
        this.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar(v);
            }
        });


        this.adapter = new ArrayAdapter<Agenda>(this, android.R.layout.simple_list_item_1, agendamentos);
        lvAgendamentos.setAdapter(adapter);

        this.lvAgendamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agenda medico = (Agenda) lvAgendamentos.getItemAtPosition(position);
                //mostrarMensagem(medico.toString());

                idAgendamento = medico.getId();
            }
        });


    }

    private void cancelar(View v) {
        try {
            if (idAgendamento != 0) {
                JSONObject parametro = new JSONObject();
                JSONObject j_resposta;
                parametro.put("token", Invoker.token);
                parametro.put("id", idAgendamento);
                parametro.put("situacao", 3);//cancelado
                //parametro.put("data_inicio", "2016-11-28");
                j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "agenda/altera", parametro.toString()));

                List<Agenda> aux = agendamentos;
                for (int i = 0; i < aux.size(); i++) {
                    if (aux.get(i).getId() == idAgendamento) {
                        //adapter.notifyDataSetChanged();
                        aux.get(i).setSituacao("Cancelado");
                    }
                }
                //Atualiza o ListView
                adapter = new ArrayAdapter<Agenda>(this, android.R.layout.simple_list_item_1, aux);
                lvAgendamentos.setAdapter(adapter);
                mostrarMensagem("Cancelado com Sucesso!");
            } else {
                mostrarMensagem("Selecione um para cancelar");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
