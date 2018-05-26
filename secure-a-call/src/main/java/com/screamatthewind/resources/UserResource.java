package com.screamatthewind.resources;

import com.nimbusds.jose.JOSEException;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.screamatthewind.auth.AuthUtils;
import com.screamatthewind.core.User;
import com.screamatthewind.db.UserDAO;
import com.screamatthewind.utility.ExceptionHandler;
import com.google.common.base.Optional;

@Path("/api/me")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private static UserDAO dao = null;
	
	public UserResource(UserDAO userDAO) {
		this.dao = userDAO;
	}

	@GET
	@UnitOfWork
	public Response getUser(@Context HttpServletRequest request) throws ParseException, JOSEException {

		try {

			Optional<User> foundUser = getAuthUser(request);
		
			if (!foundUser.isPresent()) {
				return Response.status(Status.NOT_FOUND).build();
			}

			return Response.ok().entity(foundUser.get()).build();

		} catch (Exception e) {
			return ExceptionHandler.handleException("getUser:", "", e, UserResource.class);
		}
	}
	
/*	// for testing
	@GET
	@Path("/all")
	@UnitOfWork
	public Response getAllUsers() {
		return Response.ok().entity(dao.findAll()).build();
	}
*/
	@GET
	@Path("/healthCheck")
	@UnitOfWork
	public Response getUserCount() {
		
		try {
			
			Long result = dao.countAll();
	
			if (result > 0)
				return Response.ok().entity(Status.OK).build();
			
			else
				return Response.status(Status.EXPECTATION_FAILED).build();

		} catch (Exception e) {
			return ExceptionHandler.handleException("getUserCount:", "", e, UserResource.class);
		}
	}
	
	@PUT
	@UnitOfWork
	public Response updateUser(@Valid User user, @Context HttpServletRequest request) throws ParseException, JOSEException {

		try {
			
			Optional<User> foundUser = getAuthUser(request);
			
			if (!foundUser.isPresent()) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new ErrorMessage(AuthResource.NOT_FOUND_MSG)).build();
			}
			
			Optional<User> foundEmailUser = dao.findByEmail(user.getEmail().toUpperCase());
			
			User userToUpdate = foundUser.get();
			if (foundEmailUser.isPresent()) {
				if (foundEmailUser.get().getId() != user.getId()) {
					return Response
							.status(Status.CONFLICT)
							.entity(new ErrorMessage("Email address is already being used")).build();
				}
			}
			
			userToUpdate.setDisplayName(user.getDisplayName());
			userToUpdate.setEmail(user.getEmail());
			dao.save(userToUpdate);
	
			return Response.ok().build();

		} catch (Exception e) {
			return ExceptionHandler.handleException("updateUser:", user.toString(), e, UserResource.class);
		}
	}
	
	/*
	 * Helper methods
	 */	
	public static Optional<User> getAuthUser(HttpServletRequest request) throws ParseException, JOSEException {
		String subject = AuthUtils.getSubject(request.getHeader(AuthUtils.AUTH_HEADER_KEY));
		return dao.findById(Long.parseLong(subject));
	}
}
