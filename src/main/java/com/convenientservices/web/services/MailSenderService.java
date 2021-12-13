package com.convenientservices.web.services;

import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.entities.User;

public interface MailSenderService {
    void sendActivateCode(User user);
    void sendRestoreCode(User user);
    void sendBookingCancellationMessage(Booking booking);
    void sendOrderReminderMessage(Booking booking);
}
