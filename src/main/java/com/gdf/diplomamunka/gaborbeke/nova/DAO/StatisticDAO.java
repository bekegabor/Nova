package com.gdf.diplomamunka.gaborbeke.nova.DAO;

import com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects.TicketStatisticDTO;
import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StatisticDAO {

    private final JdbcTemplate jdbcTemplate;

    public StatisticDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TicketStatisticDTO> getStatistics(){

        return jdbcTemplate.query("select month(create_date) as month, status, count(*) as count " +
                "from ticket " +
                "where year(create_date) = year(current_date) " +
                "group by month(create_date), status ", new RowMapper<TicketStatisticDTO>() {
            @Override
            public TicketStatisticDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                return new TicketStatisticDTO(resultSet.getInt("month"), resultSet.getString("status"), resultSet.getLong("count"));
            }
        });

    }

    public int getTicketsCount(){
        RowCountCallbackHandler handler = new RowCountCallbackHandler();
        jdbcTemplate.query("select * from ticket", handler);
        return handler.getRowCount();
    }

    public int getNumberOfPendingTickets(){
        RowCountCallbackHandler handler = new RowCountCallbackHandler();
        jdbcTemplate.query("SELECT * FROM ticket WHERE status = 'PENDING'", handler);
        return handler.getRowCount();
    }

    public int getNumberOfInProgressTickets(){
        RowCountCallbackHandler handler = new RowCountCallbackHandler();
        jdbcTemplate.query("SELECT * FROM ticket WHERE status = 'IN_PROGRESS'", handler);
        return handler.getRowCount();
    }

    public int getNumberOfClosedTickets(){
        RowCountCallbackHandler handler = new RowCountCallbackHandler();
        jdbcTemplate.query("SELECT * FROM ticket WHERE status = 'CLOSED'", handler);
        return handler.getRowCount();
    }

}
