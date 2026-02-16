package com.example.eventmanagement.events.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.eventmanagement.events.model.Event;
import com.example.eventmanagement.events.vo.AddEventRequestVO;
import com.example.eventmanagement.events.vo.EventVO;

public interface EventService {
    public List<EventVO> getAllEvents();
    
    public Optional<Event> getEvent(int id);
    
    public void joinEventByEmail(String email, int eventId);
    
    public List<EventVO> getEventsByEmail(String email);
    
    public List<EventVO> getEventsByUserId(int id);
    
    void addEvent(String name, String desc, String address,
            String date, String email, MultipartFile image) throws Exception;
    
    void deleteEvent(int id);

    void updateEvent(int id, String name, String desc,
                     String address, String date,
                     MultipartFile image) throws Exception;

    List<EventVO> getJoinedEventsByEmail(String email);

}
