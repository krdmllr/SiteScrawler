package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
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
    
    @Inject
    private ICrawlerLaufService crawlerService;
    
    private Quelle gewaehlteQuelle; 
     
    private Quelle neueQuelle; 
    
    private Quelle gewaehlteQuelleKopie;

	@PostConstruct
    public void init(){
    	setzeDefaultQuelle();
    }
    
    private void setzeDefaultQuelle(){
    	if(getQuellen() != null && !getQuellen().isEmpty())
    		setGewaehlteQuelle(getQuellen().get(0));
    }
    
    public void crawleManuell(){
    	crawlerService.crawl();
    }
    
    public List<Quelle> getQuellen(){
    	List<Quelle> quellen = quellenManager.getQuellen();
    	return quellen;
    }

	public Quelle getGewaehlteQuelle() {
		return gewaehlteQuelle;
	}

	public void setGewaehlteQuelle(Quelle geweahlteQuelle) {
		gewaehlteQuelleKopie = new Quelle(geweahlteQuelle.getName(), geweahlteQuelle.getBild(), geweahlteQuelle.getRsslink(), geweahlteQuelle.getTagOderId(), geweahlteQuelle.getFilterprofile()); 
		this.gewaehlteQuelle = geweahlteQuelle;
	}
	
	public void starteQuellenErstellen(){
		neueQuelle = new Quelle();
	}
	
	public void verwerfeAenderungen(){
		gewaehlteQuelle.setName(gewaehlteQuelleKopie.getName());
		gewaehlteQuelle.setRsslink(gewaehlteQuelleKopie.getRsslink());
		gewaehlteQuelle.setTagOderId(gewaehlteQuelleKopie.getTagOderId());
	} 
	
	public void speichereAenderung(){
		quellenManager.modifiziereQuelle(gewaehlteQuelle);
		setGewaehlteQuelle(getGewaehlteQuelle());
	}
	
	public void uebernehmeNeueQuelle(){
		quellenManager.erstelleQuelle(neueQuelle);
		neueQuelle = new Quelle();
	    setzeDefaultQuelle();
	}
	
	public void loescheQuelle(){
		quellenManager.loescheQuelle(gewaehlteQuelle);
		setGewaehlteQuelle(null);
		
		setzeDefaultQuelle();
	}
	
	public boolean zeigeLoeschButton(){
		return true;
	}
	
	public boolean zeigeVerwerfenButton(){
		return wurdeQuelleVeraendert();
	}
	
	public boolean zeigeNeueQuelleVerwerfenButton(){
		if(neueQuelle == null) return false;
		
		if(neueQuelle.getName() != null && !neueQuelle.getName().isEmpty()) return true;
		
		if(neueQuelle.getRsslink() != null && !neueQuelle.getRsslink().isEmpty()) return true;
		
		return false;
	}
	
	public boolean zeigeErstellenButton(){ 
		if(neueQuelle == null) return false;
		
		if(neueQuelle.getName() == null || neueQuelle.getName().isEmpty()) return false;
		
		if(neueQuelle.getRsslink() == null || neueQuelle.getRsslink().isEmpty()) return false;
		
		return true;
	}
	
	public boolean zeigeQuelleAn(){
		return getGewaehlteQuelle() != null;
	}
	
	public boolean zeigeSpeichernButton(){
		return wurdeQuelleVeraendert();
	} 
	
	private boolean wurdeQuelleVeraendert(){
		
		if(gewaehlteQuelleKopie == null || gewaehlteQuelle == null) return false;
		
		if(!istStringGleich(gewaehlteQuelle.getName(),gewaehlteQuelleKopie.getName())) return true;
		if(!istStringGleich(gewaehlteQuelle.getRsslink(),gewaehlteQuelleKopie.getRsslink())) return true;
		if(!istStringGleich(gewaehlteQuelle.getTagOderId(),gewaehlteQuelleKopie.getTagOderId())) return true;
		
		return false;
	}
	
	private boolean istStringGleich(String string1, String string2){
		if(string1 == null && string2 == null) return true;
		
		if(string1 == null && string2.isEmpty()) return true;
		
		if(string1.isEmpty() && string2 == null) return true;
		
		if(string1 == null || string2 == null) return false;
		
		if(string1.equals(string2)) return true;
		
		return false; 
	}
	
    public Quelle getNeueQuelle() {
		return neueQuelle;
	}

	public void setNeueQuelle(Quelle neueQuelle) {
		this.neueQuelle = neueQuelle;
	}
}
