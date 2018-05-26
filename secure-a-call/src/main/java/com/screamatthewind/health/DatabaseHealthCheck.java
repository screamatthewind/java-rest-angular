package com.screamatthewind.health;
 
import com.codahale.metrics.health.HealthCheck;

import io.dropwizard.hibernate.HibernateBundle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
 
public class DatabaseHealthCheck extends HealthCheck { 
 
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHealthCheck.class); 
	private final HibernateBundle hibernateBundle;
    private DataSource datasource;
    
	public DatabaseHealthCheck(HibernateBundle hibernateBundle, DataSource datasource) {
		this.hibernateBundle = hibernateBundle;
		this.datasource = datasource;
	}

    @Override 
    protected HealthCheck.Result check() throws Exception { 
        LOGGER.info("Do DatabaseHealthCheck..."); 

        HealthCheck.Result result = null;		

		if(hibernateBundle.getSessionFactory().isClosed()) {
			result = Result.unhealthy("Database Session is closed");
		}
         
		try (Connection connection = datasource.getConnection()) {
            Statement statement;
            ResultSet resultSet;
            
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select 1 from dual");

            if (resultSet.next()) {
                result = Result.healthy("Database is healthy");
            } 
            
        } catch (Throwable t) {
            result = HealthCheck.Result.unhealthy("Database health check had an error", t);
        }	
      
      return result;
    } 
 
}