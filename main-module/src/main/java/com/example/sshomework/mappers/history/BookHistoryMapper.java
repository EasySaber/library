package com.example.sshomework.mappers.history;

import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.history.BookHistory;
import com.example.sshomework.mappers.MapMapper;
import org.mapstruct.Mapper;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface BookHistoryMapper extends MapMapper<HistoryDto, BookHistory> {
}
