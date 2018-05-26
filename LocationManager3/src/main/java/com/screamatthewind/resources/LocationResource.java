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

import com.screamatthewind.core.Location;
import com.screamatthewind.core.User;
import com.screamatthewind.db.LocationDAO;
import com.screamatthewind.utility.ExceptionHandler;

@Path("/api/location")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {
	
	private final LocationDAO dao;
	
	public LocationResource(LocationDAO locationDAO) {
		this.dao = locationDAO;
	}

	// for testing
	@GET
	@Path("/all")
	@UnitOfWork
	public Response getAllLocations(@Context final HttpServletRequest request) throws ParseException, JOSEException {
		Optional<User> foundUser = UserResource.getAuthUser(request);

		return Response.ok().entity(dao.findAllLocations(foundUser.get().getId())).build();
	}

    @GET
    @Path("/{id}")
	@UnitOfWork
    public Response getLocationById(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {

    	Optional<User> user = UserResource.getAuthUser(request);
    	
		Location location = dao.findById(id, user.get().getId());
        if (location != null)
            return Response.ok(location).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteLocation(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {
    	Optional<User> user = UserResource.getAuthUser(request);

    	dao.delete(dao.findById(id, user.get().getId()));

		return Response.ok().build();
    }

    @POST
	@UnitOfWork
	public Response saveLocation(@Valid final Location location, @Context final HttpServletRequest request) throws JOSEException, ParseException {
		Optional<User> user = UserResource.getAuthUser(request);
		Location locationExists = dao.getLocationByName(user.get().getId(), location.getLocationName().toUpperCase());		
		
		if (locationExists != null) {
			JSONObject json = new JSONObject();
			json.put("message", "Location already exists");
	        return Response.status(Response.Status.FORBIDDEN).entity(json.toString()).build();		    
		}

		location.setUserId(user.get().getId());
		
		Location savedLocation = null;

		try {
			savedLocation = dao.save(location);
		} catch (Exception e) {
			return ExceptionHandler.handleEcxeption("LocationResource.saveLocation", location.toString(), e, LocationResource.class);
		}

		return Response.ok(savedLocation).build();
	}

    @PUT
	@UnitOfWork
	public Response updateLocation(@Valid final Location location, @Context final HttpServletRequest request) throws JOSEException, ParseException {
		Optional<User> user = UserResource.getAuthUser(request);

		location.setUserId(user.get().getId());
		
		Location savedLocation = null;

		try {
			savedLocation = dao.save(location);
		} catch (Exception e) {
			return ExceptionHandler.handleEcxeption("LocationResource.updateLocation", location.toString(), e, LocationResource.class);
		}

		return Response.ok(savedLocation).build();
	}
}
