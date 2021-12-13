package com.convenientservices.web.services;

import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService{

    private final JavaMailSender mailSender;
    @Value("${mail.port}")
    private int port;
    @Value("${mail.host}")
    private String hostname;
    @Value("${mail.server.username}")
    private String sender;


    public MailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendActivateCode(User user) {
        String subject = "please activate your account";
        String content = "Please activate your account. Go to the link: \n"
                + "http://" + hostname + ":" + port + "/users/activate/" + user.getActivationCode();
        sendMail(user.getEmail(), subject, content);
    }

    @Override
    public void sendRestoreCode(User user) {
        String subject = "please change your password";
        String content = "Please change your password. Go to the link: \n"
                + "http://" + hostname + ":" + port + "/change/" + user.getChangeCode();
        sendMail(user.getEmail(), subject, content);
    }

    @Override
    public void sendBookingCancellationMessage (Booking booking) {
        String subject = "Cancellation of booking " + booking.getId();
        String content = "Бронирование №"+booking.getId() +" на "+ booking.getDt().toLocalTime() + " " +
                booking.getDt().toLocalDate()+ " было отменено клиентом "+booking.getUser().getUserName();
        sendMail(booking.getUser().getEmail(), subject, content);
        sendMail(booking.getMaster().getEmail(), subject, content);
    }

    @Override
    public void sendOrderReminderMessage (Booking booking) {
        String subject = "order reminder";
        String content = "Напоминаем Вам, что вы записаны на " + booking.getDt().toLocalTime() + " "
                + booking.getDt().toLocalDate() + " к мастеру " + booking.getMaster().getLastName() + " по адресу "
                + booking.getPointOfServices().getAddress().getAddress1();
        sendMail(booking.getUser().getEmail(), subject, content);
    }

    private void sendMail(String email, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
