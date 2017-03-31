package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FilterProfil {
	private UUID id;
	private String Titel;
	private List<String> tags; 
	
	public FilterProfil() {
		id = UUID.randomUUID();
		this.tags = new ArrayList<>();
	}
	
	public UUID getId(){
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterProfil other = (FilterProfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
