package de.sitescrawler.model;

import de.sitescrawler.jpa.Nutzer;

public class Mitarbeiter
{

    private Boolean isAdmin;
    private Nutzer  nutzeraccount;

    public Boolean getIsAdmin()
    {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public Nutzer getNutzeraccount()
    {
        return this.nutzeraccount;
    }

    public void setNutzeraccount(Nutzer nutzeraccount)
    {
        this.nutzeraccount = nutzeraccount;
    }
}
