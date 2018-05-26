package com.screamatthewind.health;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.screamatthewind.db.HealthUpdateDAO;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

public class HealthCheckMonitorService implements Managed {

	Environment environment = null;
	HibernateBundle hibernateBundle;
	DataSource dataSource;
	HealthUpdateDAO daoUpdateHealth;

    public HealthCheckMonitorService(Environment environment, HibernateBundle hibernateBundle,  DataSource dataSource, HealthUpdateDAO daoUpdateHealth) {
		this.environment = environment;
		this.hibernateBundle = hibernateBundle;
		this.dataSource = dataSource;
		this.daoUpdateHealth = daoUpdateHealth;
    }
    
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

    @Override
    public void start() throws Exception {
        System.out.println("Starting jobs");
        service.scheduleAtFixedRate(new HealthCheckMonitorJob(environment, hibernateBundle, dataSource, daoUpdateHealth), 1, 10, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Shutting down");
        service.shutdown();
    }
}