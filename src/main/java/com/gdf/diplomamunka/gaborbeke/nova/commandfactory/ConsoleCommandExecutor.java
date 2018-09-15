package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleCommandExecutor {

    private final List<ConsoleCommand> consoleCommands = new ArrayList<>();

    public String executeOperation(ConsoleCommand consoleCommand, String[] parameters) throws IOException, URISyntaxException {
        consoleCommand.setParameters(parameters);
        consoleCommands.add(consoleCommand);
        return consoleCommand.execute();
    }
}
