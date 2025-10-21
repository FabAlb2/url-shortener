package com.example.url_shortener.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlStoreService {
    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String shorten(String url) {
        String id;
        do {
            id = generateId();
        } while (store.containsKey(id));
        store.put(id, url);
        return id;
    }

    public Optional<String> resolve(String id) {
        return Optional.ofNullable(store.get(id));
    }

    private String generateId() {
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        String s = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return s.substring(0, Math.min(8, s.length()));
    }
}
