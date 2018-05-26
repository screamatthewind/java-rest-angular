package com.screamatthewind.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.screamatthewind.core.Contact;

public class ContactMapper implements ResultSetMapper<Contact>
{
  public Contact map(int index, ResultSet r, StatementContext ctx) throws SQLException
  {
	  Contact contact = new Contact();

	  contact.setId(r.getInt("ID"));
	  contact.setLocationId(r.getInt("location_id"));
	  contact.setUserId(r.getInt("user_id"));
	  contact.setContactType(r.getString("contact_type"));
	  contact.setContactName(r.getString("contact_name"));
	  contact.setPhoneNumber(r.getString("phone_number"));
	  contact.setEmailAddress(r.getString("email_address"));
	  contact.setPriority(r.getInt("priority"));
    
    return contact;
  }
}