package de.sitescrawler.jpa.management.interfaces;

import java.util.List;

import de.sitescrawler.jpa.Quelle;

public interface IQuellenManager {

	public List<Quelle> getQuellen();
	
	public void erstelleQuelle(Quelle quelle);
	
	public void modifiziereQuelle(Quelle quelle);
	
	public void loescheQuelle(Quelle quelle);
	
	public Quelle getQuelle(Integer id);
}
