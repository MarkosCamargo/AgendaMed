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
import br.ceavi.udesc.agendamedmobile.model.Endereco;
import br.ceavi.udesc.agendamedmobile.model.PostoSaude;
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class PostoActivity extends AppCompatActivity {
    private Button btnBuscar;
    private EditText etPosto;
    private ListView lvPostos;
    public List<PostoSaude> postos = new ArrayList<>();
    private ArrayAdapter<PostoSaude> adapter;
    AlertDialog.Builder dialog = null;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto);
        try {
            JSONObject parametro = new JSONObject();

            parametro.put("token", Invoker.token);
            JSONObject j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "posto_saude/lista", parametro.toString()));
            System.out.println(j_resposta.toString());
            JSONArray j = j_resposta.getJSONArray("itens");

            for (int i = 0; i < j.length(); i++) {
                if (j.getJSONObject(i).has("endereco")) {
                    JSONObject j_endereco = j.getJSONObject(i).getJSONObject("endereco");
                    //                                                Endereco(String descricao, int id, double latitude, double longitude) {
                    postos.add(new PostoSaude(j.getJSONObject(i).getLong("cnpj"), j.getJSONObject(i).getInt("id"), j.getJSONObject(i).getString("nome"), new Endereco(j_endereco.getString("descricao"),j_endereco.getInt("id"), j_endereco.getDouble("latitude"),j_endereco.getDouble("longitude"))));
                }else{
                    postos.add(new PostoSaude(j.getJSONObject(i).getLong("cnpj"), j.getJSONObject(i).getInt("id"), j.getJSONObject(i).getString("nome"), null));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException ete) {
            ete.printStackTrace();
            mostrarMensagem("Não há postos de saúde cadastrados no servidor");
        } catch (Exception et) {
            et.printStackTrace();
            mostrarMensagem("Você não está conectado a internet!");
        }

        this.etPosto = (EditText) findViewById(R.id.etNomePosto);
        this.btnBuscar = (Button) findViewById(R.id.btnBuscarPosto);
        this.lvPostos = (ListView) findViewById(R.id.lvPosto);

        this.adapter = new ArrayAdapter<PostoSaude>(this, android.R.layout.simple_list_item_1, postos);
        lvPostos.setAdapter(adapter);
        lvPostos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostoSaude dis = (PostoSaude) lvPostos.getItemAtPosition(position);
                mostrarMensagem(dis.getNome() +", ID: " +dis.getId() + " Selecionado");

                //exibir uma dialog se o paciente quer procurar os medicos pelo nome ou pelas especialiades
                //mostrar os medicos deste posto para o paciente para realizar um agendamento
                PostoActivity.this.id = dis.getId();
                criaDialog(view);
            }
        });

        this.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(v);
            }
        });


    }

    private void criaDialog(View view) {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Aviso");
        dialog.setMessage("Que maneira deseja agendar?");
        dialog.setNegativeButton("Por Médicos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PostoActivity.this, MedicoActivity.class);
                //finish();
                intent.putExtra("postoID", id);
                startActivity(intent);
            }
        });
        dialog.setPositiveButton("Por Especialidades", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarMensagem("Implementar isso!!");

            }
        });
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.create();
        dialog.show();
    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void buscar(View v) {
        String bsca = this.etPosto.getText().toString();
        List<PostoSaude> aux = new ArrayList<>();
        for (PostoSaude s : postos) {
            if (s.getNome().contains(bsca)) {
                //adapter.notifyDataSetChanged();
                aux.add(s);
            }
        }
        //Atualiza o ListView
        this.adapter = new ArrayAdapter<PostoSaude>(this, android.R.layout.simple_list_item_1, aux);
        lvPostos.setAdapter(adapter);
    }
}
