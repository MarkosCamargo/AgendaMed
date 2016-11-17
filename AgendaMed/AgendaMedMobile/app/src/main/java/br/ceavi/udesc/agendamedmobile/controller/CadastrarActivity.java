package br.ceavi.udesc.agendamedmobile.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ceavi.udesc.agendamedmobile.R;

public class CadastrarActivity extends AppCompatActivity {
    private Button btnCriarConta;

    private EditText etSenhaCad;
    private EditText etSenha2Cad;
    private EditText etNomeCad;
    private EditText etSusCad;
    private EditText etEnderecoCad;
    private EditText etUsuarioCad;

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
                //Criar Usuario ou Paciente

                mostrarMensagem("Conta criada com sucesso!");
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
