package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ponto_calculator.R;

public class MainActivity extends AppCompatActivity
{
    //Declaração das variáveis
    Button btnLogin, btnRegisterMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linkando as variáveis com os elementos da tela
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(clickLogin);

        btnRegisterMain = findViewById(R.id.btnRegisterMain);
        btnRegisterMain.setOnClickListener(clickCadastro);
    }

    //Ao clicar no botão "login"...
    View.OnClickListener clickLogin = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent novaTela = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(novaTela);
            Log.i("Mudança de tela","Usuário entrou na tela de login");
        }
    };

    //Ao clicar no botão "cadastre-se"...
    View.OnClickListener clickCadastro = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent novaTela = new Intent(MainActivity.this, RegisterFuncioarioActivity.class);
            startActivity(novaTela);
            Log.i("Mudança de tela","Usuário entrou na tela de cadastro");
        }
    };
}