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

public class AlterPasswordActivity extends AppCompatActivity
{
    ContextApp contextApp;

    Button btnResetPassword;

    EditText edtCodeResetToken, edtNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_password);

        btnResetPassword = findViewById(R.id.btnResetPassword);
        edtCodeResetToken = findViewById(R.id.edtCodeResetToken);
        edtNewPassword = findViewById(R.id.edtNewPassword);

        contextApp = (ContextApp)getApplicationContext();

        btnResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(edtCodeResetToken.getText().length()==0)
                {
                    edtCodeResetToken.setError("Informe o código!");
                    edtCodeResetToken.requestFocus();
                }
                else if(edtNewPassword.getText().length()==0)
                {
                    edtNewPassword.setError("Informe sua nova senha!");
                    edtNewPassword.requestFocus();
                }
                else
                {
                    String tokenJWT = edtCodeResetToken.getText().toString();
                    String newPassword = edtNewPassword.getText().toString();

                    Controller controller = new Controller(contextApp, new RequisitionCallback()
                    {
                        @Override
                        public void onRequisitionResult(boolean result)
                        {
                            if(result)
                            {
                                Intent it = new Intent(AlterPasswordActivity.this, LoginActivity.class);
                                startActivity(it);
                                finish();
                            }
                        }
                    });
                    controller.resetPassword(tokenJWT, newPassword);
                }
            }
        });
    }
}