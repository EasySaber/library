package com.example.sshomework.mappers.history;

import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.history.GenreHistory;
import com.example.sshomework.mappers.MapMapper;
import org.mapstruct.Mapper;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface GenreHistoryMapper extends MapMapper<HistoryDto, GenreHistory> {
}
