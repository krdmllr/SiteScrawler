package de.sitescrawler.applikation;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.qualifier.Produktiv;

@SessionScoped
@Named("profil")
public class ProfilBean implements Serializable
{

    private static final long   serialVersionUID = 1L;
    
    @Inject
    private DataBean dataBean;
    
    @Inject
    private INutzerDatenService nutzerService;
    
    public Nutzer getNutzer(){
    	return nutzerService.getNutzer();
    }
    
    public void setNeuesPasswort(String neuesPasswort, String altesPasswort){
    	if(!isPasswortValide(altesPasswort)) return;
    	
    	nutzerService.aenderePasswort(neuesPasswort, altesPasswort);
    }
    
    public void setNeueEmail(String email, String aktuellesPasswort){
    	if(!isPasswortValide(aktuellesPasswort)) return;
    	
    	nutzerService.aendereEmailAdresse(email, aktuellesPasswort);
    }

    public void setEmpfangeHtmlEmails(boolean empfangeHtmlEmail){
    	
    	nutzerService.aendereEmpfangeHtmlEmails(empfangeHtmlEmail);
    }
    
    public void loescheNutzer(String aktuellesPasswort){
    	
    	 if(!isPasswortValide(aktuellesPasswort)) return;
    	 
    	 nutzerService.loescheNutzer(aktuellesPasswort);
    	
    }
    
    private boolean isPasswortValide(String aktuellesPasswort){
    	return nutzerService.istPasswortValide(aktuellesPasswort);
    }
}
