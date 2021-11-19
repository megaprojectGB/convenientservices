package com.convenientservices.web.services;

import com.convenientservices.web.entities.Booking;

import java.util.List;

public interface BookingService {
    Booking findById (Long id) throws Exception;
    List<Booking> findAll();
    Booking save(Booking booking);
}
