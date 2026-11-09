package com.urlshortener.analytics.controller;

import com.urlshortener.analytics.dto.AnalyticsResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Day 1 route stub. Consumer + real aggregates land in Days 6–7.
 */
@RestController
public class AnalyticsController {

  @GetMapping("/analytics/{code}")
  public ResponseEntity<AnalyticsResponse> analytics(@PathVariable String code) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
        .body(new AnalyticsResponse(code, 0L, List.of()));
  }
}
