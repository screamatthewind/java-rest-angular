package com.screamatthewind.core;

import java.util.List;

public class LocationContacts {

	private Location location;
	private List<Contact> contacts;

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation(){
		return this.location;
	}
	
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public List<Contact> getContacts() {
		return this.contacts;
	}
}
