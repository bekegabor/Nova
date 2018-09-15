package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ConsoleService {
    String blockUser(String username);
    List<String> listAvailableCommands() throws URISyntaxException, IOException;
    String unblockUser(String username);
    String grantRole(String username, String role);
    List<String> getEmployeesList();
    List<String> getUsersList();
    List<String> getAllBlockedUser();
    List<TicketStatisticDTO> getStatistics();
    Integer getTicketsCount();
    Integer getNumberOfPendingTickets();
    Integer getNumberOfInProgressTickets();
    Integer getNumberOfClosedTickets();
}
