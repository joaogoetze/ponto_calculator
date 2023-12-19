package model;

import java.io.Serializable;

public class Funcionario implements Serializable
{
    private int id;
    private String email;
    private String name;
    private boolean active;
    private boolean staff;

    public Funcionario(int id, String email, String name, boolean active, boolean staff)
    {
        this.id = id;
        this.email = email;
        this.name = name;
        this.active = active;
        this.staff = staff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
