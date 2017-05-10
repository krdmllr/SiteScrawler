package de.sitescrawler.jpa.management;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

@ApplicationScoped
@Named
public class QuellenManager implements IQuellenManager
{

    private List<Quelle>  quellen = new ArrayList<>();
    @PersistenceContext
    private EntityManager entityManager;

    public QuellenManager()
    {
        // TODOD Remove falls db zugriff gew�nscht.
        // this.quellen = new ArrayList<>();
        // Quelle spiegelQuelle = new Quelle("Spiegel", "http://www.spiegel.de/schlagzeilen/tops/index.rss");
        // this.quellen.add(spiegelQuelle);
    }

    @Override
    public List<Quelle> getQuellen()
    {
        if (this.quellen.isEmpty())
        {
            this.quellen = this.getAlleQuellen();
        }
        return this.quellen;
    }

    private List<Quelle> getAlleQuellen()
    {
        TypedQuery<Quelle> query = this.entityManager.createNamedQuery("Quellen.findAll", Quelle.class);
        List<Quelle> quellen = query.getResultList();
        return quellen;
    }

    @Transactional(value = TxType.REQUIRED)
    private void speichereQuelle(Quelle quelle)
    {
        this.entityManager.merge(quelle); 
    }

    @Override
    public void erstelleQuelle(Quelle quelle)
    {
        this.speichereQuelle(quelle);
        this.quellen.add(quelle);
    }

    @Override
    public void modifiziereQuelle(Quelle quelle)
    {
        this.speichereQuelle(quelle);
    }

    @Override
    public void loescheQuelle(Quelle quelle)
    {
        this.entityManager.remove(quelle);
        this.quellen.remove(quelle);

    }
}
