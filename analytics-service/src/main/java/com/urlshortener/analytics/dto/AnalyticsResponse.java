package com.urlshortener.analytics.dto;

import java.time.Instant;
import java.util.List;

public record AnalyticsResponse(
    String code,
    long totalClicks,
    List<ClickSummary> recentClicks
) {
  public record ClickSummary(
      Instant clickedAt,
      String ipHash
  ) {}
}
