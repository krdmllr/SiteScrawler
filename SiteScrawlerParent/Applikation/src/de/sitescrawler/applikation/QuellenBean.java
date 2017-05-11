package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

@SessionScoped
@Named("quellen")
public class QuellenBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int uiStatus; //0 = Normal; 1 = Bearbeiten; 2 = Erstellen; 
	
    @Inject
    private DataBean          dataBean;
    
    @Inject
    private IQuellenManager quellenManager;
    
    private Quelle gewaehlteQuelle; 
     
    private Quelle neueQuelle;
    
    @PostConstruct
    public void init(){
    	setzeDefaultQuelle();
    }
    
    private void setzeDefaultQuelle(){
    	if(getQuellen() != null && !getQuellen().isEmpty())
    		setGewaehlteQuelle(getQuellen().get(0));
    }
    
    public List<Quelle> getQuellen(){
    	List<Quelle> quellen = quellenManager.getQuellen();
    	return quellen;
    }

	public Quelle getGewaehlteQuelle() {
		return gewaehlteQuelle;
	}

	public void setGewaehlteQuelle(Quelle geweahlteQuelle) {
		 
		this.gewaehlteQuelle = geweahlteQuelle;
	}
	
	public void starteQuellenErstellen(){
		neueQuelle = new Quelle();
		gewaehlteQuelle = neueQuelle;
		setUiStatus(2);
	}
	
	public void verwerfeErstellteQuelle(){
		if(uiStatus == 1)
		{
			//Verwerfe Änderungen
		}
		else{
			//Verwerfe neue Quelle
			setGewaehlteQuelle(null);
			setzeDefaultQuelle();
			setUiStatus(1);
			neueQuelle = null;
		} 
	}
	
	public void speichereAenderung(){
		quellenManager.modifiziereQuelle(gewaehlteQuelle);
	}
	
	public void uebernehmeNeueQuelle(){
		quellenManager.erstelleQuelle(neueQuelle);
		setGewaehlteQuelle(neueQuelle);
		setUiStatus(0);
	}
	
	public void loescheQuelle(){
		quellenManager.loescheQuelle(gewaehlteQuelle);
		setGewaehlteQuelle(null);
		
		setzeDefaultQuelle();
		setUiStatus(1);
	}
	
	public boolean zeigeLoeschButton(){
		if(uiStatus <= 1) return true;
		return false;
	}
	
	public boolean zeigeVerwerfenButton(){
		if(uiStatus >= 1) return true;
		return false;
	}
	
	public boolean zeigeErstellenButton(){
		if(uiStatus != 2) return false;
		
		if(neueQuelle.getName() == null || neueQuelle.getName().isEmpty()) return false;
		
		if(neueQuelle.getRsslink() == null || neueQuelle.getRsslink().isEmpty()) return false;
		
		return true;
	}
	
	public boolean zeigeQuelleAn(){
		return getGewaehlteQuelle() != null;
	}
	
	public boolean zeigeSpeichernButton(){
		if(uiStatus == 1) return true;
		
		return false;
	}

	public int getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(int uiStatus) {
		System.out.println("Status: " + uiStatus);
		this.uiStatus = uiStatus;
	} 
}
