package com.gdf.diplomamunka.gaborbeke.nova.services;


import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.ContactDTO;
import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;
import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import com.gdf.diplomamunka.gaborbeke.nova.model.Attachment;
import com.gdf.diplomamunka.gaborbeke.nova.model.Ticket;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.persistance.AttachmentRepository;
import com.gdf.diplomamunka.gaborbeke.nova.persistance.TicketRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, AttachmentRepository attachmentRepository){
        this.ticketRepository = ticketRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Ticket create(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketRepository.deleteById(ticket.getId());
    }

    @Override
    public void removeAttachment(Ticket ticket) {
        attachmentRepository.delete(ticket.getAttachment());
    }

    @Override
    public List<Ticket> getUserTickets(User user) {
        return ticketRepository.findAllByUser(user);
    }

    @Override
    public List<Ticket> getOpenTicketsByEmployee(User user) {
        return ticketRepository.findAllByEmployeeIdAndStatusPending(user.getId());
    }

    @Override
    public List<Ticket> getClosedTicketsByEmployee(User user) {
        return ticketRepository.findAllByEmployeeIdAndStatusClosed(user.getId());
    }

    @Override
    public List<Ticket> getInprogressTicketsByEmployee(User user) {
         return ticketRepository.findallByEmployeeIdAndStatusInProgress(user.getId());
    }

    @Override
    public List<Ticket> getTicketsByEmployee(User user) {
        return ticketRepository.findAllByEmployeeId(user.getId());
    }

    @Override
    public List<ContactDTO> getContactsForEmployeeTicketsById(User user) {
        return ticketRepository.getContactsByEmployeeId(user.getId());
    }

    @Override
    public List<Ticket> getUnassignedTickets() {
        return ticketRepository.findAllByEmployeeIdIsNull();
    }

    @Override
    public void saveAttachment(Attachment attachment) {
        attachmentRepository.save(attachment);
    }

}
