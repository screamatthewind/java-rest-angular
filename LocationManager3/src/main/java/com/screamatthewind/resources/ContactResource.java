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

@Path("/api/contact")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContactResource {
	
	private final ContactDAO dao;
	
	public ContactResource(ContactDAO contactDAO) {
		this.dao = contactDAO;
	}

	@GET
	@Path("/all/{locationId}")
	@UnitOfWork
	public Response getAllContacts(@PathParam("locationId") Long locationId, @Context final HttpServletRequest request) throws ParseException, JOSEException {
		Optional<User> user = UserResource.getAuthUser(request);
		
		return Response.ok().entity(dao.findAllContacts(locationId, user.get().getId())).build();
	}

    @GET
    @Path("/{id}")
	@UnitOfWork
    public Response getContactById(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {
		Optional<User> user = UserResource.getAuthUser(request);

		return Response.ok().entity(dao.findById(id, user.get().getId())).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteContact(@PathParam("id") Long id, @Context final HttpServletRequest request) throws ParseException, JOSEException {
		Optional<User> user = UserResource.getAuthUser(request);

		dao.delete(dao.findById(id, user.get().getId()));

		return Response.ok().build();
    }

    @POST
	@UnitOfWork
	public Response saveContact(@Valid final Contact contact, @Context final HttpServletRequest request) throws ParseException, JOSEException {
		Optional<User> user = UserResource.getAuthUser(request);

		Contact savedContact = null;
		contact.setUserId(user.get().getId());
		
		try {
			savedContact = dao.save(contact);
		} catch (Exception e) {
			return ExceptionHandler.handleEcxeption("ContactResource.saveContact", contact.toString(), e, ContactResource.class);
		}
    	
		return Response.ok(savedContact).build();
	}


}
