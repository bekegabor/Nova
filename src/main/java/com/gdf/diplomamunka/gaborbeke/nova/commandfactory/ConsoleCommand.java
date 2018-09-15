package com.gdf.diplomamunka.gaborbeke.nova.commandfactory;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ConsoleCommand {
    String execute() throws IOException, URISyntaxException;
    void setParameters(String[] parameters);
}
