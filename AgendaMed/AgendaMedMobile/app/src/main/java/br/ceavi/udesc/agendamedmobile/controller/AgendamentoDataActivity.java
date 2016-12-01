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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.ceavi.udesc.agendamedmobile.R;
import br.ceavi.udesc.agendamedmobile.model.PostoSaude;
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class AgendamentoDataActivity extends AppCompatActivity {
    AlertDialog.Builder dialog = null;
    private ListView lvDatas;
    private EditText etDataVerificar;
    private Button btnBuscarDisponibilidade;
    private ArrayAdapter<String> adapter;
    private List<String> datas = new ArrayList<>();
    int idHorario;
    int medicoID = 0;
    String data;
    String hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_data);
        this.lvDatas = (ListView) findViewById(R.id.lvDates);
        this.etDataVerificar = (EditText) findViewById(R.id.etDataVerificar);
        this.btnBuscarDisponibilidade = (Button) findViewById(R.id.btnBuscarDisponibilidade);

        Intent intent = getIntent();
        if (intent.hasExtra("medicoID")) {
            medicoID = intent.getIntExtra("medicoID", medicoID);
        }
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

        this.btnBuscarDisponibilidade = (Button) findViewById(R.id.btnBuscarDisponibilidade);
        this.btnBuscarDisponibilidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(v);
            }
        });

        this.lvDatas = (ListView) findViewById(R.id.lvDates);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        this.lvDatas.setAdapter(adapter);
        this.lvDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String medico = (String) lvDatas.getItemAtPosition(position);
                mostrarMensagem(medico);
                //idMedico = medico.getId();
                String[] coleta = medico.split("-");
                idHorario = Integer.parseInt(coleta[0]);
                data = coleta[1];
                hora = coleta[2];

                criaDialog(view);
                // Toast.makeText(getApplicationContext(), "Nome: " + dis.getNome() + "\n" + "Especialiade: " + dis.getProfissao(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscar(View v) {
        JSONObject parametro = new JSONObject();
        JSONObject j_resposta = new JSONObject();

        parametro = new JSONObject();
        List<String> aux = new ArrayList<>();
        try {
            parametro.put("token", Invoker.token);

            data = etDataVerificar.getText().toString();
            String[] mudarOrdem1 = data.split("/");
            //"2016-11-28"
            data = mudarOrdem1[2] + "-" + mudarOrdem1[1] + "-" + mudarOrdem1[0];
            if (mudarOrdem1[2].length() == 4 && (mudarOrdem1[1].length() == 2 && Integer.parseInt(mudarOrdem1[1]) <= 12) && (mudarOrdem1[0].length() == 2 && Integer.parseInt(mudarOrdem1[0]) <= 31)) {
                parametro.put("id_medico", medicoID);
                parametro.put("data_inicio", data);// Ex: 2016-11-28
                j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "agenda/disponibilidade", parametro.toString()));
                if (j_resposta.has("itens")) {
                    JSONArray j = j_resposta.getJSONArray("itens");
                    for (int i = 0; i < j.length(); i++) {
//                    datas.add(j.getJSONObject(i).getString("date"));
                        if (j.getJSONObject(i).has("horas")) {
                            JSONArray h = j.getJSONObject(i).getJSONArray("horas");
                            for (int ii = 0; ii < h.length(); ii++) {
                                String[] mudarOrdem = j.getJSONObject(i).getString("date").split("-");
                                aux.add(j.getJSONObject(i).get("id_horario") + "-" + mudarOrdem[2] + "/" + mudarOrdem[1] + "/" + mudarOrdem[0] + "-" + h.get(ii));
                            }
                        }
                    }
                } else {
                    mostrarMensagem("Este Médico não possui HORÁRIOS");
                }
            } else {
                mostrarMensagem("A data informada é invalida");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            mostrarMensagem("A data informada é invalida");
            e.printStackTrace();
        }

        //Atualiza o ListView
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aux);
        lvDatas.setAdapter(adapter);
    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    private void criaDialog(View view) {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Aviso");
        dialog.setMessage("deseja agendar neste horario?");
        dialog.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirma(null);

            }
        });
        dialog.setPositiveButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.create();
        dialog.show();
    }


    private void confirma(View v) {
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
    }
}
