package com.screamatthewind.resources;

import com.google.common.base.Optional;
import com.nimbusds.jose.JOSEException;
import com.screamatthewind.core.Contact;
import com.screamatthewind.core.User;
import com.screamatthewind.core.Contact;
import com.screamatthewind.db.ContactDAO;
import com.screamatthewind.utility.ExceptionHandler;

import io.dropwizard.hibernate.UnitOfWork;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

@Path("/api/contact")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContactResource {
	
	Logger logger = LoggerFactory.getLogger(LocationResource.class);

	private final ContactDAO dao;
	
	public ContactResource(ContactDAO contactDAO) {
		this.dao = contactDAO;
	}

	@GET
	@Path("/all/{locationId}")
	@UnitOfWork
	public Response getAllContacts(@PathParam("locationId") Long locationId, @Context final HttpServletRequest request) throws ParseException, JOSEException {
		
		try {
			
			Optional<User> authUser = UserResource.getAuthUser(request);
	
			if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
						
			return Response.ok().entity(dao.findAllContacts(locationId, authUser.get().getId())).build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("getAllContacts:", Long.toString(locationId), e, ContactResource.class);
		}
	}

    @GET
    @Path("/{id}")
	@UnitOfWork
    public Response getContactById(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {

    	try {

			Optional<User> authUser = UserResource.getAuthUser(request);
			
			if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
	
			return Response.ok().entity(dao.findById(id, authUser.get().getId())).build();
			
    	} catch (Exception e) {
			return ExceptionHandler.handleException("getContactById:", Long.toString(id), e, ContactResource.class);
		}
	}

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteContact(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {

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
			return ExceptionHandler.handleException("deleteContact:", Long.toString(id), e, ContactResource.class);
		}
    }

    @POST
	@UnitOfWork
	public Response saveContact(@Valid final Contact contact, @Context final HttpServletRequest request) throws ParseException, JOSEException {

    	try {
    		
			Optional<User> authUser = UserResource.getAuthUser(request);
			
			if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
	
			Contact savedContact = null;
			contact.setUserId(authUser.get().getId());
			
			try {
				savedContact = dao.save(contact);
			} catch (Exception e) {
				return ExceptionHandler.handleException("saveContact", contact.toString(), e, ContactResource.class);
			}
	    	
			return Response.ok(savedContact).build();
			
    	} catch (Exception e) {
			return ExceptionHandler.handleException("saveContact:", contact.toString(), e, ContactResource.class);
		}
	}
}
