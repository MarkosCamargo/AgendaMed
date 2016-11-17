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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ceavi.udesc.agendamedmobile.R;
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
            //System.err.println(CryptUtils.decrypt("3VTHC2Ul5HJsGOx4EcICs4HwDHVkuyGjA4vFycCy1N93hTM4cVW4/Rc54z6OUEjnexCL+k/9fVClWdaRHEBUPA=="));

            String baseUrlAuth = "http://agendamedauth.herokuapp.com/";

            // teste autenticação web
            JSONObject parametro = new JSONObject();
            parametro.put("login", etUsuario.getText().toString());
            parametro.put("senha", Md5Utils.toMd5(etSenha.getText().toString()));
            System.out.println(parametro.toString());

            JSONObject j_resposta = new JSONObject(executePost(baseUrlAuth + "autentica_web", parametro.toString()));
            System.out.print(j_resposta.toString());
            try {
                j_resposta.getString("token");
                mostrarMensagem("Seja Bem-Vindo!!");
                Intent intent = new Intent(this, OpcoesActivity.class);
                finish();
                startActivity(intent);
            } catch (Exception ex) {
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
                mostrarMensagem(j_resposta.getString("mensagem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static String executePost(String url, String param) throws UnsupportedEncodingException {
        return execute(HTTPMethod.POST, url, URLEncoder.encode(param, "UTF-8"));
    }

    public static String executeGet(String url, String param) throws UnsupportedEncodingException {
        return execute(HTTPMethod.GET, url + "?parametro=" + URLEncoder.encode(param, "UTF-8"));
    }

    private static String execute(HTTPMethod method, String url) {
        HttpURLConnection connection = null;
        try {
            connection = getNewConnection(new URL(url));
            connection.setRequestMethod(method.toString());

            return getResponse(connection);
        } catch (IOException e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String execute(HTTPMethod method, String url, String param) {
        HttpURLConnection connection = null;
        try {
            connection = getNewConnection(new URL(url));
            connection.setRequestMethod(method.toString());

            // envia requisição
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes("parametro=" + param);
            writer.flush();
            writer.close();

            return getResponse(connection);
        } catch (IOException e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static HttpURLConnection getNewConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "pt-BR");

        connection.setUseCaches(false);
        connection.setDoOutput(true);

        return connection;
    }

    private enum HTTPMethod {
        POST,
        GET
    }

}
