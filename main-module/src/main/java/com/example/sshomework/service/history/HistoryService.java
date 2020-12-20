package com.example.sshomework.service.history;

import com.example.sshomework.dto.history.HistoryDto;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface HistoryService {
    void addNewEntry(Long entityId, String entityName, EntityHistory entityHistory, String method);
    List<HistoryDto> getHistory(Long entityId, EntityHistory entityHistory);
}
