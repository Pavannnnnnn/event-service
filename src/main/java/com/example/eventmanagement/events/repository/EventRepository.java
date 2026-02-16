package com.example.eventmanagement.events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventmanagement.events.model.Event;
import com.example.eventmanagement.events.model.MyEvents;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	List<Event> findByCreatorEmail(String creatorEmail);
	
	 List<Event> findAllByOrderByEventDateDesc();
}
