package com.example.eventmanagement.events.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.eventmanagement.events.converter.EventConverter;
import com.example.eventmanagement.events.model.Event;
import com.example.eventmanagement.events.service.EventService;
import com.example.eventmanagement.events.vo.EventVO;
import com.example.eventmanagement.events.vo.MyEventRequestVO;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public List<EventVO> getAllEvents() {
		return eventService.getAllEvents();
	}

	@GetMapping("/{id}")
	public EventVO getEvent(@PathVariable(name = "id") int id) {
		Optional<Event> eventId = eventService.getEvent(id);
		EventConverter converter = new EventConverter();
		return converter.toVO(eventId.get());
	}

	@PostMapping("/join")
	public ResponseEntity<String> joinEvent(@RequestBody MyEventRequestVO request) {
		eventService.joinEventByEmail(request.getEmail(), request.getEventId());
		return ResponseEntity.ok("Event Created successfully");
	}

	@PostMapping("/add")
	public ResponseEntity<String> addEvent(@RequestParam("eventName") String eventName,
			@RequestParam("eventDescription") String eventDescription,
			@RequestParam("eventAddress") String eventAddress, @RequestParam("eventDate") String eventDate,
			@RequestParam("creatorEmail") String creatorEmail,
			@RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

		eventService.addEvent(eventName, eventDescription, eventAddress, eventDate, creatorEmail, image);

		return ResponseEntity.ok("Event added successfully");
	}

	@GetMapping("/byUser/{email}")
	public ResponseEntity<List<EventVO>> getEventsByUser(@PathVariable String email) {
		List<EventVO> events = eventService.getEventsByEmail(email);
		return ResponseEntity.ok(events);
	}

	@GetMapping("/joined/{userId}")
	public ResponseEntity<List<EventVO>> getJoinedEvents(@PathVariable int userId) {
		List<EventVO> events = eventService.getEventsByUserId(userId);
		return ResponseEntity.ok(events);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEvent(@PathVariable int id) {
		eventService.deleteEvent(id);
		return ResponseEntity.ok("Event deleted successfully");
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<String> updateEvent(@PathVariable int id, @RequestParam("eventName") String name,
			@RequestParam("eventDescription") String desc, @RequestParam("eventAddress") String address,
			@RequestParam("eventDate") String date,
			@RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

		eventService.updateEvent(id, name, desc, address, date, image);

		return ResponseEntity.ok("Event updated successfully");
	}
	
	@GetMapping("/joinedByEmail/{email}")
	public ResponseEntity<List<EventVO>> getJoinedEventsByEmail(@PathVariable String email) {
	    List<EventVO> events = eventService.getJoinedEventsByEmail(email);
	    return ResponseEntity.ok(events);
	}


}
