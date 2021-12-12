package com.convenientservices.web.repositories;


import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserUserName(String name);
    List<Booking> findAllByUserAndMasterAndPointOfServicesAndDtAfterAndDtBefore(User user, User master, PointOfServices pointOfServices, LocalDateTime dt, LocalDateTime dateTime);
    List<Booking> findByMasterId(Long id);
}
