package de.sitescrawler.jpa.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

@ApplicationScoped
@Named
@Transactional
public class QuellenManager implements IQuellenManager
{

    private List<Quelle>  quellen = new ArrayList<>();
    
    private Map<Integer, Quelle> bekannteQuellen = new HashMap<>();
    
    @PersistenceContext
    private EntityManager entityManager;

    public QuellenManager()
    {
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
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
    @Transactional(value = TxType.REQUIRED)
    public void erstelleQuelle(Quelle quelle)
    {
        this.speichereQuelle(quelle);
        this.quellen.add(quelle);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void modifiziereQuelle(Quelle quelle)
    {
        this.speichereQuelle(quelle);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void loescheQuelle(Quelle quelle)
    {
        this.entityManager.remove(quelle);
        //TODO Lösche Artikel von Quelle aus SOLR
        this.quellen.remove(quelle);  
        bekannteQuellen.remove(quelle.getQid()); 
    }
        
	@Override
	public Quelle getQuelle(Integer id) {
		
		//Versuche die Quelle aus den bekannten Quellen zu finden.
		if(bekannteQuellen.containsKey(id)) 
			return bekannteQuellen.get(id);
		
		//Quelle war nicht bekannt, suche Quelle in Datenbank.
		TypedQuery<Quelle> query = this.entityManager.createQuery("SELECT q FROM Quelle q WHERE q.qid = :id", Quelle.class);
		query.setParameter("id", id);
		Quelle quelle = query.getSingleResult();
		bekannteQuellen.put(id, quelle);
		
		return quelle;
		
	}
}
