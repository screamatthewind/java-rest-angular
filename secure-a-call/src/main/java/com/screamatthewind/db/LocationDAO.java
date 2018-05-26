package com.screamatthewind.db;

import java.util.List;

import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.screamatthewind.core.Address;
import com.screamatthewind.core.Location;
import com.screamatthewind.utility.GetLocationFromAddress;

public class LocationDAO extends AbstractDAO<Location> {

	SessionFactory sessFactory;
	
	public LocationDAO(SessionFactory factory) {
        super(factory);
        
        sessFactory = factory;
    }

    public Location findById(Long id, Long userId) {

    	Location location = (Location) namedQuery("Location.findByLocationId")
    			.setParameter("id", id)
    			.setParameter("userId", userId)
    			.uniqueResult();

    	return location;
    }
    
    public Location save(Location location) throws Exception {
    	
		String locationName = location.getLocationName();
		String emailAddress = location.getEmailAddress();
		
		List<Address> addresses = GetLocationFromAddress.findAddress(location.toString(false));

		if (addresses.isEmpty()) {
			location.setLocationName("");
			addresses = GetLocationFromAddress.findAddress(location.toString(false)); // try finding it without location name

			if (addresses.isEmpty()) {
				addresses = GetLocationFromAddress.findAddress(location.toString(true)); // try finding it without address 2
			}
		}
		
		if (!addresses.isEmpty()) {
			Address address = addresses.get(0);

			location.setPlaceId(address.getPlaceId());
			location.setLocationName(locationName);
			location.setAddress1(address.getAddressLine(0));
			// location.setAddress2 = "";
			location.setCity(address.getLocality());
			location.setState(address.getAdminArea());
			location.setZip(address.getPostalCode());
			location.setStreetNumber(address.getStreetNumber());
			location.setStreetName(address.getStreetName());
			location.setCounty(address.getSubAdminArea());
			location.setCountryCode(address.getCountryCode());
			location.setEmailAddress(emailAddress);
			location.setFormattedAddress(address.getFormattedAddress());
			location.setLatitude(address.getLatitude());
			location.setLongitude(address.getLongitude());			
			
			location.setFormattedCoordinates(address.getLatitude(), address.getLongitude());

		} else {
			location.setLocationName(locationName);
			location.setEmailAddress(emailAddress);
		}
    	
		Location savedLocation = null;
		
		try {
			Session session = sessFactory.getCurrentSession();
			
			savedLocation = (Location) session.merge(location);  // handles updates better where there are unique constraints
			// savedLocation = persist(location);
			session.flush();
		} catch (Exception e) {
			sessFactory.getCurrentSession().clear();
			throw new Exception(e);
		}
		
		return savedLocation;
    }

    public Location getLocationByName(long userId, String locationName) {
    	Location location = (Location) namedQuery("Location.findByLocationName")
    			.setParameter("userId", userId)
    			.setParameter("locationName", locationName)
    			.uniqueResult();

    	return location;
	}

	public List<Location> findAllLocations(long userId) {
        return list(namedQuery("Location.findAllLocations").setParameter("userId", userId));
    }

    public void delete(Location location) {
        currentSession().delete(location);
    }
}
