package com.gdf.diplomamunka.gaborbeke.nova.controller;

import com.gdf.diplomamunka.gaborbeke.nova.commandfactory.*;
import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;
import com.gdf.diplomamunka.gaborbeke.nova.enums.Role;
import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import com.gdf.diplomamunka.gaborbeke.nova.services.ConsoleService;
import com.gdf.diplomamunka.gaborbeke.nova.services.FileService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.model.chart.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Data
@ViewScoped
@Component(value = "consoleController")
@ELBeanName(value = "consoleController")
@Join(path = "/admin/console", to = "/admin/console.xhtml")
public class ConsoleController {

    private String[] params;
    private Map<String, ConsoleCommand> commandsMap;
    private final ConsoleService consoleService;
    private final FileService fileService;
    private BarChartModel barModel;
    private PieChartModel pieModel;

    @Getter
    @Setter
    private List<String> helpContent;

    @Inject
    public ConsoleController(ConsoleService consoleService, FileService fileService) {
        this.consoleService = consoleService;
        this.fileService = fileService;
    }

    @PostConstruct
    public void init() {
        barModel = new BarChartModel();
        pieModel = new PieChartModel();
        params = new String[1];
        commandsMap = new HashMap<>();
        ConsoleCommand blockUser = new BlockUserConsoleCommand(params, consoleService);
        ConsoleCommand unblockUser = new UnblockUserConsoleCommand(params, consoleService);
        ConsoleCommand grantUserRole = new GrantRoleConsoleCommand(params, consoleService);
        ConsoleCommand help = new HelpConsoleCommand(params, consoleService);
        ConsoleCommand listEmployees = new ListEmployeesConsoleCommand(params, consoleService);
        ConsoleCommand listUsers = new ListUsersConsoleCommand(params, consoleService);
        ConsoleCommand createStatistics = new CreateStatisticsConsoleCommand(params, consoleService);
        ConsoleCommand listBlockedUsers = new ListBlockedUsersConsoleCommand(params, consoleService);
        commandsMap.put("block-user", blockUser);
        commandsMap.put("unblock-user", unblockUser);
        commandsMap.put("grant-role", grantUserRole);
        commandsMap.put("help", help);
        commandsMap.put("list-employees", listEmployees);
        commandsMap.put("list-users", listUsers);
        commandsMap.put("create-statistics", createStatistics);
        commandsMap.put("list-blocked-users", listBlockedUsers);
    }

    @Deferred
    @RequestAction
    @IgnorePostback
    public void triggerPostContruct(){}

