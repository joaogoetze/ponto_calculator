package view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ponto_calculator.R;

import java.util.ArrayList;
import java.util.List;

import controller.Controller;
import controller.PontoAdapter;
import controller.RequisitionCallback;
import controller.RequisitionFuncionarioCallback;

import controller.RequisitionPontosCallback;
import model.ContextApp;
import model.Funcionario;
import model.Ponto;

public class RegisterPontoActivity extends AppCompatActivity
{
    ContextApp contextApp;
    Button btnRegisterPonto;
    List<Ponto> listaPontos = new ArrayList<>();
    TextView tvSalutation, tvLogout;
    long buttonClickTime = 0, delay_millis = 860000;

    private RecyclerView recyclerView;
    private PontoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ponto);

        contextApp = (ContextApp)getApplicationContext();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvSalutation = findViewById(R.id.tvSalutation);
        tvLogout = findViewById(R.id.tvLogout);

        btnRegisterPonto = findViewById(R.id.btnRegisterPonto);

        Controller controller = new Controller(contextApp, new RequisitionFuncionarioCallback()
        {
            @Override
            public void onRequisitionFuncionarioResult(Funcionario funcionario)
            {
                    tvSalutation.setText(("Olá " + funcionario.getName() + "!"));
            }
        });
        controller.showNameFuncionario();

        Controller controller3 = new Controller(contextApp, new RequisitionPontosCallback()
        {
            @Override
            public void onRequisitionPontosResult(List<Ponto> listaPontos)
            {
                System.out.println(listaPontos.toString());
                adapter = new PontoAdapter(contextApp, listaPontos);
                recyclerView.setAdapter(adapter);
            }
        });
        listaPontos = controller3.getPonto();
        System.out.println("Pontos retornados da função: " + listaPontos);

        btnRegisterPonto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Controller controller1 = new Controller(contextApp, new RequisitionCallback()
                {
                    @Override
                    public void onRequisitionResult(boolean result) {
                        if(result)
                        {
                            Log.d("Registrar ponto", "Verdadeiro");
                            if(contextApp.getEntradaOuSaida())
                            {
                                Controller controller4 = new Controller(contextApp, new RequisitionPontosCallback()
                                {
                                    @Override
                                    public void onRequisitionPontosResult(List<Ponto> listaPontos)
                                    {
                                        System.out.println(listaPontos.toString());
                                        adapter = new PontoAdapter(contextApp, listaPontos);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                                listaPontos = controller4.getPonto();
                                contextApp.setEntradaOuSaida(false);
                            }
                            else
                            {
                                contextApp.setEntradaOuSaida(true);
                            }
                        }
                    }
                });
                controller1.registerPonto();
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Controller controller02 = new Controller(contextApp, new RequisitionCallback()
                {
                    @Override
                    public void onRequisitionResult(boolean result)
                    {
                        if(result)
                        {
                            finish();
                        }
                    }
                });
                controller02.logout();
            }
        });

        Handler handler = new Handler();
        buttonClickTime = System.currentTimeMillis();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                long currentTime = System.currentTimeMillis();
                System.out.println(currentTime);
                if(currentTime - buttonClickTime >= delay_millis)
                {
                    finish();
                }
            }
        }, delay_millis);
    }
}