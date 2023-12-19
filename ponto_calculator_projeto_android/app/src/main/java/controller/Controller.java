package controller;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import model.ContextApp;
import model.Funcionario;
import model.Ponto;

public class Controller extends AppCompatActivity
{
    AsyncHttpClient asyncHttpClient;
    ContextApp contextApp;
    String jsonResponse, tokenJWT, error, name, email;
    JSONObject jsonObject;
    int id;
    boolean active, staff;
    private RequisitionCallback callback;
    private RequisitionFuncionarioCallback funcionarioCallback;
    private RequisitionPontosCallback pontosCallback;

    // As URLs devem ser alteradas dependendo de onde o back-end estiver rodando
    String loginURL = "http://192.168.2.102:8001/api/Auth/login";
    String registerURL = "http://192.168.2.102:8001/api/Auth/register";
    String forgotPasswordURL = "http://192.168.2.102:8001/api/Auth/forgot-password?email=";
    String resetPasswordURL = "http://192.168.2.102:8001/api/Auth/reset-password";
    String getFucionarioDataURL = "http://192.168.2.102:8001/api/Auth/user";
    String logoutURL = "http://192.168.2.102:8001/api/Auth/logout";
    String registerPontoURL = "http://192.168.2.102:8001/api/ponto?jwt=";
    String getPontoURL  = "http://192.168.2.102:8001/api/ponto?jwt=";

    public Controller(ContextApp contextApp, RequisitionCallback callback)
    {
        this.contextApp = contextApp;
        this.callback = callback;
    }

    public Controller(ContextApp contextApp, RequisitionFuncionarioCallback funcionarioCallback)
    {
        this.contextApp = contextApp;
        this.funcionarioCallback = funcionarioCallback;
    }

    public Controller(ContextApp contextApp, RequisitionPontosCallback pontosCallback)
    {
        this.contextApp = contextApp;
        this.pontosCallback = pontosCallback;
    }

