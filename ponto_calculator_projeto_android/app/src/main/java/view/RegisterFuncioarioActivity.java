package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ponto_calculator.R;

import controller.Controller;
import controller.RequisitionCallback;

import model.ContextApp;

public class RegisterFuncioarioActivity extends AppCompatActivity
{

    //Declaração das variáveis
    EditText edtNameRegister, edtEmailRegister, edtPasswordRegister;

    Button btnRegister;

    String email, password, name;

    ContextApp contextApp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_funcionario);

        //Linkando as variáveis com os elementos da tela
        edtNameRegister = findViewById(R.id.edtNameRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);

        //Colocando o contexto da aplicação no objeto contextApp
        contextApp = (ContextApp) getApplicationContext();

        btnRegister = findViewById(R.id.btnRegister);

        //Ao clicar no botão "cadastre-se"...
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Testes condicionais de preenchimento dos campos pelo usuário
                if(edtNameRegister.getText().length()==0)
                {
                    edtNameRegister.setError("Informe seu nome!");
                    edtNameRegister.requestFocus();
                }
                else if(edtEmailRegister.getText().length()==0)
                {
                    edtEmailRegister.setError("Informe seu e-mail!");
                    edtEmailRegister.requestFocus();
                }
                else if(edtPasswordRegister.getText().length()==0)
                {
                    edtPasswordRegister.setError("Informe uma senha!");
                    edtPasswordRegister.requestFocus();
                }
                else
                {
                    email = edtEmailRegister.getText().toString();
                    password = edtPasswordRegister.getText().toString();
                    name = edtNameRegister.getText().toString();

                    Log.i("Cadastro","Usuário preencheu os campos e clicou no botão de cadastro");

                    Controller controller = new Controller(contextApp, new RequisitionCallback()
                    {
                        @Override
                        public void onRequisitionResult(boolean result)
                        {
                            if (result)
                            {
                                Intent it = new Intent(RegisterFuncioarioActivity.this, LoginActivity.class);
                                startActivity(it);
                            }
                        }
                    });
                    controller.registerFuncionario(email, password, name);
                }
            }
        });
    }
}