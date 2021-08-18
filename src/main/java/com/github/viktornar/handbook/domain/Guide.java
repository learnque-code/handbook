package com.github.viktornar.handbook.domain;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Builder
@Data
public class Guide {
    private String id;
    private String urlPath;
    private String path;
    private String type;
    private String name;
    private String description;
    private String topics;
    private Date created;
    private Date changed;
}
