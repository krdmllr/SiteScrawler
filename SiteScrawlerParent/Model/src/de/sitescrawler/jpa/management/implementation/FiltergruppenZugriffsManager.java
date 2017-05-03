package de.sitescrawler.jpa.management.implementation;

import java.time.LocalDateTime;
import java.util.List;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.IFiltergruppenZugriffsManager;

public class FiltergruppenZugriffsManager implements IFiltergruppenZugriffsManager {

	@Override
	public List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuZeitpunkt(LocalDateTime empfangszeitpunkt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void speicherArchiveintrag(Archiveintrag archiveintrag, Filterprofilgruppe filtergruppe,
			LocalDateTime erstellungsZeitpunkt) {
		// TODO Auto-generated method stub
		
	}

}
