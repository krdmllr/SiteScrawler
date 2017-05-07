package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

@SessionScoped
@Named("quellen")
public class QuellenBean implements Serializable {

	private static final long serialVersionUID = 1L;

    @Inject
    private DataBean          dataBean;
    
    @Inject
    private IQuellenManager quellenManager;
    
    private Quelle geweahlteQuelle;
    
    public List<Quelle> getQuellen(){
    	return quellenManager.getQuellen();
    }

	public Quelle getGeweahlteQuelle() {
		return geweahlteQuelle;
	}

	public void setGeweahlteQuelle(Quelle geweahlteQuelle) {
		this.geweahlteQuelle = geweahlteQuelle;
	}
}
