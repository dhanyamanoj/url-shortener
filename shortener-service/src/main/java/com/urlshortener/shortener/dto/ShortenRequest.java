package com.urlshortener.shortener.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenRequest(
    @NotBlank String url,
    Integer ttlDays
) {}
