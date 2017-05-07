package de.sitescrawler.jpa.management;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

@ApplicationScoped
@Named
public class QuellenManager implements IQuellenManager {

	List<Quelle> quellen; 
	
	public QuellenManager(){
		quellen = new ArrayList<>();
		Quelle spiegelQuelle = new Quelle("Spiegel", "http://www.spiegel.de/schlagzeilen/tops/index.rss");
		quellen.add(spiegelQuelle);
	}
	
	@Override
	public List<Quelle> getQuellen() {


		return quellen;
	}

	@Override
	public void erstelleQuelle(Quelle quelle) {
		quellen.add(quelle);
	}

	@Override
	public void modifiziereQuelle(Quelle quelle) {
		
		
	}

	@Override
	public void loescheQuelle(Quelle quelle) {
		quellen.remove(quelle);
		
	}  
}
