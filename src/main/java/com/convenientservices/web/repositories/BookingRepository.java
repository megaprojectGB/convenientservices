package com.convenientservices.web.repositories;


import com.convenientservices.web.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
