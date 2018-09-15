package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import com.gdf.diplomamunka.gaborbeke.nova.services.ConsoleService;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;

public class HelpConsoleCommand  implements ConsoleCommand {

    private ConsoleService consoleService;
    private String[] arguments;

    @Autowired
    public HelpConsoleCommand(String[] arguments, ConsoleService consoleService){
        this.arguments = arguments;
        this.consoleService = consoleService;
    }

    public HelpConsoleCommand(){}

    @Override
    public String execute() throws IOException, URISyntaxException {
        PrimeFaces.current().executeScript("$('#helpButton').click();");
        return "Súgó elindítva!";
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
