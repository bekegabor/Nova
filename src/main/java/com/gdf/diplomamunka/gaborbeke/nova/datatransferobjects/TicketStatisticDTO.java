package com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects;

import lombok.Data;

@Data
public class TicketStatisticDTO {

    private Integer month;
    private String status;
    private Long count;

    public TicketStatisticDTO(Integer month, String status, Long count) {
        this.month = month;
        this.status = status;
        this.count = count;
    }
}
