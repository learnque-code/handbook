package com.github.viktornar.handbook.dao;

import com.github.viktornar.handbook.dto.GuideDTO;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.Date;
import java.util.List;

public interface GuideDao {
    @SqlQuery("select * from guides where name = :name")
    GuideDTO findByName(@Bind("name") String name);

    @SqlQuery("select distinct id from guides where name = :name")
    String existsByName(@Bind("name") String name);

    @SqlQuery("select active from guides where url_path = :url_path")
    Boolean activeByUrlPath(@Bind("url_path") String urlPath);

    @SqlUpdate("update guides set url_path = :url_path, path = :path, type = :type, name = :name, description = :description, topics = :topics, repository_name = :repository_name where id = :id")
    @Transaction
    @GetGeneratedKeys("changed")
    Date updateGuide(
            @Bind("url_path") String urlPath, @Bind("path") String path, @Bind("type") String type, @Bind("name") String name,
            @Bind("description") String description, @Bind("topics") String topics, @Bind("repository_name") String repository_name, @Bind("id") String id);

    @SqlUpdate("insert into guides (url_path, path, type, name, description, topics, repository_name) values (?, ?, ?, ?, ?, ?, ?)")
    @Transaction
    @GetGeneratedKeys("id")
    String insertGuide(String urlPath, String path, String type, String name, String description, String topics, String repositoryName);

    @SqlQuery("select * from guides")
    List<GuideDTO> allGuides();

    @SqlQuery("select * from guides where id = ?")
    GuideDTO findById(String id);

    @SqlUpdate("delete from guides where id = ?")
    int deleteById(String id);

    @SqlUpdate("update guides set active = :active where id = :id")
    @Transaction
    @GetGeneratedKeys("changed")
    Date updateGuideActiveStatus(@Bind("active") Boolean active, @Bind("id") String id);
}
