package com.screamatthewind.health;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.health.HealthCheck;
import com.screamatthewind.utility.ExceptionHandler;

public class RestHealthCheck extends HealthCheck {
	private final Client client;

	public RestHealthCheck(Client client) {
		super();
		this.client = client;
	}

	@Override
	protected Result check() throws Exception {
		try {
			WebTarget webTarget = client.target("http://localhost:8080/api/me/healthCheck");
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
	
			if (response.getStatus() == 200)
				return Result.healthy("Rest interface is healthy");
			else
				return Result.unhealthy("Rest interface health check failed");

		} catch (Exception e) {
			ExceptionHandler.handleException("check", "", e, RestHealthCheck.class);
			return Result.unhealthy("Rest interface health checked had a error");
		}
	}
}