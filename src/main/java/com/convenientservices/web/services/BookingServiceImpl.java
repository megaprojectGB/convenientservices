package com.convenientservices.web.services;

import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.repositories.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private BookingRepository repository;

    @Override
    public Booking findById (Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Booking> findAll () {
        return repository.findAll();
    }

    @Override
    public Booking save (Booking booking) {
        return repository.save(booking);
    }
}
