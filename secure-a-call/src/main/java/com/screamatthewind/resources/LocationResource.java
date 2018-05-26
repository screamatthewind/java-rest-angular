package com.screamatthewind.resources;

import com.google.common.base.Optional;
import com.nimbusds.jose.JOSEException;

import io.dropwizard.hibernate.UnitOfWork;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.screamatthewind.core.Location;
import com.screamatthewind.core.User;
import com.screamatthewind.db.LocationDAO;
import com.screamatthewind.utility.ExceptionHandler;

@Path("/api/location")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {
	
	Logger logger = LoggerFactory.getLogger(LocationResource.class);

	private final LocationDAO dao;
	
	public LocationResource(LocationDAO locationDAO) {
		this.dao = locationDAO;
	}

	// for testing
	@GET
	@Path("/all")
	@UnitOfWork
	public Response getAllLocations(@Context final HttpServletRequest request) throws ParseException, JOSEException {

		try {
		
			Optional<User> authUser = UserResource.getAuthUser(request);
	
			if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
			
			return Response.ok().entity(dao.findAllLocations(authUser.get().getId())).build();

		} catch (Exception e) {
			return ExceptionHandler.handleException("getAllLocations:", "", e, LocationResource.class);
		}
	}

    @GET
    @Path("/{id}")
	@UnitOfWork
    public Response getLocationById(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {

    	try {
    		
	    	Optional<User> authUser = UserResource.getAuthUser(request);
	
	    	if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
	
			Location location = dao.findById(id, authUser.get().getId());
	        if (location != null)
	            return Response.ok(location).build();
	        else
	            return Response.status(Status.NOT_FOUND).build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("getLocationById:", Long.toString(id), e, LocationResource.class);
		}
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteLocation(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {

    	try {
	    	
	    	Optional<User> authUser = UserResource.getAuthUser(request);
	
	    	if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
	
	    	dao.delete(dao.findById(id, authUser.get().getId()));
	
			return Response.ok().build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("deleteLocation:", Long.toString(id), e, LocationResource.class);
		}
    }
    
    @POST
	@UnitOfWork
	public Response saveLocation(@Valid final Location location, @Context final HttpServletRequest request) throws JOSEException, ParseException {

    	try {
    	
	    	Optional<User> authUser = UserResource.getAuthUser(request);
	
	    	if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
	
			Location locationExists = dao.getLocationByName(authUser.get().getId(), location.getLocationName().toUpperCase());		
			
			if (locationExists != null) {
				JSONObject json = new JSONObject();
				json.put("message", "Location already exists");
		        return Response.status(Response.Status.CONFLICT).entity(json.toString()).build();		    
			}
	
			location.setUserId(authUser.get().getId());
			
			Location savedLocation = null;
	
			try {
				savedLocation = dao.save(location);
			} catch (Exception e) {
				return ExceptionHandler.handleException("LocationResource.saveLocation", location.toString(), e, LocationResource.class);
			}
	
			return Response.ok(savedLocation).build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("saveLocation:", location.toString(), e, LocationResource.class);
		}
	}

    @PUT
	@UnitOfWork
	public Response updateLocation(@Valid final Location location, @Context final HttpServletRequest request) throws JOSEException, ParseException {

    	try {
    	
	    	Optional<User> authUser = UserResource.getAuthUser(request);
	
	    	if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
	
			Location locationExists = dao.getLocationByName(authUser.get().getId(), location.getLocationName().toUpperCase());		
			
			if ((locationExists != null) && (locationExists.getId() != location.getId())) {
				JSONObject json = new JSONObject();
				json.put("message", "Location already exists");
		        return Response.status(Response.Status.CONFLICT).entity(json.toString()).build();		    
			}
	
			Location savedLocation = null;
	
			try {
				savedLocation = dao.save(location);
			} catch (Exception e) {
				return ExceptionHandler.handleException("LocationResource.updateLocation", location.toString(), e, LocationResource.class);
			}
	
			return Response.ok(savedLocation).build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("updateLocation:", location.toString(), e, LocationResource.class);
		}
	}
}
