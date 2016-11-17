package br.ceavi.udesc.agendamedmobile.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.ceavi.udesc.agendamedmobile.R;
import br.ceavi.udesc.agendamedmobile.model.PostoSaude;
import br.ceavi.udesc.agendamedmobile.util.Invoker;

public class PostoActivity extends AppCompatActivity {
    private Button btnBuscar;
    private EditText etPosto;
    private ListView lvPostos;
    public List<PostoSaude> postos = new ArrayList<>();
    private ArrayAdapter<PostoSaude> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto);
        try {
            JSONObject parametro = new JSONObject();
            parametro.put("token", Invoker.token);
            JSONObject j_resposta = new JSONObject(Invoker.executeGet(Invoker.baseUrlAuth + "posto_saude/lista", parametro.toString()));
            System.out.print(j_resposta.toString());
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

        postos.add(new PostoSaude("100.111.111.11/1", "email@email.com", 1, "Posto de Saúde Ibirama", 32450990));
        postos.add(new PostoSaude("100.111.111.11/1", "email@email.com", 1, "Posto de Saúde Rio do Sul", 32450990));
        postos.add(new PostoSaude("100.111.111.11/1", "email@email.com", 1, "Posto de Saúde Pomerode", 32450990));
        postos.add(new PostoSaude("100.111.111.11/1", "email@email.com", 1, "Posto de Saúde Curitibanos", 32450990));
        postos.add(new PostoSaude("100.111.111.11/1", "email@email.com", 1, "Posto de Saúde Ascurra", 32450990));

        this.etPosto = (EditText) findViewById(R.id.etNomePosto);
        this.btnBuscar = (Button) findViewById(R.id.btnBuscarPosto);
        this.lvPostos = (ListView) findViewById(R.id.lvPosto);

        this.adapter = new ArrayAdapter<PostoSaude>(this, android.R.layout.simple_list_item_1, postos);
        lvPostos.setAdapter(adapter);
        lvPostos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostoSaude dis = (PostoSaude) lvPostos.getItemAtPosition(position);
                mostrarMensagem(dis.getNome() + " Selecionado");

                //exibir uma dialog se o paciente quer procurar os medicos pelo nome ou pelas especialiades
                //mostrar os medicos deste posto para o paciente para realizar um agendamento
            }
        });

        this.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(v);
            }
        });


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
