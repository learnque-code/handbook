package com.github.viktornar.handbook.github.repositories;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
public class Repository {
    @Getter
    private final Long id;

    @Getter
    private final String name;

    @Getter
    private final String fullName;

    @Getter
    private final String description;

    @Getter
    private final String htmlUrl;

    @Getter
    private final String gitUrl;

    @Getter
    private final String sshUrl;

    @Getter
    private final String cloneUrl;

    @Getter
    private final List<String> topics;

    @JsonCreator
    public Repository(@JsonProperty("id") Long id, @JsonProperty("name") String name,
                      @JsonProperty("full_name") String fullName, @JsonProperty("description") String description,
                      @JsonProperty("html_url") String htmlUrl, @JsonProperty("git_url") String gitUrl,
                      @JsonProperty("ssh_url") String sshUrl, @JsonProperty("clone_url") String cloneUrl,
                      @JsonProperty("topics") List<String> topics) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.htmlUrl = htmlUrl;
        this.gitUrl = gitUrl;
        this.sshUrl = sshUrl;
        this.cloneUrl = cloneUrl;
        this.topics = topics;
    }
}
