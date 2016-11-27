package br.ceavi.udesc.agendamedmobile.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class CancelarAgendamentoActivity extends AppCompatActivity {
    private ListView lvAgendamentos;
    public List<Object> agendamentos = new ArrayList<>();
    private ArrayAdapter<Object> adapter;
    private Button btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_agendamento);
        try {
            JSONObject parametro = new JSONObject();
            parametro.put("token", Invoker.token);
            JSONObject j_resposta = new JSONObject(Invoker.executeGet(Invoker.baseUrlAgenda + "agenda/lista", parametro.toString()));//id do usuario
            System.out.print(j_resposta.toString());
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

        this.lvAgendamentos = (ListView) findViewById(R.id.lvAgendamentos);
        this.btnCancelar = (Button) findViewById(R.id.btnCancelar);

        this.adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, agendamentos);
        lvAgendamentos.setAdapter(adapter);

    }

    private void mostrarMensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
