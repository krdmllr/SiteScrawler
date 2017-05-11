package de.sitescrawler.jpa.management;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenManager;

@ApplicationScoped
public class FiltergruppenManager implements IFiltergruppenManager
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void speichereAenderung(Filterprofilgruppe filtergruppe)
    {
        this.entityManager.merge(filtergruppe);

    }

}
