package com.screamatthewind.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.screamatthewind.core.Contact;
import com.screamatthewind.core.Location;

public interface MobileDAO {

	@SqlQuery("call get_location_by_distance(:units, :lat, :lng, :radius)")
	@Mapper(LocationMapper.class)
	List<Location> getByDistance(@Bind("units") String units, @Bind("lat") Double lat, @Bind("lng") Double lng, @Bind("radius") Integer radius);

	@SqlQuery("SELECT * FROM Contact where location_id = :locationId")
	@Mapper(ContactMapper.class)
	List<Contact> getAllContacts(@Bind("locationId") Long locationId);
}

