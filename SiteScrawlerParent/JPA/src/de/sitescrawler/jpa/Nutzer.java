package de.sitescrawler.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the nutzer database table.
 *
 */
@Entity
@NamedQuery(name = "Nutzer.findAll", query = "SELECT n FROM Nutzer n")
@NamedEntityGraph(name = "NutzerFull", includeAllAttributes = true)
public class Nutzer implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    private String            uid;

    private String            email;

    private String            vorname;

    private String            nachname;

    private String            passwort;

    // bi-directional many-to-many association to Rolle
    @ManyToMany
    @JoinColumn(name = "uid")
    private List<Rolle>       rollen;

    public Nutzer()
    {
    }

    public String getUid()
    {
        return this.uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getVorname()
    {
        return this.vorname;
    }

    public void setVorname(String vorname)
    {
        this.vorname = vorname;
    }

    public String getNachname()
    {
        return this.nachname;
    }

    public void setNachname(String nachname)
    {
        this.nachname = nachname;
    }

    public String getPasswort()
    {
        return this.passwort;
    }

    public void setPasswort(String passwort)
    {
        this.passwort = passwort;
    }

    public List<Rolle> getRollen()
    {
        return this.rollen;
    }

    public void setRollen(List<Rolle> rollen)
    {
        this.rollen = rollen;
    }

    public String getGanzerName()
    {
        return this.vorname + " " + this.nachname;
    }

}