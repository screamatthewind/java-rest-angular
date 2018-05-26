package com.screamatthewind.utility;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class ExceptionHandler {

	public static Response handleEcxeption(String functionName, String functionData, Exception e, Class<?> clazz) {

		Logger logger = LoggerFactory.getLogger(clazz);
		
		JSONObject json = new JSONObject();
	    Throwable cause = e.getCause();

	    if (cause instanceof ConstraintViolationException){
	        SQLException exc = ((ConstraintViolationException)cause).getSQLException();
	
	        json.put("message",  "There was an error.  It has been reported");	        
			logger.info("Function: " + functionName + ": " + exc.getMessage());
	        logger.error("Function: " + functionName + ": " + cause.toString());
	        logger.error(functionData);

	    } else if (cause instanceof MySQLIntegrityConstraintViolationException ){
	        SQLException exc = ((ConstraintViolationException)cause).getSQLException();
	    	
	        json.put("message",  "There was an error.  It has been reported");	        
			logger.info("Function: " + functionName + ": " + exc.getMessage());
	        logger.error("Function: " + functionName + ": " + cause.toString());
	        logger.error(functionData);
	    	
	    } else {
	        logger.error("Function: " + functionName + ": " + cause.toString());
	        logger.error(functionData);
			logger.error("Exception", new Exception());
	        json.put("message",  "There was an error.  It has been reported");	        
	    }
	    
        return Response.status(Response.Status.FORBIDDEN).entity(json.toString()).build();		    
	}
}
