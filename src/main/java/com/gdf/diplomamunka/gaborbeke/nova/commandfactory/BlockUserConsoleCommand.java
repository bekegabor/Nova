package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import com.gdf.diplomamunka.gaborbeke.nova.services.ConsoleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


public class BlockUserConsoleCommand implements ConsoleCommand {

    private ConsoleService consoleService;
    private String[] arguments;

    @Autowired
    public BlockUserConsoleCommand(String[] arguments, ConsoleService consoleService){
        this.arguments = arguments;
        this.consoleService = consoleService;
    }

    public BlockUserConsoleCommand(){}

    @Override
    public String execute() {
        return consoleService.blockUser(arguments[0]);
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
