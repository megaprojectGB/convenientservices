package com.convenientservices.web.services;

import com.convenientservices.web.dto.BookingRow;
import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.repositories.BookingRepository;
import com.convenientservices.web.repositories.PointOfServicesRepository;
import com.convenientservices.web.repositories.ServiceRepository;
import com.convenientservices.web.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository repository;
    private final PointOfServicesRepository pointOfServicesRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public Booking findById(Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Booking> findAll() {
        return repository.findAll();
    }

    @Override
    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public List<Booking> getGoodBookings(Principal principal) {
        return this.findAllByUserName(principal.getName()).stream()
                .filter(booking -> booking.getDt().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getDt))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Booking> getOldBookings(Principal principal) {
        return this.findAllByUserName(principal.getName()).stream()
                .filter(booking -> booking.getDt().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getDt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findAllByUserName(String name) {
        return repository.findAllByUserUserName(name);
    }

    @Override
    public List<Booking> findAllByPosId(Long id) {
        return findAll().stream().filter(booking -> booking.getPointOfServices().getId().equals(id)).sorted((o1, o2) -> o1.getDt().compareTo(o2.getDt())).collect(Collectors.toList());
    }

    @Override
    public List<BookingRow> getAllBookingsByPosAndMasterAndDate(LocalDate selectedDate, Long masterId, Long posId) {
        List<BookingRow> list = new ArrayList<>();
        int startTime = 700;
        int endTime = 730;
        int length = 1;
        String text = "EVENT ";
        String span = "SPAN ";
        LocalDate date;
//        list.add(new BookingRow((long) 7, 800,
//                930, length, text , span, selectedDate, Math.random() * 10 > 5));
//        list.add(new BookingRow((long) 7, 800,
//                930, length, text , span, selectedDate.plusDays(2), Math.random() * 10 > 5));
//        list.add(new BookingRow((long) 7, 1100,
//                1430, length, text , span, selectedDate.plusDays(3), Math.random() * 10 > 5));
        for (int i = 0; i < 95; i++) {
            date = selectedDate.plusDays(i % 7);
            if (i % 7 == 0) {
                startTime += 100;
                endTime = startTime + 30;
            }
//            if (Math.random() * 10 > 8) {
//                endTime = startTime + 130;
//            } else {
//                endTime = startTime + 30;
//            }
            list.add(new BookingRow((long) i, startTime,
                    endTime, length, text + i, span, date, Math.random() * 10 > 5));
        }
        return list;
    }

    @Override
    @Transactional
    public void addBooking(Long posId, String date, String startTime, Long masterId, Principal principal, Long serviceId) {
        Booking booking = new Booking();
        User user = userRepository.findUserByUserName(principal.getName()).orElse(null);
        User master = userRepository.findById(masterId).orElse(null);
        LocalDateTime dt = LocalDateTime.now().plusDays(5);
        PointOfServices pos = pointOfServicesRepository.findById(posId).orElse(null);
        com.convenientservices.web.entities.Service service = serviceRepository.findById(serviceId).orElse(null);
        List<com.convenientservices.web.entities.Service> services = new ArrayList<>();
        services.add(service);
        booking.setServices(services);
        booking.setMaster(master);
        booking.setUser(user);
        booking.setDt(dt);
        booking.setPointOfServices(pos);
        repository.save(booking);


    }
}
