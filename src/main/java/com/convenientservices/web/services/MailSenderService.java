package com.convenientservices.web.services;

import com.convenientservices.web.entities.User;

public interface MailSenderService {
    void sendActivateCode(User user);
}
