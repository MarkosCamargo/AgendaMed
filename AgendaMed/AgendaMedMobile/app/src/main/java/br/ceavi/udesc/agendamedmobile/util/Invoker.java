package br.ceavi.udesc.agendamedmobile.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by marcos on 17/11/16.
 */

public class Invoker {
    public static final String baseUrlAuth = "http://agendamedauth.herokuapp.com/";
    public static final String baseUrlAgenda = "http://agendamedagenda.herokuapp.com/";
    public static String token = "";
    public static int id = 0;

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
