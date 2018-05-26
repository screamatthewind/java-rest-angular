package com.screamatthewind.health;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Environment;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import javax.sql.DataSource;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

import com.screamatthewind.db.HealthUpdateDAO;
import com.screamatthewind.utility.ExceptionHandler;

public class HealthCheckMonitorJob implements Runnable {

	Environment environment = null;
	HibernateBundle hibernateBundle;
	DataSource dataSource;
	HealthUpdateDAO daoUpdateHealth;
	
	public HealthCheckMonitorJob(Environment environment, HibernateBundle hibernateBundle,  DataSource dataSource, HealthUpdateDAO daoUpdateHealth) {
		this.environment = environment;
		this.hibernateBundle = hibernateBundle;
		this.dataSource = dataSource;
		this.daoUpdateHealth = daoUpdateHealth;
	}
	
	@Override
    public void run() {
		
        List<String> unhealthyResults = new ArrayList<String>();
        
		try {
		
	        System.out.println(System.currentTimeMillis());
	
	        environment.healthChecks().unregister("updateHealthExternal");
	        environment.healthChecks().register("updateHealthInternal", new UpdateHealthCheck(hibernateBundle, dataSource, daoUpdateHealth, false));

	        SortedMap<String, Result> results = environment.healthChecks().runHealthChecks();
	        
	        for(SortedMap.Entry<String,Result> entry : results.entrySet()) {
	        	  String key = entry.getKey();
	        	  Result result = entry.getValue();
	
	        	  if (!result.isHealthy())
	        		  unhealthyResults.add(key);
	    	}
	        
		} catch (Exception e) {
			ExceptionHandler.handleException("run", "", e, HealthCheckMonitorJob.class);
		}
	
		try {
	        environment.healthChecks().unregister("updateHealthInternal");
	        environment.healthChecks().register("updateHealthExternal", new UpdateHealthCheck(hibernateBundle, dataSource, daoUpdateHealth, true));
		} catch (Exception e) {
			ExceptionHandler.handleException("run", "Cannot add updateHealthExternal to HealthChecks", e, HealthCheckMonitorJob.class);
		}
	
		try {
			if (unhealthyResults.size() > 0) {
				
				String text = "<html><head></head><body>";
				
				for (int i=0; i<unhealthyResults.size(); i++) {
					text += unhealthyResults.get(0).toString();
					text += ": failed<BR>";
				}
	
				text += "</body></html>";
							
				Email email = new EmailBuilder()
				    .from("SecureACall", "username@gmail.com")
				    .to("User", "user2@gmail.com")
				    .subject("SecureACall Failed Health Check")
				    .textHTML(text)
				    .build();
	
				Thread sendMail = new Thread(new Runnable() {

					public void run() {
						new Mailer("smtp.gmail.com", 587, "username@gmail.com", "password", TransportStrategy.SMTP_TLS)
						.sendMail(email);
	
						try {
							
							Connection connection = dataSource.getConnection();
				            Statement statement;
				            int rowsUpdated = 0;
				            
				            statement = connection.createStatement();
				            
				            rowsUpdated = statement.executeUpdate("update health_check set last_time_error_reported = NOW()");
		
				            if (rowsUpdated != 1) {
					        	ExceptionHandler.handleException("check", "No rows updated while updating health_check.last_time_error_reported", new Exception(), UpdateHealthCheck.class);
				            } 
				            
				        } catch (Exception e) {
				        	ExceptionHandler.handleException("check", "Error occurred while updating health_check.last_time_error_reported", e, UpdateHealthCheck.class);
				        }	
				    }
				});
			
				sendMail.start();				
		      
			}
		} catch (Exception e) {
			ExceptionHandler.handleException("run", "Failed to send Emails", e, HealthCheckMonitorJob.class);
		}
    }
}