package com.screamatthewind.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import com.screamatthewind.core.Health;

public class HealthUpdateDAO extends AbstractDAO<Health> {

	public HealthUpdateDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@UnitOfWork
    public boolean save() throws Exception {

    	Health health = new Health();		
    	
    	health.setLastTimeUpdated(DateTime.now());
    	
    	persist(health);

    	return true;
    }

}
