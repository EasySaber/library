package com.example.sshomework.dto.history;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
public class HistoryDto {
    private Long id;
    private ZonedDateTime dateTime;
    private String userName;
    private String operation;
    private Long entityId;
    private String entityName;
}
