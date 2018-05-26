package com.screamatthewind.resources;

import java.text.ParseException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.health.HealthCheck.Result;
import com.nimbusds.jose.JOSEException;
import com.screamatthewind.utility.ExceptionHandler;

import io.dropwizard.hibernate.UnitOfWork;

// runs all health checks

@Path("/api/healthCheck")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HealthResource {

	@GET
	@Path("/run")
	@UnitOfWork
	public Response runHealthChecks(){
		try {

			/*		for (Entry<String, Result> entry : registry.runHealthChecks().entrySet()) {
			    if (entry.getValue().isHealthy()) {
			        System.out.println(entry.getKey() + ": OK");
			    } else {
			        System.out.println(entry.getKey() + ": FAIL");
			    }
			}
			 */		
			return null;

		} catch (Exception e) {
			return ExceptionHandler.handleException("getPage:", "", e, ClientResource.class);
		}

	}
}
