package de.sitescrawler.jpa.management;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Filtermanager;
import de.sitescrawler.jpa.management.interfaces.IFilterManagerManager;

@ApplicationScoped
public class FiltermanagerManager implements IFilterManagerManager
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void speichereAenderung(Filtermanager filterManager)
    {
        this.entityManager.merge(filterManager);
    }

}
