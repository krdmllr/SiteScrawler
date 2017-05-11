package de.sitescrawler.jpa.management;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.management.interfaces.IArtikelManager;

@ApplicationScoped
@Transactional
public class ArtikelManager implements IArtikelManager
{
    private static final Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");
    @PersistenceContext
    private EntityManager       entityManager;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void speichereArtikel(Artikel artikel)
    {
        ArtikelManager.LOGGER.info("Persistiere Artikel " + artikel.getTitel() + " mit Quelle " + artikel.getQuelle());
        this.entityManager.merge(artikel);
    }

}
