package com.gdf.diplomamunka.gaborbeke.nova.controller;

import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.ContactDTO;
import com.gdf.diplomamunka.gaborbeke.nova.email.Mail;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.EmailService;
import com.gdf.diplomamunka.gaborbeke.nova.services.TicketService;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RequestScope
@Component(value = "emailSendController")
@ELBeanName(value = "emailSendController")
@Join(path = "/employee/email", to = "/employee/email/sendemail.xhtml")
public class EmailSendController {

    @Getter
    @Setter
    private Mail mail;

    @Getter
    @Setter
    private List<ContactDTO> assignedTickets;

    @Getter
    @Setter
    private List<SelectItem> contactEmails;

    @Getter
    @Setter
    private Integer emailAddressAndSubjectSelectDialogWidth;

    @Getter
    @Setter
    private List<String> emailAddresses;


    private final UserService userService;
    private final TicketService ticketService;
    private final EmailService emailService;

    @Autowired
    public EmailSendController(UserService userService, TicketService ticketService, EmailService emailService){
        this.userService = userService;
        this.ticketService = ticketService;
        this.emailService = emailService;
    }


    @PostConstruct
    public void init(){
        mail = new Mail();
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        Optional<User> loggedInEmployee = userService.getUserByUsername(username);
        assignedTickets = ticketService.getContactsForEmployeeTicketsById(loggedInEmployee.get());
        assignedTickets = assignedTickets.stream().distinct().collect(Collectors.toList());
        contactEmails = assignedTickets.stream().map(contact -> new SelectItem(contact.getEmail(), contact.getFirstname()+" "+contact.getLastName()+" <"+contact.getEmail()+">")).distinct().collect(Collectors.toList());

    }

    public void validateAndSendEmail() throws MessagingException, IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String currentUserName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        if (isEditorContainsOnlyEmptyHtml()){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "A szóköz nem elfogadott érték!"));
            return;
        }
        prepareUserAndEmailAddress(mail);
        emailService.sendEmail(mail);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Az email sikeresen el lett küldve a "+mail.getSendTo()+" címre!"));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()+"/employee/home.xhtml");
    }

    private Boolean isEditorContainsOnlyEmptyHtml(){
       String message = mail.getMessage().replaceAll("&nbsp;","").replaceAll("<p>|</p>|<br>|</br>|<div>|</div>|\t|\r|\n","");
       return message.trim().length() == 0;
    }

    public void prepareUserAndEmailAddress(Mail mail){
        if (contactEmails.size() > 0) {
           String fullName = assignedTickets
                   .stream()
                   .filter(assignedticket -> (assignedticket.getEmail()).equals(mail.getSendTo()))
                   .map(assignedTicket -> assignedTicket.getFirstname() +" "+assignedTicket.getLastName())
                   .findFirst()
                   .orElse("Felhasználó");
            mail.setUsername(fullName);
        }
    }
}
