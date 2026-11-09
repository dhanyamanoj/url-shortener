package com.urlshortener.shortener.dto;

import java.time.Instant;

public record ShortenResponse(
    String code,
    String shortUrl,
    String ownerToken,
    Instant expiresAt
) {}
