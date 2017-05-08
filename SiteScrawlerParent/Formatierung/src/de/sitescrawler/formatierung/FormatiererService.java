package de.sitescrawler.formatierung;

import javax.enterprise.context.ApplicationScoped;

import de.sitescrawler.jpa.Archiveintrag;

@ApplicationScoped
public class FormatiererService implements IFormatiererService
{

    public String generierePlaintextZusammenfassung(Archiveintrag archiveintrag)
    {
        // TODO Auto-generated method stub
        return "Plaintext lululu";
    }

    public String generiereHtmlZusammenfassung(Archiveintrag archiveintrag)
    {
        // TODO Auto-generated method stub
        return "Html lululu";
    }

}