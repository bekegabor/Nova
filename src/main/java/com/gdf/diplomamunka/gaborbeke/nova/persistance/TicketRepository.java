package com.gdf.diplomamunka.gaborbeke.nova.persistance;


import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.ContactDTO;
import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;
import com.gdf.diplomamunka.gaborbeke.nova.model.Ticket;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findAllByEmployeeIdIsNull();

    List<Ticket> findAllByUser(User user);

    List<Ticket> findAllByEmployeeId(Long employeeId);


    @Query(value = "select * from Ticket ticket where ticket.employee_id=?1 and ticket.status = 'PENDING'", nativeQuery = true)
    List<Ticket> findAllByEmployeeIdAndStatusPending(Long employeeId);

    @Query(value = "select * from Ticket ticket where ticket.employee_id=?1 and ticket.status = 'CLOSED'", nativeQuery = true)
    List<Ticket> findAllByEmployeeIdAndStatusClosed(Long employeeId);

    @Query(value = "select * from Ticket ticket where ticket.employee_id=?1 and ticket.status = 'IN_PROGRESS'", nativeQuery = true)
    List<Ticket> findallByEmployeeIdAndStatusInProgress(Long employeeId);

    @Query("SELECT  new com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.ContactDTO(u.username, u.firstname, u.lastname, u.email, t.subject, t.id) FROM User u, Ticket t WHERE t.user.id = u.id AND t.employeeId =?1 AND (t.status = 'PENDING' OR t.status = 'IN_PROGRESS')")
    List<ContactDTO> getContactsByEmployeeId(Long employeeId);

    @Query(value = "SELECT COUNT(*) FROM ticket WHERE status = 'PENDING'", nativeQuery = true)
    Integer getNumberOfPendingTickets();

    @Query(value = "SELECT COUNT(*) FROM ticket WHERE status = 'CLOSED'", nativeQuery = true)
    Integer getNumberOfInProgressTickets();

    @Query(value = "SELECT COUNT(*) FROM ticket WHERE status = 'IN_PROGRESS'", nativeQuery = true)
    Integer getNumberOfClosedTickets();

    @Query(value = "SELECT COUNT(*) FROM ticket", nativeQuery = true)
    Integer getTicketsCount();
}
