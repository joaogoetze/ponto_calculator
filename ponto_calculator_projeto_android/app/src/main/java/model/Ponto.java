package model;

import java.io.Serializable;

public class Ponto implements Serializable
{
    private int idPonto;
    private int userId;
    private String dataPonto;
    private boolean in_out;

    public Ponto(int idPonto, int userId, String dataPonto, boolean in_out)
    {
        this.idPonto = idPonto;
        this.userId = userId;
        this.dataPonto = dataPonto;
        this.in_out = in_out;
    }

    public int getIdPonto()
    {
        return idPonto;
    }

    public void setIdPonto(int idPonto)
    {
        this.idPonto = idPonto;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getDataPonto() {
        return dataPonto;
    }

    public String isInt_outLiteral()
    {
        if(in_out)
        {
            return "Entrada";
        }
        else
        {
            return "Saida";
        }
    }

    public void setIn_out(boolean in_out) {
        this.in_out = in_out;
    }
}