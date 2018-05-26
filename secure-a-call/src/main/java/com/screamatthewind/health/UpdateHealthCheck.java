package com.screamatthewind.health;
 
import com.codahale.metrics.health.HealthCheck;
import com.screamatthewind.db.HealthUpdateDAO;
import com.screamatthewind.utility.ExceptionHandler;

import io.dropwizard.hibernate.HibernateBundle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
 
public class UpdateHealthCheck extends HealthCheck { 
 
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHealthCheck.class); 
	private final HibernateBundle hibernateBundle;
    private DataSource datasource;
    private HealthUpdateDAO dao;
    private boolean isExternal;
    
	public UpdateHealthCheck(HibernateBundle hibernateBundle, DataSource datasource, HealthUpdateDAO daoUpdateHealth, boolean isExternal) {
		this.hibernateBundle = hibernateBundle;
		this.datasource = datasource;
		this.dao = daoUpdateHealth;
		this.isExternal = isExternal;
	}

    @Override 
    protected HealthCheck.Result check() throws Exception { 
        LOGGER.info("Do DatabaseHealthCheck..."); 

        HealthCheck.Result result = null;		

		try {
			
			Connection connection = datasource.getConnection();
            Statement statement;
            int rowsUpdated = 0;
            
            statement = connection.createStatement();
            
            if (isExternal)
            	rowsUpdated = statement.executeUpdate("update health_check set last_external_update = NOW()");
            else
            	rowsUpdated = statement.executeUpdate("update health_check set last_internal_update = NOW()");

            if (rowsUpdated == 1) {

            	ResultSet resultSet = null;
            	
				if (isExternal) {
                	resultSet = statement.executeQuery("select last_internal_update from health_check");
                	resultSet.next();

                	java.util.Date lastInternalUpdate = null;
    				Timestamp timestamp  = resultSet.getTimestamp(1);
                	if (timestamp != null)
                		lastInternalUpdate = new java.util.Date(timestamp.getTime());

            	    System.out.println(lastInternalUpdate);

                	result = Result.healthy("Last Internal Update: " + lastInternalUpdate);

				} else 
					result = Result.healthy("Update health check is healthy");
            } 
            
        } catch (Exception e) {
        	ExceptionHandler.handleException("check", "", e, UpdateHealthCheck.class);
            result = HealthCheck.Result.unhealthy("Update health check had an error");
        }	
      
      return result;
    } 
 
}