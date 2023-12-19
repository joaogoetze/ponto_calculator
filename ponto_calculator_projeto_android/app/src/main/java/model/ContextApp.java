package model;

import android.app.Application;

public class ContextApp extends Application
{
    private String currentTokenJWT;
    private boolean entradaOuSaida = true;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public String getCurrentTokenJWT()
    {
        return currentTokenJWT;
    }

    public void setCurrentTokenJWT(String currentTokenJWT)
    {
        this.currentTokenJWT = currentTokenJWT;
    }

    public boolean getEntradaOuSaida(){ return entradaOuSaida;}

    public void setEntradaOuSaida(boolean entradaOuSaida)
    {
        this.entradaOuSaida = entradaOuSaida;
    }
}