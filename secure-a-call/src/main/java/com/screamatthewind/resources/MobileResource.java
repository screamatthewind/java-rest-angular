package com.screamatthewind.resources;

import com.nimbusds.jose.JOSEException;
import com.screamatthewind.core.Contact;
import com.screamatthewind.core.Location;
import com.screamatthewind.core.LocationContacts;
import com.screamatthewind.db.ContactDAO;
import com.screamatthewind.db.MobileDAO;
import com.screamatthewind.utility.ExceptionHandler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/mobile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class MobileResource {
	
	Logger logger = LoggerFactory.getLogger(LocationResource.class);

	private final MobileDAO daoMobile;
	
	public MobileResource(MobileDAO daoMobile) {
		this.daoMobile = daoMobile;
	}

//	http://localhost:8080/api/mobile/all/miles/38.729827/-75.278149/100
	
	@GET
	@Path("/all/{units}/{lat}/{lng}/{radius}")
	public Response getAll(@PathParam("units") String units, @PathParam("lat") Double lat, @PathParam("lng") Double lng, @PathParam("radius") Integer radius, @Context final HttpServletRequest request) throws ParseException, JOSEException {
		
		try {
			
/*			Optional<User> authUser = UserResource.getAuthUser(request);
	
			if (!authUser.isPresent()) {
				logger.error("Cannot find user credentials");
	
				JSONObject json = new JSONObject();
				json.put("message", "Cannot find user user information");
	
				return Response.status(Status.NOT_FOUND).build();
			}
*/			
			List<LocationContacts> locationContactsList = new ArrayList<LocationContacts>();
			
			List<Location> locations = daoMobile.getByDistance(units, lat, lng, radius);

			for (Location location: locations) {

				LocationContacts locationContacts = new LocationContacts();

				locationContacts.setLocation(location);
				locationContacts.setContacts(daoMobile.getAllContacts(location.getId()));
				
				locationContactsList.add(locationContacts);
			}
			
			return Response.ok().entity(locationContactsList).build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("getAll:", "", e, MobileResource.class);
		}
	}

}
