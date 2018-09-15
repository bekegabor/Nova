package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import com.gdf.diplomamunka.gaborbeke.nova.services.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class ListUsersConsoleCommand implements ConsoleCommand{

    private ConsoleService consoleService;
    private String[] arguments;

    @Autowired
    public ListUsersConsoleCommand(String[] arguments, ConsoleService consoleService){
        this.arguments = arguments;
        this.consoleService = consoleService;
    }

    public ListUsersConsoleCommand(){}

    @Override
    public String execute() throws IOException, URISyntaxException {
        List<String> users = consoleService.getUsersList();
        if(users.size() == 0){
            return "Jelenleg nincs egy felhasználó sem a rendszerben regisztrálva!";
        }
        return consoleService.getUsersList().stream().collect(Collectors.joining(","));
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
