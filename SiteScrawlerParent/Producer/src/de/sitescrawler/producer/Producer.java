package de.sitescrawler.producer;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class Producer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Produces
	@PersistenceContext
	private EntityManager entityManager;
}
