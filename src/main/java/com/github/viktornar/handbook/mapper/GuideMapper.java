package com.github.viktornar.handbook.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.viktornar.handbook.domain.Guide;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

@Component
public class GuideMapper implements RowMapper<Guide>{
    @Override
    public Guide map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Guide.builder()
                .id(rs.getString("id"))
                .build();
    }
}
