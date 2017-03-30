package de.sitescrawler.applikation;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.sitescrawler.model.Archiveintrag;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named("archiv")
public class ArchivBean implements Serializable {

	private static final long serialVersionUID = 1L;

	 public List<Archiveintrag> getArchiveintraege(){
	 List<Archiveintrag> list = new ArrayList();
	
	 for (int i = 0; i < 20; i++) {
	 Archiveintrag eintrag = new Archiveintrag();
	 eintrag.setErstellungsDatum(LocalDateTime.now());
	 list.add(eintrag);
	 }
	
	 return list;
	 }
}
