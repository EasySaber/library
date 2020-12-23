package com.example.sshomework.service.history;

import com.example.sshomework.dto.history.HistoryDto;

import java.util.List;

/**
 * @author Aleksey Romodin
 *
 * Управление историей операций над сущностями
 */
public interface HistoryService {
    void addNewHistory(Long entityId, String method);
    List<HistoryDto> getHistoryEntity(Long entityId);
    void commitEntity(Long entityId);
}
