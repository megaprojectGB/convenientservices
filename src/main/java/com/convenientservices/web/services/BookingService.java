package com.convenientservices.web.services;

import com.convenientservices.web.entities.Booking;

import java.security.Principal;
import java.util.List;

public interface BookingService {
    Booking findById (Long id) throws Exception;

    List<Booking> findAll ();

    List<Booking> findAllByUserName (String name);

    List<Booking> findAllByPosId (Long id);

    Booking save (Booking booking);

    List<Booking> getGoodBookings (Principal principal);

    void deleteById (Long id);

    List<Booking> getOldBookings (Principal principal);
}
