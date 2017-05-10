package de.sitescrawler.jpa.management;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.management.interfaces.IFirmenManager;

@ApplicationScoped
public class FirmenManager implements IFirmenManager
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void speichereAenderungen(Firma firma)
    {
        this.entityManager.merge(firma);
    }

}
