package com.gdf.diplomamunka.gaborbeke.nova.services;


import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.ContactDTO;
import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;
import com.gdf.diplomamunka.gaborbeke.nova.model.Ticket;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TicketService {

    Ticket create(Ticket ticket);
    void delete(Ticket ticket);
    void removeAttachment(Ticket ticket);
    List<Ticket>getUserTickets(User user);
    List<Ticket> getOpenTicketsByEmployee(User user);
    List<Ticket>getClosedTicketsByEmployee(User user);
    List<Ticket>getInprogressTicketsByEmployee(User user);
    List<Ticket>getTicketsByEmployee(User user);
    List<ContactDTO>getContactsForEmployeeTicketsById(User user);
    List<Ticket>getUnassignedTickets();
}
