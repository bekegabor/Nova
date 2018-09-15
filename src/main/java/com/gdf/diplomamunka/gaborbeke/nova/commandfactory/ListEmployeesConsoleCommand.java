package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import com.gdf.diplomamunka.gaborbeke.nova.services.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class ListEmployeesConsoleCommand implements ConsoleCommand {

    private ConsoleService consoleService;
    private String[] arguments;

    @Autowired
    public ListEmployeesConsoleCommand(String[] arguments, ConsoleService consoleService){
        this.arguments = arguments;
        this.consoleService = consoleService;
    }

    public ListEmployeesConsoleCommand(){}

    @Override
    public String execute() throws IOException, URISyntaxException {
        List<String> employees = consoleService.getEmployeesList();
        if(employees.size() == 0){
            return "Jelenleg nincs egy alkalmazott sem a rendszerben regisztrálva!";
        }

        return consoleService.getEmployeesList().stream().collect(Collectors.joining(","));
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
