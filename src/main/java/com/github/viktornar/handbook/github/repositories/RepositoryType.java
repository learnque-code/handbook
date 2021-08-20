package com.github.viktornar.handbook.github.repositories;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RepositoryType {
    GETTING_STARTED("getting-started", "gs-"),
    TUTORIAL("tutorial", "tut-"),
    TOPICAL("topical", "top-"),
    ARTICLE("article", "art-"),
    PUZZLE("puzzle", "puz-"),
    UNKNOWN("unknown", "");

    private final String slug;
    private final String prefix;

    RepositoryType(final String slug, final String prefix) {
        this.slug = slug;
        this.prefix = prefix;
    }

    public static RepositoryType fromSlug(final String slug) {
        return Arrays.stream(RepositoryType.values())
                .filter(type -> type.getSlug().equals(slug))
                .findFirst().orElse(RepositoryType.UNKNOWN);
    }

    public static RepositoryType fromRepositoryName(final String repositoryName) {
        return Arrays.stream(RepositoryType.values())
                .filter(type -> repositoryName.startsWith(type.getPrefix()))
                .findFirst().orElse(RepositoryType.UNKNOWN);
    }

    public String stripPrefix(final String repositoryName) {
        return repositoryName.replaceFirst(this.prefix, "");
    }

    @JsonValue
    public String getSlug() {
        return this.slug;
    }

    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String toString() {
        return this.slug;
    }
}

