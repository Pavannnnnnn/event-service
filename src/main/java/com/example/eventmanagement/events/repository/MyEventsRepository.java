package com.example.eventmanagement.events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventmanagement.events.model.MyEvents;

@Repository
public interface MyEventsRepository extends JpaRepository<MyEvents, Integer> {

	List<MyEvents> findByUserId(int id);

	boolean existsByUserIdAndEventId(int userId, int eventId);
}