package org.challenge.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Security token used for authentication")
public class Token {
    @Schema(description = "Token's value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

