package com.library.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app.dues")
@Validated
public record DuesSettings(@NotNull @Min(1) Long borrowDuration, @NotNull @Min(0) Double finePerDay) {
}
