package com.svalero.enajenarte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO de salida para la versión 2 de workshops. Amplía la información que recibe el cliente sin modificar la respuesta de la versión 1.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkshopOutDtoV2 {

    private long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private int durationMinutes;
    private float price;
    private boolean isOnline;
    private int maxCapacity;

    private String speakerName;
}