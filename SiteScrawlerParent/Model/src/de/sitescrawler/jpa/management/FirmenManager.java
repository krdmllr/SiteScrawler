package de.sitescrawler.jpa.management;

import javax.enterprise.context.ApplicationScoped;

import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.management.interfaces.IFirmenManager;

@ApplicationScoped
public class FirmenManager implements IFirmenManager{

	@Override
	public void speichereAenderungen(Firma firma) {
		// TODO Auto-generated method stub
		
	}

}
