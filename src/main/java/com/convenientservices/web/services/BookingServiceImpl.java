package com.convenientservices.web.services;

import com.convenientservices.web.dto.BookingRow;
import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.repositories.BookingRepository;
import com.convenientservices.web.repositories.PointOfServicesRepository;
import com.convenientservices.web.repositories.ServiceRepository;
import com.convenientservices.web.repositories.UserRepository;
import com.convenientservices.web.utilities.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PointOfServicesRepository pointOfServicesRepository;
    private final ServiceRepository serviceRepository;
    private final MailSenderService mailSenderService;

    @Override
    public Booking findById (Long id) throws Exception {
        return bookingRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Booking> findAll () {
        return bookingRepository.findAll();
    }

    @Override
    public Booking save (Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getGoodBookings (Principal principal) {
        return this.findAllByUserName(principal.getName()).stream()
                .filter(booking -> booking.getDt().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getDt))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById (Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getOldBookings (Principal principal) {
        return this.findAllByUserName(principal.getName()).stream()
                .filter(booking -> booking.getDt().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getDt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findAllByUserName (String name) {
        return bookingRepository.findAllByUserUserName(name);
    }

    @Override
    public List<Booking> findAllByPosId (Long id) {
        return findAll().stream().filter(booking -> booking.getPointOfServices().getId().equals(id))
                .sorted(Comparator.comparing(Booking::getDt))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingRow> getAllBookingsByPosAndMasterAndDate (Principal principal, LocalDate selectedDate, Long masterId, Long posId) {
        User user = userRepository.findUserByUserName(principal.getName()).orElse(null);
        User master = userRepository.findById(masterId).orElse(null);
        PointOfServices pos = pointOfServicesRepository.findById(posId).orElse(null);
        LocalDateTime dateTime;
        if (selectedDate.compareTo(LocalDate.now()) == 0) {
            dateTime = LocalDateTime.of(selectedDate, LocalTime.now());
        } else {
            dateTime = LocalDateTime.of(selectedDate, LocalTime.of(7, 0));
        }
        LocalDateTime endDateTime = LocalDateTime.of(selectedDate.plusDays(6), LocalTime.of(23, 0));
        // Список букингов у юзера в точке для мастера на неделю
        List<Booking> bookings = bookingRepository.findAllByUserAndMasterAndPointOfServicesAndDtAfterAndDtBefore(user, master, pos, dateTime, endDateTime);
        Map<LocalDate, Map<String, BookingRow>> week = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            week.computeIfAbsent(selectedDate.plusDays(i), k -> new HashMap<>());
            Map<String, BookingRow> day = getDay(selectedDate.plusDays(i), bookings);
            week.put(selectedDate.plusDays(i), day);
        }
        return getBookingList(week, selectedDate);
    }

    @Override
    @Transactional
    public void addBooking (Long posId, String date, String startTime, Long masterId, Principal principal, Long serviceId) {
        Booking booking = new Booking();
        LocalDateTime dt = LocalDateTime.of(Utils.getLocalDateFromString(date), LocalTime.of(Integer.parseInt(startTime.split(":")[0]), Integer.parseInt(startTime.split(":")[1]), 0));
        User user = userRepository.findUserByUserName(principal.getName()).orElse(null);
        User master = userRepository.findById(masterId).orElse(null);
        PointOfServices pos = pointOfServicesRepository.findById(posId).orElse(null);
        com.convenientservices.web.entities.Service service = serviceRepository.findById(serviceId).orElse(null);
        List<com.convenientservices.web.entities.Service> services = new ArrayList<>();
        services.add(service);
        booking.setServices(services);
        booking.setMaster(master);
        booking.setUser(user);
        booking.setDt(dt);
        booking.setPointOfServices(pos);
        bookingRepository.save(booking);
    }

    private List<BookingRow> getBookingList (Map<LocalDate, Map<String, BookingRow>> week, LocalDate selectedDate) {
        List<BookingRow> resultList = new ArrayList<>();
        int startTime = 700;
        int flag = 1;
        for (int i = 0; i < 28; i++) {
            if (flag == 1) {
                startTime += 100;
                flag = 2;
            } else if (flag == 2) {
                startTime += 30;
                flag = 3;
            } else if (flag == 3) {
                startTime += 70;
                flag = 2;
            }
            for (int j = 0; j < 7; j++) {
                resultList.add(week.get(selectedDate.plusDays(j)).get(String.valueOf(startTime)));
            }
        }
        return resultList;
    }

    private Map<String, BookingRow> getDay (LocalDate selectedDate, List<Booking> bookings) {
        Map<String, BookingRow> day = new HashMap<>();
        int startTime = 700;
        int endTime = 700;
        int length = 1;
        String span = "SPAN ";
        int flag = 1;
        for (int i = 0; i < 28; i++) {
            if (flag == 1) {
                startTime += 100;
                endTime = startTime;
                flag = 2;
            } else if (flag == 2) {
                startTime += 30;
                endTime = startTime;
                flag = 3;
            } else if (flag == 3) {
                startTime += 70;
                endTime = startTime;
                flag = 2;
            }
            day.put(String.valueOf(startTime), new BookingRow((long) i, startTime,
                    endTime, length, span, selectedDate, checkTimeToBooking(selectedDate, startTime, bookings)));
        }
        return day;
    }

    private boolean checkTimeToBooking (LocalDate selectedDate, int startTime, List<Booking> bookings) {
        LocalTime localTime = LocalTime.of(startTime / 100, startTime % 100);
        if (selectedDate.compareTo(LocalDate.now()) == 0) {
            if (localTime.isBefore(LocalTime.now())) {
                return false;
            }
        }
        return (bookings.stream()
                .filter(e -> e.getDt().toLocalDate().compareTo(selectedDate) == 0)
                .noneMatch(e -> e.getDt().toLocalTime().compareTo(localTime) == 0 ||
                        (localTime.isAfter(e.getDt().toLocalTime()) && localTime.isBefore(e.getDt().toLocalTime().plusMinutes(e.getServices().get(0).getDuration() / 60)))));
    }

    @Override
    public List<Booking> getBookingsMaster (Long id) {
        return bookingRepository.findByMasterId(id).stream()
                .filter(booking -> booking.getDt().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getDt))
                .collect(Collectors.toList());
    }


    @Async
    @Scheduled(fixedDelayString = "PT24H")
    @Override
    public void bookingReminder () {
        List<Booking> booking = bookingRepository.findAll().stream().filter(book -> book.getDt().toLocalDate()
                .isEqual(LocalDate.now().plusDays(1))).collect(Collectors.toList());
        if (!booking.isEmpty()) {
            booking.forEach(order -> mailSenderService.sendOrderReminderMessage(order));
        }
    }
}
