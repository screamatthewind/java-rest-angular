package com.screamatthewind.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
@Table(name = "health")

public class Health {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "last_time_updated")
	private DateTime lastTimeUpdated;

	@Column(name = "last_time_error_reported")
	private DateTime lastTimeErrorReported;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public DateTime getLastTimeUpdated() {
		return lastTimeUpdated;
	}
	
	public void setLastTimeUpdated(DateTime lastTimeUpdated) {
		this.lastTimeUpdated = lastTimeUpdated;
	}
	
	public DateTime getLastTimeErrorReported() {
		return lastTimeErrorReported;
	}
	
	public void setLastTimeErrorReported(DateTime dateTime) {
		this.lastTimeErrorReported = lastTimeErrorReported;
	}
}
