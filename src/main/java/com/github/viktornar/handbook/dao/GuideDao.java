package com.github.viktornar.handbook.dao;

import com.github.viktornar.handbook.domain.Guide;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface GuideDao {
    @SqlQuery("select * from guides where name = :name")
    Guide findByName(@Bind("name") String name);
}
