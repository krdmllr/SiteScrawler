package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FilterProfil
{
    private UUID         id;
    private String       Titel;
    private List<String> tags;

    public FilterProfil()
    {
        this.id = UUID.randomUUID();
        this.tags = new ArrayList<>();
    }

    public FilterProfil(String... tags)
    {
        this();
        Collections.addAll(this.tags, tags);
    }

    public UUID getId()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        FilterProfil other = (FilterProfil) obj;
        if (this.id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else
            if (!this.id.equals(other.id))
            {
                return false;
            }
        return true;
    }

    public String getTitel()
    {
        return this.Titel;
    }

    public void setTitel(String titel)
    {
        this.Titel = titel;
    }

    public void addTag(String tag)
    {
        this.tags.add(tag);
    }

    public void removeTag(String tag)
    {
        this.tags.remove(tag);
    }

    public List<String> getTags()
    {
        return this.tags;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }

}
