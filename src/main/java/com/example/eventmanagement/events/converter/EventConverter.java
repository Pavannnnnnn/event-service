package com.example.eventmanagement.events.converter;

import org.springframework.stereotype.Component;

import com.example.eventmanagement.events.model.Event;
import com.example.eventmanagement.events.vo.AddEventRequestVO;
import com.example.eventmanagement.events.vo.EventVO;

@Component   
public class EventConverter {

	public EventVO toVO(Event event) {
		EventVO vo = new EventVO();
		vo.setEventId(event.getEventId());
		vo.setEventName(event.getEventName());
		vo.setEventDate(event.getEventDate());
		vo.setEventAddress(event.getEventAddress());
		vo.setEventDescription(event.getEventDescription());
		vo.setEventImage(event.getEventImage());
		vo.setCreatorEmail(event.getCreatorEmail());
		return vo;
	}
	
	public Event toEntity(AddEventRequestVO request) {

	    Event event = new Event();

	    event.setEventName(request.getEventName());
	    event.setEventDescription(request.getEventDescription());
	    event.setEventAddress(request.getEventAddress());
	    event.setEventDate(request.getEventDate());
	    event.setEventImage(request.getEventImage());

	    event.setCreatorEmail(request.getCreatorEmail());

	    return event;
	}
}
