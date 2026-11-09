package com.urlshortener.shortener.controller;

import com.urlshortener.shortener.dto.ShortenRequest;
import com.urlshortener.shortener.dto.ShortenResponse;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Day 1 route stubs. Codes are random Base62 (7–8 chars), never Base62(DB id).
 */
@RestController
@RequestMapping
public class ShortenerController {

  @PostMapping("/api/shorten")
  public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
    // Day 2+: validate URL, generate random Base62 code, persist, return ownerToken
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
        .body(new ShortenResponse(null, null, null, null));
  }

  @GetMapping("/r/{code}")
  public ResponseEntity<Map<String, String>> redirect(@PathVariable String code) {
    // Day 2+: lookup mapping, respect expires_at, 302; Day 4+: cache + fail-open ClickEvent
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
        .body(Map.of(
            "error", "not_implemented",
            "message", "Redirect stub — implement in Days 2–5",
            "path", "/r/" + code));
  }

  @DeleteMapping("/api/{code}")
  public ResponseEntity<Map<String, String>> delete(
      @PathVariable String code,
      @RequestHeader(value = "X-Owner-Token", required = false) String ownerToken) {
    // Day 2+: require X-Owner-Token; Day 4+: evict Redis
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
        .body(Map.of(
            "error", "not_implemented",
            "message", "Delete stub — implement in Days 2–4",
            "path", "/api/" + code));
  }
}
