package de.sitescrawler.jpa.management.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.IFiltergruppenZugriffsManager;
import de.sitescrawler.utility.FiltergruppenZeitpunktCheckUtil;

public class FiltergruppenZugriffsManager implements IFiltergruppenZugriffsManager {

	@Override
	public List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuZeitpunkt(LocalDateTime empfangszeitpunkt) {
		
		List<Filterprofilgruppe> alleFiltergruppen = null; //TODO MARCEL DATENBANK
		List<Filterprofilgruppe> relevanteFiltergruppen = new ArrayList();
		
		for(Filterprofilgruppe filtergruppe: alleFiltergruppen){
			FiltergruppenZeitpunktCheckUtil checkUtil = new FiltergruppenZeitpunktCheckUtil(filtergruppe, empfangszeitpunkt);
			if(checkUtil.sollArchiveintragErstelltWerden())
				relevanteFiltergruppen.add(filtergruppe);
		}
		
		return null;
	}

	@Override
	public void speicherArchiveintrag(Archiveintrag archiveintrag, Filterprofilgruppe filtergruppe) {
		// TODO Auto-generated method stub
		
	}
	
	

}
