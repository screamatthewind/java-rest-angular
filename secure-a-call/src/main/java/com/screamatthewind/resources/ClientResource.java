package com.screamatthewind.resources;

import java.io.IOException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.screamatthewind.utility.ExceptionHandler;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class ClientResource {
	
	@GET
	public Response getPage() throws IOException {
		
		try {
			
			URL clientPage = Resources.getResource("assets/index.html");
		    return Response.ok(Resources.toString(clientPage, Charsets.UTF_8)).build();

    	} catch (Exception e) {
			return ExceptionHandler.handleException("getPage:", "", e, ClientResource.class);
		}
	}
}
