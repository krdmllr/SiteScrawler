package de.sitescrawler.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.utility.DateUtils;

@ApplicationScoped
public class ReporterService implements IReportService
{

    @Inject
    IFiltergruppenZugriffsManager filtergruppenZugriff;
    ExecutorService               threadPool = Executors.newFixedThreadPool(5);

    @Inject
    ArchiveintragErstellen        archiveintragErstellen;

    private List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(LocalDateTime zeitpunkt)
    {
        return this.filtergruppenZugriff.getFiltergruppeMitEmpfangZuZeitpunkt(zeitpunkt);
    }

    public void generiereReports(LocalDateTime zeitpunkt)
    {
        LocalDateTime korrigierteAktuelleZeit = DateUtils.rundeZeitpunkt(zeitpunkt);

        List<Filterprofilgruppe> gruppen = this.getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(korrigierteAktuelleZeit);

        for (Filterprofilgruppe fg : gruppen)
        {
            this.archiveintragErstellen.erstelleReport(fg, korrigierteAktuelleZeit);
        }
    }

    public void generiereManuellenReport(Filterprofilgruppe profilgruppe)
    {
        this.archiveintragErstellen.erstelleReport(profilgruppe, LocalDateTime.now());

    }

}
