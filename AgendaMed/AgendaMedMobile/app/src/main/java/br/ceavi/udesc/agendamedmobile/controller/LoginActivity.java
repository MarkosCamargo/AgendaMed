package br.ceavi.udesc.agendamedmobile.controller;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ceavi.udesc.agendamedmobile.R;
import br.ceavi.udesc.agendamedmobile.util.Invoker;
import br.ceavi.udesc.agendamedmobile.util.Md5Utils;

public class LoginActivity extends AppCompatActivity {
    private Button btnCriarConta;
    private Button btnLogin;
    private EditText etUsuario;
    private EditText etSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        this.etSenha = (EditText) findViewById(R.id.etSenha);
        this.etUsuario = (EditText) findViewById(R.id.etUsuario);

        this.btnCriarConta = (Button) findViewById(R.id.btnCriarConta);
        this.btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar(v);
            }
        });

        this.btnLogin = (Button) findViewById(R.id.btnEntrar);
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar(v);
            }
        });

    }

    private void entrar(View v) {
        try {
            JSONObject parametro = new JSONObject();
            parametro.put("login", etUsuario.getText().toString());
            parametro.put("senha", Md5Utils.toMd5(etSenha.getText().toString()));
            //System.out.println(parametro.toString());

            JSONObject j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAuth + "autentica_mobile", parametro.toString()));
            System.out.println(j_resposta.toString());

            if (j_resposta.has("token")) {
                Invoker.token = j_resposta.getString("token"); //nao colocar esse token pois ele é diferente do modulo web
                mostrarMensagem("Seja Bem-Vindo, " + etUsuario.getText().toString());
                Intent intent = new Intent(this, OpcoesActivity.class);
                finish();
                startActivity(intent);
            } else {
                mostrarMensagem(j_resposta.getString("mensagem"));
            }
        } catch (JSONException ex) {
            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception et) {
            et.printStackTrace();
            mostrarMensagem("Você não está conectado a internet!");
        }


    }

    public void mostrarMensagem(String mensagem) {
        Toast.makeText(this,
                mensagem,
                Toast.LENGTH_SHORT).show();
    }

    private void cadastrar(View v) {
        Intent intent = new Intent(this, CadastrarActivity.class);
        //finish();
        startActivity(intent);
    }


}
