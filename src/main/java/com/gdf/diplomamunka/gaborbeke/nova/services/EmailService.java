package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.email.Mail;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    public void sendEmail(Mail mail) throws MessagingException, IOException;
}
