package com.gdf.diplomamunka.gaborbeke.nova.validator.calendar;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class DateOfBirth {

    private LocalDate dateOfBirth;

    public DateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


}
