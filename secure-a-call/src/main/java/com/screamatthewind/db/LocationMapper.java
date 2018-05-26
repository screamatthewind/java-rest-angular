package com.screamatthewind.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.screamatthewind.core.Location;

public class LocationMapper implements ResultSetMapper<Location>
{
  public Location map(int index, ResultSet r, StatementContext ctx) throws SQLException
  {
    Location location = new Location();

    location.setId(r.getInt("ID"));
    location.setUserId(r.getInt("user_id"));
    location.setPlaceId(r.getString("place_id"));
    location.setLocationType(r.getString("location_type"));
    location.setLocationName(r.getString("location_name"));
    location.setAddress1(r.getString("address_1"));
    location.setAddress2(r.getString("address_2"));
    location.setCity(r.getString("city"));
    location.setState(r.getString("state"));
    location.setZip(r.getString("zip"));
    location.setCountryCode(r.getString("country_code"));
    location.setLatitude(r.getDouble("latitude"));
    location.setLatitude(r.getDouble("longitude"));
    
    return location;
  }
}