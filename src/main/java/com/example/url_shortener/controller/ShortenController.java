package com.example.url_shortener.controller;

import com.example.url_shortener.model.ShortenRequest;
import com.example.url_shortener.service.UrlStoreService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class ShortenController {
    private final UrlStoreService store;

    public ShortenController(UrlStoreService store) {
        this.store = store;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shorten(@RequestBody ShortenRequest req, HttpServletRequest request) {
        if (req.getUrl() == null || req.getUrl().isBlank()) {
            return ResponseEntity.badRequest().body("URL is required");
        }
        String id = store.shorten(req.getUrl());
        String base = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String shortUrl = base + "/" + id;

        HashMap<String, String> response = new HashMap<>();
        response.put("id", id);
        response.put("shortUrl", shortUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> redirect(@PathVariable String id) {
        return store.resolve(id)
                .map(url -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", url);
                    return new ResponseEntity<>(headers, HttpStatus.FOUND);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
    }
}
