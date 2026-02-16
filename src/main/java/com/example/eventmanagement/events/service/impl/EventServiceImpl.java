package com.example.eventmanagement.events.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.eventmanagement.events.converter.EventConverter;
import com.example.eventmanagement.events.model.Event;
import com.example.eventmanagement.events.model.MyEvents;
import com.example.eventmanagement.events.repository.EventRepository;
import com.example.eventmanagement.events.repository.MyEventsRepository;
import com.example.eventmanagement.events.service.EventService;
import com.example.eventmanagement.events.service.SupabaseStorageService;
import com.example.eventmanagement.events.vo.EventVO;
import com.example.eventmanagement.events.service.EmailService;


@Service
public class EventServiceImpl implements EventService {

	private final MyEventsRepository myEventsRepository;
	private final RestTemplate restTemplate;

	private final EventRepository repository;
	private final EventConverter converter;

	private final SupabaseStorageService storageService;
	private final EmailService emailService;

	public EventServiceImpl(
	        EventRepository repository,
	        EventConverter converter,
	        MyEventsRepository myEventsRepository,
	        RestTemplate restTemplate,
	        SupabaseStorageService storageService,
	        EmailService emailService) {

	    this.repository = repository;
	    this.converter = converter;
	    this.myEventsRepository = myEventsRepository;
	    this.restTemplate = restTemplate;
	    this.storageService = storageService;
	    this.emailService = emailService;  // 🔥 ADD THIS
	}

	@Override
	public List<EventVO> getAllEvents() {
		return repository.findAllByOrderByEventDateDesc().stream().map(converter::toVO).collect(Collectors.toList());
	}

	@Override
	public Optional<Event> getEvent(int id) {
		return repository.findById(id);
	}

	@Override
	public void joinEventByEmail(String email, int eventId) {

		String url = "http://event-authorization-service.railway.internal:8080/api/users/email/" + email;
	    Integer userId = restTemplate.getForObject(url, Integer.class);

	    boolean alreadyJoined =
	            myEventsRepository.existsByUserIdAndEventId(userId, eventId);

	    if (alreadyJoined) {
	        throw new RuntimeException("User already joined this event");
	    }

	    MyEvents myEvent = new MyEvents(eventId, userId);
	    myEventsRepository.save(myEvent);

	    // 🔥 Fetch event details
	    Event event = repository.findById(eventId)
	            .orElseThrow(() -> new RuntimeException("Event not found"));

	    // 🔥 Send ticket email
	    emailService.sendTicketEmail(
	            email,
	            event.getEventName(),
	            event.getEventDate().toString(),
	            event.getEventAddress()
	    );
	}


	@Override
	public void addEvent(String name, String desc, String address, String date, String email, MultipartFile image)
			throws Exception {

		String imageUrl = null;

		if (image != null && !image.isEmpty()) {

			String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename().replaceAll("\\s+", "_");

			imageUrl = storageService.uploadImage(fileName, image.getBytes());
		}

		Event event = new Event();
		event.setEventName(name);
		event.setEventDescription(desc);
		event.setEventAddress(address);
		event.setEventDate(LocalDateTime.parse(date));
		event.setCreatorEmail(email);
		event.setEventImage(imageUrl); // store FULL URL

		repository.save(event);
	}

	@Override
	public List<EventVO> getEventsByEmail(String email) {
		return repository.findByCreatorEmail(email).stream().map(converter::toVO).collect(Collectors.toList());
	}

	@Override
	public List<EventVO> getEventsByUserId(int userId) {
		List<MyEvents> mappings = myEventsRepository.findByUserId(userId);
		List<Integer> eventIds = mappings.stream().map(MyEvents::getEventId).collect(Collectors.toList());
		if (eventIds.isEmpty()) {
			return List.of();
		}
		List<Event> events = repository.findAllById(eventIds);
		return events.stream().map(converter::toVO).collect(Collectors.toList());
	}

	@Override
	public void deleteEvent(int id) {

	    Event event = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Event not found"));

	    // 🔥 Delete image from Supabase
	    if (event.getEventImage() != null) {
	        storageService.deleteImage(event.getEventImage());
	    }

	    repository.deleteById(id);
	}


	@Override
	public void updateEvent(int id,
	                        String name,
	                        String desc,
	                        String address,
	                        String date,
	                        MultipartFile image) throws Exception {

	    Event event = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Event not found"));

	    event.setEventName(name);
	    event.setEventDescription(desc);
	    event.setEventAddress(address);
	    event.setEventDate(LocalDateTime.parse(date));

	    if (image != null && !image.isEmpty()) {

	        // 🔥 Delete old image from Supabase
	        if (event.getEventImage() != null) {
	            storageService.deleteImage(event.getEventImage());
	        }

	        String fileName = System.currentTimeMillis() + "_" +
	                image.getOriginalFilename()
	                        .replaceAll("\\s+", "_");

	        String imageUrl = storageService.uploadImage(
	                fileName,
	                image.getBytes()
	        );

	        event.setEventImage(imageUrl);
	    }

	    repository.save(event);
	}


	@Override
	public List<EventVO> getJoinedEventsByEmail(String email) {

		String url = "http://event-authorization-service.railway.internal:8080/api/users/email/" + email;

		Integer userId = restTemplate.getForObject(url, Integer.class);

		List<MyEvents> mappings = myEventsRepository.findByUserId(userId);

		List<Integer> eventIds = mappings.stream().map(MyEvents::getEventId).toList();

		if (eventIds.isEmpty()) {
			return List.of();
		}

		List<Event> events = repository.findAllById(eventIds);

		return events.stream().map(converter::toVO).toList();
	}
	


}
