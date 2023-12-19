package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ponto_calculator.R;

import controller.Controller;
import controller.RequisitionCallback;

import model.ContextApp;

public class RecoverPasswordActivity extends AppCompatActivity
{
    Button btnSendToken;

    EditText edtEmailSendToken;

    ContextApp contextApp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        btnSendToken = findViewById(R.id.btnSendToken);

        edtEmailSendToken = findViewById(R.id.edtEmailSendToken);

        contextApp = (ContextApp) getApplicationContext();

        btnSendToken.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(edtEmailSendToken.getText().length()==0)
                {
                    edtEmailSendToken.setError("Informe seu e-mail!");
                    edtEmailSendToken.requestFocus();
                }
                else
                {
                    String email = edtEmailSendToken.getText().toString();

                    Controller controller = new Controller(contextApp, new RequisitionCallback()
                    {
                        @Override
                        public void onRequisitionResult(boolean result)
                        {
                            if(result)
                            {
                                Intent it = new Intent(RecoverPasswordActivity.this, AlterPasswordActivity.class);
                                startActivity(it);
                            }
                        }
                    });
                    controller.sendTokenResetPassword(email);
                }
            }
        });
    }
}