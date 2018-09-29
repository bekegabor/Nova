package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.DAO.StatisticDAO;
import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;
import com.gdf.diplomamunka.gaborbeke.nova.enums.Role;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ConsoleServiceImpl implements ConsoleService{

    private final UserService userService;
    private final TicketService ticketService;
    private final FileService fileService;
    private final StatisticDAO statisticDAO;


    @Autowired
    public ConsoleServiceImpl(UserService userService, TicketService ticketService, FileService fileService, StatisticDAO statisticDAO){
        this.userService = userService;
        this.ticketService = ticketService;
        this.fileService = fileService;
        this.statisticDAO = statisticDAO;
    }


    @Override
    public String blockUser(String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()){
            if(!user.get().isEnabled()){
                return "A megadott felhasználói fiók már blokkolva van: "+username;
            }
            userService.updateUserEnabledStatusByUsername(0, username);
            return "Felhasználói fiók ("+username+") sikeresen blokkolva lett!";
        }
        return "A megadott felhasználói fiók nem létezik: "+username;
    }

    @Override
    public List<String> listAvailableCommands() throws URISyntaxException, IOException {
        return fileService.readTextFileFromClasspath();
    }

    @Override
    public String unblockUser(String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()){
            if(user.get().isEnabled()){
                return "A megadott felhasználói fiók már engedélyezve van: "+username;
            }
            userService.updateUserEnabledStatusByUsername(1, username);
            return "Felhasználói fiók ("+username+") sikeresen engedélyezve lett!";
        }
        return "A megadott felhasználói fiók nem létezik: "+username;
    }

    @Override
    public String grantRole(String username, String role) {
        Optional<User> user = userService.getUserByUsername(username);
        boolean isExistingRole = Stream.of(Role.values()).map(Role::name).anyMatch(existingRole -> existingRole.equals(role));
        if (user.isPresent()){
            if (!isExistingRole){
                return "A megadott ROLE nem létezik: "+role+" (lehetséges opciók: USER, ADMIN, EMPLOYEE)";
            }
            userService.updateUserRoleByUsername(role, username);
            return "A felhasználó ("+username+") új jogosultsága ("+role+") sikeresen módosítva lett!";
        }
        return "A megadott felhasználói fiók nem létezik: "+username;
    }

    @Override
    public List<String> getEmployeesList() {

        return userService.getEmployees();
    }

    @Override
    public List<String> getUsersList() {
        return userService.getUsers();
    }

    @Override
    public List<String> getAllBlockedUser() {
        return userService.getBlockedUsers();
    }

    @Override
    public List<TicketStatisticDTO> getStatistics() {
        return statisticDAO.getStatistics();
    }

    @Override
    public Integer getTicketsCount() {
        return statisticDAO.getTicketsCount();
    }

    @Override
    public Integer getNumberOfPendingTickets() {
        return statisticDAO.getNumberOfPendingTickets();
    }

    @Override
    public Integer getNumberOfInProgressTickets() {
        return statisticDAO.getNumberOfInProgressTickets();
    }

    @Override
    public Integer getNumberOfClosedTickets() {
        return statisticDAO.getNumberOfClosedTickets();
    }
    }



