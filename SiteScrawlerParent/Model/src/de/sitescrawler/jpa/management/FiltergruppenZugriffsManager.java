package de.sitescrawler.jpa.management;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.utility.FiltergruppenZeitpunktCheckUtil;

@ApplicationScoped
@Named
public class FiltergruppenZugriffsManager implements IFiltergruppenZugriffsManager
{
    @PersistenceContext(unitName = "Model_Persistance")
    private EntityManager entityManager;

    @Override
    public List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuZeitpunkt(LocalDateTime empfangszeitpunkt)
    {

        List<Filterprofilgruppe> alleFiltergruppen = this.getAlleFilterprofilgruppen();
        List<Filterprofilgruppe> relevanteFiltergruppen = new ArrayList<>();

        for (Filterprofilgruppe filtergruppe : alleFiltergruppen)
        {
            FiltergruppenZeitpunktCheckUtil checkUtil = new FiltergruppenZeitpunktCheckUtil(filtergruppe, empfangszeitpunkt);
            if (checkUtil.sollArchiveintragErstelltWerden())
            {
                relevanteFiltergruppen.add(filtergruppe);
            }
        }

        return relevanteFiltergruppen;
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void speicherArchiveintrag(Archiveintrag archiveintrag, Filterprofilgruppe filtergruppe)
    {
        this.entityManager.merge(archiveintrag);

    }

    private List<Filterprofilgruppe> getAlleFilterprofilgruppen()
    {
        TypedQuery<Filterprofilgruppe> query = this.entityManager.createNamedQuery("Fitlerprofilgruppen.findAll", Filterprofilgruppe.class);
        List<Filterprofilgruppe> filtergruppen = query.getResultList();
        return filtergruppen;
    }

}
