package com.github.viktornar.handbook.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Component
public class ChangedMapper implements RowMapper<Date> {
    @Override
    public Date map(ResultSet rs, StatementContext ctx) throws SQLException {
        return rs.getDate("changed");
    }
}
