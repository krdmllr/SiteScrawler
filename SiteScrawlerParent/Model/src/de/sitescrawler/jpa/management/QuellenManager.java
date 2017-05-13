package de.sitescrawler.jpa.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Transactional
public class QuellenManager implements IQuellenManager
{

    private List<Quelle>         quellen         = new ArrayList<>();

    private Map<Integer, Quelle> bekannteQuellen = new HashMap<>();

    @PersistenceContext
    private EntityManager        entityManager;

    public QuellenManager()
    {
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
    
    @Override
    public void ladeQuellenEin(){
    	this.quellen = this.getAlleQuellen();
    }

    @Transactional(value = TxType.REQUIRED)
    private List<Quelle> getAlleQuellen()
    {
        TypedQuery<Quelle> query = this.entityManager.createNamedQuery("Quellen.findAll", Quelle.class);
        List<Quelle> quellen = query.getResultList();
        return quellen;
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void erstelleQuelle(Quelle quelle)
    {
        this.entityManager.persist(quelle);
        this.quellen.add(quelle);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void modifiziereQuelle(Quelle quelle)
    {
        this.entityManager.merge(quelle);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void loescheQuelle(Quelle quelle)
    {
        quelle = this.entityManager.find(Quelle.class, quelle.getQid());
        this.entityManager.remove(quelle);
        // TODO Lösche Artikel von Quelle aus SOLR
        this.quellen.remove(quelle);
        this.bekannteQuellen.remove(quelle.getQid());
    }

    @Override
    public Quelle getQuelle(Integer id)
    {

        // Versuche die Quelle aus den bekannten Quellen zu finden.
        if (this.bekannteQuellen.containsKey(id))
        {
            return this.bekannteQuellen.get(id);
        }

        // Quelle war nicht bekannt, suche Quelle in Datenbank.
        TypedQuery<Quelle> query = this.entityManager.createQuery("SELECT q FROM Quelle q WHERE q.qid = :id", Quelle.class);
        query.setParameter("id", id);
        Quelle quelle = query.getSingleResult();
        this.bekannteQuellen.put(id, quelle);

        return quelle;

    }
}
