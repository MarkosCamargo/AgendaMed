package br.ceavi.udesc.agendamedmobile.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class HistoricoActivity extends AppCompatActivity {
    private ListView lvHistorico;
    public List<Agenda> agendamentos = new ArrayList<>();
    private ArrayAdapter<Agenda> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        try {
            JSONObject parametro = new JSONObject();
            parametro.put("token", Invoker.token);
            parametro.put("id_usuario", Invoker.id);
            JSONObject j_resposta = new JSONObject(Invoker.executeGet(Invoker.baseUrlAgenda + "agenda/lista", parametro.toString()));//listar as consultas de um usuario
            //System.out.print(j_resposta.toString());
            //{"itens":[{"data":"2016-12-05","horario":{"hora_final":"12:00","hora_inicio":"08:00","posto_saude":{"telefone":"(47) 98888-3334","nome":"Posto de Saude de Ibirama","id":8,"email":"ibirama@sus.com.br"},"especialidades":[{"id":4,"descricao":"Cardiologista"}],"medico":{"nome":"House","id":1,"crm":"1"},"id":1,"dia_semana":1,"tempo_medio":30},"hora":"08:00","paciente":{"nascimento":"1996-11-02","numero_sus":"123456","id_usuario":15,"nome":"6","id":8},"id":13}]}
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
                    agendamentos.add(new Agenda(j.getJSONObject(i).getString("data"), j.getJSONObject(i).getString("hora"), j.getJSONObject(i).getInt("id"), situacao, j.getJSONObject(i).getJSONObject("horario").getJSONObject("posto_saude").getString("nome")));
                }

            }
            this.lvHistorico = (ListView) findViewById(R.id.lvHistorico);

            this.adapter = new ArrayAdapter<Agenda>(this, android.R.layout.simple_list_item_1, agendamentos);
            lvHistorico.setAdapter(adapter);
            if(agendamentos.isEmpty()){
                mostrarMensagem("Não há agendamentos cadastrados no servidor");
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
    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