    public String handleCommand(String command, String[] params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InstantiationException, IOException, URISyntaxException {
        if (!isValidConsoleCommand(command)) {
            return "Nem létező parancs: " + command;
        }

        if (!isValidParameter(params, command)) {
            return "Érvénytelen paraméter!";
        }
        ConsoleCommand currentCommand = commandsMap.get(command);
        ConsoleCommandExecutor executor = new ConsoleCommandExecutor();
        String message = executor.executeOperation(currentCommand, params);
        return message;
    }

    private Boolean isValidConsoleCommand(String command) {
        return commandsMap.containsKey(command);
    }

    private Boolean isValidParameter(String[] params, String command) {
        try {
            if ("help".equals(command) || "list-employees".equals(command) || "list-users".equals(command) || "create-statistics".equals(command) || "list-blocked-users".equals(command)) {
                return true;
            }

            if ("grant-role".equals(command)) {
                if (params[0].trim().length() > 0 && params[1].trim().length() > 0) {
                    return true;
                }
                return false;
            }

            if (params[0].trim().length() == 0 || params.length > 1) {
                return params[0].trim().length() > 0 && params[1].trim().length() == 0;
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            return false;
        }
        return params[0].trim().length() > 0;
    }


    public void createHelpWindow() throws IOException, URISyntaxException {
        helpContent = consoleService.listAvailableCommands();
    }

    public void updateChart(){
        barModel = new BarChartModel();
        List<TicketStatisticDTO> statistics = consoleService.getStatistics().stream().sorted(Comparator.comparing(TicketStatisticDTO::getMonth)).collect(Collectors.toList());
        Integer ticketsCount = Objects.isNull(consoleService.getTicketsCount()) ? 0 : consoleService.getTicketsCount();
        Integer numberOfPendingTickets = Objects.isNull(consoleService.getNumberOfPendingTickets()) ? 0 : consoleService.getNumberOfPendingTickets();
        Integer numberOfInProgressTickets = Objects.isNull(consoleService.getNumberOfInProgressTickets()) ? 0 : consoleService.getNumberOfInProgressTickets();
        Integer numberOfClosedTickets = Objects.isNull(consoleService.getNumberOfClosedTickets()) ? 0 : consoleService.getNumberOfClosedTickets();

        DateFormatSymbols hungarianDateFormatSymbols = new DateFormatSymbols(Locale.forLanguageTag("hu-HU"));
        String months[] = hungarianDateFormatSymbols.getMonths();
        List<String> roles = Stream.of(Role.values()).map(Role::name).collect(Collectors.toList());
        List<String> statuses = Stream.of(Status.values()).map(Status::name).collect(Collectors.toList());
        Map<String, String> localizedStatusCodes = new HashMap<>();
        localizedStatusCodes.put("PENDING", "Befogadás alatt");
        localizedStatusCodes.put("IN_PROGRESS", "Folyamatban");
        localizedStatusCodes.put("CLOSED", "Lezárt");

        ChartSeries pending = new ChartSeries();
        ChartSeries inProgress = new ChartSeries();
        ChartSeries closed = new ChartSeries();

        Map<String, ChartSeries> chartSeriesMaps = new HashMap<>();
        chartSeriesMaps.put(Status.IN_PROGRESS.name(), inProgress);
        chartSeriesMaps.put(Status.PENDING.name(), pending);
        chartSeriesMaps.put(Status.CLOSED.name(), closed);

        Map<Integer, Map<String, List<TicketStatisticDTO>>> groupedStatisticsMap = statistics.stream().collect(groupingBy(TicketStatisticDTO::getMonth, groupingBy(TicketStatisticDTO::getStatus)));

        for (int i=1; i<=12; i++){
            Map<String, List<TicketStatisticDTO>> currentInnerMap = groupedStatisticsMap.get(i);
            if (Objects.isNull(currentInnerMap)) {
                pending.set(months[i-1], 0);
                pending.setLabel(localizedStatusCodes.get(Status.PENDING.name()));
                chartSeriesMaps.put(Status.PENDING.name(), pending);

                inProgress.set(months[i-1], 0);
                inProgress.setLabel(localizedStatusCodes.get(Status.IN_PROGRESS.name()));
                chartSeriesMaps.put(Status.IN_PROGRESS.name(), inProgress);

                closed.set(months[i-1], 0);
                closed.setLabel(localizedStatusCodes.get(Status.CLOSED.name()));
                chartSeriesMaps.put(Status.CLOSED.name(), closed);
                continue;
            }
                for (int j=0; j<3; j++){
                    List<TicketStatisticDTO> currentInnerList = currentInnerMap.get(statuses.get(j));
                    String currentStatus = currentInnerList.get(0).getStatus();
                    ChartSeries currentChartSeries = chartSeriesMaps.get(currentStatus);
                    currentChartSeries.set(months[i-1], currentInnerList.get(0).getCount());
                }
            }

        barModel.addSeries(pending);
        barModel.addSeries(inProgress);
        barModel.addSeries(closed);
        barModel.setTitle("Hibajegyek havi bontásban");
        barModel.setLegendPosition("ne");
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Hónapok");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Darabszám");
        yAxis.setMin(0);
        yAxis.setMax(15);

        Map<String, Number> pieModelMap = new HashMap<>();
        pieModelMap.put("Folyamatban", numberOfInProgressTickets);
        pieModelMap.put("Elfogadás alatt", numberOfPendingTickets);
        pieModelMap.put("Lezárt", numberOfClosedTickets);

        pieModel = new PieChartModel();
        pieModel.setData(pieModelMap);
        pieModel.setTitle("Hibajegyek összesen: "+ticketsCount);
        pieModel.setLegendPosition("w");
        pieModel.setShadow(true);

    }

}