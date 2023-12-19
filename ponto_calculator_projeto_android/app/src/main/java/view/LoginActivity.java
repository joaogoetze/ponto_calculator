package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import controller.Controller;
import controller.RequisitionCallback;

import model.ContextApp;

import com.example.ponto_calculator.R;

public class LoginActivity extends AppCompatActivity
{
    //Declaração das variáveis
    EditText edtEmailLogin, edtPasswordLogin;

    TextView tvForgotPassword;

    Button btnLogin;

    String email, password;

    ContextApp contextApp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Linkando as variáveis com os elementos da tela
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin = findViewById(R.id.btnLogin);

        //Colocando o contexto da aplicação no objeto contextApp
        contextApp = (ContextApp) getApplicationContext();

        //Ao clicar no botão "login"...
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Testes condicionais de preenchimento dos campos pelo usuário
                if(edtEmailLogin.getText().length()==0)
                {
                    edtEmailLogin.setError("Informe seu e-mail!");
                    edtEmailLogin.requestFocus();
                }
                else if (edtPasswordLogin.getText().length()==0)
                {
                    edtPasswordLogin.setError("Informe sua senha!");
                    edtPasswordLogin.requestFocus();
                }
                else
                {
                    //Pegando o que foi digitado nos campos
                    email = edtEmailLogin.getText().toString();
                    password = edtPasswordLogin.getText().toString();

                    Log.i("Login","Usuário preencheu os campos e clicou no botão de login");

                    //Instanciando um objeto controller com o contexto da aplicação e uma função de callback

                    Controller controller = new Controller(contextApp, new RequisitionCallback()
                    {
                        @Override
                        public void onRequisitionResult(boolean result)
                        {
                            if (result)
                            {
                                Intent it = new Intent(LoginActivity.this, RegisterPontoActivity.class);
                                startActivity(it);
                            }
                        }
                    });
                    controller.loginValidate(email, password);
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i("Troca de tela", "Usuário entrou na tela de recuperação de senha");
                Intent it = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
}