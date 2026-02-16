package com.example.eventmanagement.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "myevents")
public class MyEvents {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int myeventsid;

	@Column(name = "eventId")
	private int eventId;

	@Column(name = "userId")
	private int userId;

	public MyEvents() {

	}

	public MyEvents(int eventId, int userId) {
		super();
		this.eventId = eventId;
		this.userId = userId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
