package com.screamatthewind.db;

import java.util.List;

import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

import com.screamatthewind.core.Contact;
import com.screamatthewind.core.Contact;

public class ContactDAO extends AbstractDAO<Contact> {

	SessionFactory sessFactory;

	public ContactDAO(SessionFactory factory) {
        super(factory);

        sessFactory = factory;
	}

    public Contact findById(Long id, Long userId) {
    	Contact contact = (Contact) namedQuery("Contact.findById")
    			.setParameter("id", id)
    			.setParameter("userId", userId)
    			.uniqueResult();

    	return contact;
    }
    
    public Contact save(Contact contact) throws Exception {

		Contact savedContact = null;
		
		try {
			savedContact = persist(contact);
			sessFactory.getCurrentSession().flush();
		} catch (Exception e) {
			sessFactory.getCurrentSession().clear();
			throw new Exception(e);
		}
		
		return savedContact;
    }

    public List<Contact> findAllContacts(Long locationId, Long userId) {
        return list(namedQuery("Contact.findAll")
        		.setParameter("locationId", locationId)
        		.setParameter("userId", userId));
    }

    public void delete(Contact contact) {
        currentSession().delete(contact);
    }

}