    public void loginValidate(String email, String password)
    {
        //Adicionando os parâmetos a um objeto JSON
        JSONObject jsonParams = new JSONObject();
        try
        {
            jsonParams.put("email", email);
            jsonParams.put("password", password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Transformando o objeto JSON no formato necessário para a requisição
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        //Criando uma instância da requisição
        asyncHttpClient = new AsyncHttpClient();

        //Requisição HTTP Post
        asyncHttpClient.post(contextApp, loginURL, entity, "application/json", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);

                // Pegando o retorno da requisição bem sucedida e printando
                jsonResponse = response.toString();
                Log.i("success", "onSuccess: " + jsonResponse);

                //Pegando o o token JWT que vem no retorno da requisição
                try
                {
                    jsonObject = new JSONObject(jsonResponse);
                    tokenJWT = jsonObject.getString("jwt");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Colocando o token recebido na requisição como o token atual da sessão
                contextApp.setCurrentTokenJWT(tokenJWT);

                //Retorna "verdadeiro" para a tela de login
                callback.onRequisitionResult(true);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                //Testa se o erro não é nulo, se for nulo é pq o back-end não está rodando
                if (errorResponse != null)
                {
                    // Pegando o retorno com erro da requisição
                    try
                    {
                        error = errorResponse.getString("message");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.e("error", "onFailure: " + errorResponse);

                    //Conferindo qual o erro retornado e printando conforme a condição
                    if (error.equals("Invalid Credentials"))
                    {
                        Toast.makeText(contextApp, "Email ou senha inválidos!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(contextApp, "Erro ao realizar login \n tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Log.e("error", "onFailure: " + errorResponse);
                    Toast.makeText(contextApp, " Problemas no servidor \n Tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                }
                //Retorna "falso" para a tela de login
                callback.onRequisitionResult(false);
            }
        });
    }

    public void registerFuncionario(String email, String password, String name)
    {
        JSONObject jsonParams = new JSONObject();
        try
        {
            jsonParams.put("email", email);
            jsonParams.put("password", password);
            jsonParams.put("name", name);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.post(contextApp, registerURL, entity, "application/json", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);

                jsonResponse = response.toString();

                Log.i("success", "onSuccess: " + jsonResponse);
                Toast.makeText(contextApp, "Conta Cadastrada, realize seu login", Toast.LENGTH_LONG).show();

                callback.onRequisitionResult(true);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("error", "onFailure: " + errorResponse);
                if (errorResponse != null)
                {
                    Toast.makeText(contextApp, "Erro ao realizar cadastro \n Tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(contextApp, " Problemas no servidor \n Tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                }
                callback.onRequisitionResult(false);
            }
        });
    }

    public void resetPassword(String tokenJWT, String newPassword)
    {
        asyncHttpClient = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();
        try
        {
            jsonParams.put("passwordResetToken", tokenJWT);
            jsonParams.put("newPassword", newPassword);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        asyncHttpClient.post(contextApp, resetPasswordURL, entity, "application/json", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                jsonResponse = response.toString();
                Log.i("sucesso", "onSuccess: " + jsonResponse);
                Toast.makeText(contextApp, "Senha alterada", Toast.LENGTH_LONG).show();

                callback.onRequisitionResult(true);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("error", "onFailure: " + errorResponse);

                if (errorResponse != null)
                {
                    Toast.makeText(contextApp, "Erro ao alterar a senha, verifique o código e tente novamente.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(contextApp, "Erro ao alterar a senha, verifique o código e tente novamente.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showNameFuncionario()
    {
        asyncHttpClient = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("jwt", contextApp.getCurrentTokenJWT());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        String token = "jwt=" + contextApp.getCurrentTokenJWT();
        asyncHttpClient.addHeader("Cookie", token);

        asyncHttpClient.get(contextApp, getFucionarioDataURL, entity, "application/json", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);

                jsonResponse = response.toString();
                JSONObject jsonObject;

                Log.i("success", "onSuccess: " + jsonResponse);

                try
                {
                    jsonObject = new JSONObject(jsonResponse);
                    email = jsonObject.getString("email");
                    name = jsonObject.getString("name");
                    id = jsonObject.getInt("id");
                    active = jsonObject.getBoolean("active");
                    staff = jsonObject.getBoolean("staff");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Funcionario funcionario = new Funcionario(id, email, name, active, staff);
                funcionarioCallback.onRequisitionFuncionarioResult(funcionario);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Funcionario funcionario = new Funcionario(1, "email", "name", false, false);
                funcionarioCallback.onRequisitionFuncionarioResult(funcionario);
                Log.e("error", "onFailure: " + errorResponse);
            }
        });
    }

    public void logout()
    {
        asyncHttpClient = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("jwt", contextApp.getCurrentTokenJWT());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        String token = "jwt=" + contextApp.getCurrentTokenJWT();
        asyncHttpClient.addHeader("Cookie", token);

        asyncHttpClient.post(contextApp, logoutURL, entity, "application/json", new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                jsonResponse = response.toString();

                Log.i("success", "onSuccess: " + jsonResponse);
                callback.onRequisitionResult(true);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("error", "onFailure: " + errorResponse);
                callback.onRequisitionResult(false);
            }
        });
    }

    public void sendTokenResetPassword(String email)
    {
        asyncHttpClient = new AsyncHttpClient();

        String url = forgotPasswordURL + email;

        asyncHttpClient.post(url, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                Log.i("success", "Código enviado com sucesso");
                Toast.makeText(contextApp, "Código enviado!", Toast.LENGTH_LONG).show();

                callback.onRequisitionResult(true);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                if (responseString == null)
                {
                    Log.e("error", "Servidor não está rodando: " + responseString);
                    Toast.makeText(contextApp, " Problemas no servidor \n Tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (responseString.equals("User not found"))
                    {
                        Log.e("error", "onFailure: " + responseString);
                        Toast.makeText(contextApp, "Usuário inexistente", Toast.LENGTH_LONG).show();
                    }
                    Log.e("error", "onFailure: " + responseString);
                    Toast.makeText(contextApp, "Erro ao enviar código, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                }
                callback.onRequisitionResult(false);
            }
        });
    }

    public void registerPonto()
    {
        asyncHttpClient = new AsyncHttpClient();

        String url = registerPontoURL + contextApp.getCurrentTokenJWT() + "&in_out=" + contextApp.getEntradaOuSaida();

        asyncHttpClient.post(url, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                Log.i("success", "onSuccess: " + response.toString());

                callback.onRequisitionResult(true);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("error", "onFailure: " + errorResponse);

                callback.onRequisitionResult(false);
            }
        });
    }

    public List<Ponto> getPonto()
    {
        asyncHttpClient = new AsyncHttpClient();
        List<Ponto> listaPontos = new ArrayList<>();

        String url = getPontoURL + contextApp.getCurrentTokenJWT()+ "&today=" + true;
        asyncHttpClient.get(url, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response)
            {
                Log.i("Get pontoooo", "onSuccess: " + response.toString());

                if(response.toString() != "[]")
                {
                    try
                    {
                        for(int x=0;  x < response.length(); x++)
                        {
                            if (x == 4)
                            {
                                break;
                            }
                            JSONObject jsonConsulta = response.getJSONObject(x);
                            String idString = jsonConsulta.getString("id");
                            int pontoId = Integer.parseInt(idString);
                            String userIdString = jsonConsulta.getString("user_id");
                            int userId = Integer.parseInt(userIdString);
                            String dataPonto = jsonConsulta.getString("dateTime");

                            LocalDateTime dataHora = LocalDateTime.parse(dataPonto, DateTimeFormatter.ISO_DATE_TIME);

                            DateTimeFormatter formatoPersonalizado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                            // Formatar a data e hora
                            String dataHoraFormatada = dataHora.format(formatoPersonalizado);

                            boolean in_out;
                            if(x % 2 == 0)
                            {
                                in_out = true;
                            }
                            else
                            {
                                in_out = false;
                            }

                            Ponto ponto = new Ponto(pontoId, userId, dataHoraFormatada, in_out);
                            listaPontos.add(ponto);
                        }
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("Lista vaiza");
                }

                pontosCallback.onRequisitionPontosResult(listaPontos);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("error", "onFailure: " + errorResponse);

                pontosCallback.onRequisitionPontosResult(listaPontos);
            }
        });
        System.out.println("Teste");
        return listaPontos;
    }
}