package com.github.viktornar.handbook;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@ConfigurationProperties("handbook")
@Validated
public class HandbookProperties {
    @Getter
    private final Github github = new Github();

    @Getter
    private final Guides guides = new Guides();

    @Getter
    private final Web web = new Web();

    @Getter
    @Setter
    public static class Github {
        @Pattern(regexp = "([0-9a-z]*)?")
        private String token;
    }

    @Getter
    @Setter
    public static class Guides {
        private String organization = "learnque-guides";
    }

    @Getter
    @Setter
    public static class Web {
        private String home = "./web";
        private String bin = "./bin";
        private String tmp = "./web";
    }
}
