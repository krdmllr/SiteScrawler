package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.List;

public class FilterProfil {
	private String Titel;
	private List<String> tags;
	
	public FilterProfil() {
		this.tags = new ArrayList<>();
	}

	public String getTitel() {
		return Titel;
	}

	public void setTitel(String titel) {
		Titel = titel;
	}
	
	public void addTag(String tag){
		this.tags.add(tag);
	}
	
	public void removeTag(String tag){
		this.tags.remove(tag);
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	
}
