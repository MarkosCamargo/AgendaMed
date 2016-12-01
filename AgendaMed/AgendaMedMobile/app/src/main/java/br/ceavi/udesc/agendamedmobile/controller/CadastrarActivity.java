package br.ceavi.udesc.agendamedmobile.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import br.ceavi.udesc.agendamedmobile.R;
import br.ceavi.udesc.agendamedmobile.util.CryptUtils;
import br.ceavi.udesc.agendamedmobile.util.Invoker;
import br.ceavi.udesc.agendamedmobile.util.Md5Utils;

public class CadastrarActivity extends AppCompatActivity {
    private Button btnCriarConta;

    private EditText etSenhaCad;
    private EditText etSenha2Cad;
    private EditText etNomeCad;
    private EditText etNasCad;
    private EditText etSusCad;
    private EditText etEnderecoCad;
    private EditText etUsuarioCad;
    private EditText etEmailCad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


        this.etSenhaCad = (EditText) findViewById(R.id.etSenhaCad);
        this.etSenha2Cad = (EditText) findViewById(R.id.etSenha2Cad);
        this.etNomeCad = (EditText) findViewById(R.id.etNomeCad);
        this.etSusCad = (EditText) findViewById(R.id.etSusCad);
        this.etEnderecoCad = (EditText) findViewById(R.id.etEnderecoCad);
        this.etUsuarioCad = (EditText) findViewById(R.id.etUsuarioCad);
        this.etEmailCad = (EditText) findViewById(R.id.etEmailCad);
        this.etNasCad = (EditText) findViewById(R.id.etNasCad);

        this.btnCriarConta = (Button) findViewById(R.id.btnCriar);
        this.btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarConta(v);
            }
        });
    }

    private void criarConta(View v) {
        if (verificaCamposNulos()) {
            mostrarMensagem("Existem Campos em branco!");
        } else {
            if (etSenhaCad.getText().toString().equals(etSenha2Cad.getText().toString())) {
                // criando usuario
                JSONObject parametro = new JSONObject();
                JSONObject j_resposta;
                try {
                    parametro.put("login", etUsuarioCad.getText().toString());
                    // a senha deve ser enviada criptografada (md5 de preferência)
                    parametro.put("senha", Md5Utils.toMd5(etSenhaCad.getText().toString()));
                    parametro.put("mobile", "true");
                    parametro.put("email", etEmailCad.getText().toString());
                    // sucesso se login não existir
                    j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAuth + "cria_usuario", parametro.toString()));

                    parametro = new JSONObject();
                    parametro.put("login", etUsuarioCad.getText().toString());
                    parametro.put("senha", Md5Utils.toMd5(etSenhaCad.getText().toString()));
                    System.out.println(parametro.toString());
                    j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAuth + "autentica_mobile", parametro.toString()));

                    System.out.println(j_resposta.toString());
                    //tring resposta =
                    if(j_resposta.has("token")) {
                        Invoker.token = j_resposta.getString("token");
                        int id = j_resposta.getInt("id");

                        //Criando o Paciente
                        parametro = new JSONObject();
                        //Criando o Endereco
                        JSONObject parametroEndereco = new JSONObject();

                        parametroEndereco.put("id", 0);
                        parametroEndereco.put("descricao", etEnderecoCad.getText().toString());
                        parametroEndereco.put("latitude", 0);
                        parametroEndereco.put("longitude", 0);

                        parametro.put("token", Invoker.token);
                        parametro.put("nome", etNomeCad.getText().toString());
                        parametro.put("numero_sus", etSusCad.getText().toString());
                        parametro.put("id_usuario", id);
                        parametro.put("endereco", parametroEndereco);
                        parametro.put("nascimento", etNasCad.getText().toString());


                        j_resposta = new JSONObject(Invoker.executePost(Invoker.baseUrlAgenda + "paciente/insere", parametro.toString()));


                        mostrarMensagem(j_resposta.toString());
                    }else{
                        mostrarMensagem(j_resposta.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent);
            } else {
                mostrarMensagem("As senhas não são iguais!");

            }

        }

    }

    public void mostrarMensagem(String mensagem) {
        Toast.makeText(this,
                mensagem,
                Toast.LENGTH_SHORT).show();

    }

    private boolean verificaCamposNulos() {
        return etSenhaCad.getText().toString().equals("") || etSenha2Cad.getText().toString().equals("") ||
                etSusCad.getText().toString().equals("") || etUsuarioCad.getText().toString().equals("") ||
                etEnderecoCad.getText().toString().equals("") || etNomeCad.getText().toString().equals("");
    }
}
