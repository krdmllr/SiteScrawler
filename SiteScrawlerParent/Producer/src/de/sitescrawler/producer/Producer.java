package de.sitescrawler.producer;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.sitescrawler.nutzerverwaltung.NutzerDatenService;
import de.sitescrawler.nutzerverwaltung.NutzerDatenServiceDummy;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.qualifier.DummyDaten;
import de.sitescrawler.qualifier.Produktiv;

@ApplicationScoped
public class Producer implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Produces
    @Produktiv
    @PersistenceContext
    private EntityManager     entityManager;

    @Produces
    @Produktiv
    @SessionScoped
    public INutzerDatenService getNutzerDatenServiceProd()
    {
        return new NutzerDatenService();
    }

    @Produces
    @DummyDaten
    @SessionScoped
    public INutzerDatenService getNutzerDatenServiceDummy()
    {
        return new NutzerDatenServiceDummy();
    }
}
