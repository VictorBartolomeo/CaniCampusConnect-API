package org.example.canicampusconnectapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseCardDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private int maxCapacity;
    private String courseTypeName;
    private String coachName;
    private long registrationCount;
}