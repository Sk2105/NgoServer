package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
}
