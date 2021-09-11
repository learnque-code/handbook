package com.github.viktornar.handbook.mapper;

import com.github.viktornar.handbook.domain.Guide;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GuideMapper implements RowMapper<Guide>{
    @Override
    public Guide map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Guide.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .type(rs.getString( "type"))
                .path(rs.getString("path"))
                .urlPath(rs.getString("url_path"))
                .topics(rs.getString("topics"))
                .changed(rs.getDate("changed"))
                .created(rs.getDate("created"))
                .active(rs.getBoolean("active"))
                .repositoryName(rs.getString("repository_name"))
                .build();
    }
}
