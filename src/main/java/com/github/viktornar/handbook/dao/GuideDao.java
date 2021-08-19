package com.github.viktornar.handbook.dao;

import com.github.viktornar.handbook.domain.Guide;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface GuideDao {
    @SqlQuery("select * from guides where name = :name")
    Guide findByName(@Bind("name") String name);

    @SqlUpdate("insert into guides (url_path, path, type, name, description, topics) values (?, ?, ?, ?, ?, ?)")
    @GetGeneratedKeys("id")
    String insertGuide(String urlPath, String path, String type, String name, String description, String topics);
}
