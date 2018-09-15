package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import com.gdf.diplomamunka.gaborbeke.nova.services.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class ListBlockedUsersConsoleCommand implements ConsoleCommand {

    private ConsoleService consoleService;
    private String[] arguments;

    @Autowired
    public ListBlockedUsersConsoleCommand(String[] arguments, ConsoleService consoleService){
        this.arguments = arguments;
        this.consoleService = consoleService;
    }

    public ListBlockedUsersConsoleCommand(){}

    @Override
    public String execute() {
        List<String> blockedUsers = consoleService.getAllBlockedUser();
        if(blockedUsers.size() == 0){
            return "Jelenleg nincs egy tiltott felhasználó sem a rendszerben!";
        }

        return blockedUsers.stream().collect(Collectors.joining(","));
    }

    @Override
    public void setParameters(String[] parameters) {
        this.arguments = parameters;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
