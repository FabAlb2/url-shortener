package com.example.url_shortener.controller;

import com.example.url_shortener.model.ShortenRequest;
import com.example.url_shortener.service.UrlStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShortenController.class)
public class ShortenControllerTest {

    @Autowired
    private MockMvc mvc;

    // Wir mocken den Service, damit kein echter Speicher nötig ist
    @MockBean
    private UrlStoreService store;

    @Test
    void testShortenReturnsId() throws Exception {
        // Wenn shorten() aufgerufen wird, gib "abc123" zurück
        given(store.shorten(anyString())).willReturn("abc123");

        // Sende POST Request mit JSON
        String json = "{\"url\": \"https://example.com\"}";

        mvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"));
    }

    @Test
    void testRedirectFound() throws Exception {
        // Wenn resolve("abc123") aufgerufen wird, gib eine URL zurück
        given(store.resolve("abc123"))
                .willReturn(Optional.of("https://example.com"));

        mvc.perform(get("/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void testRedirectNotFound() throws Exception {
        // Wenn ID unbekannt -> leeres Optional
        given(store.resolve("doesnotexist"))
                .willReturn(Optional.empty());

        mvc.perform(get("/doesnotexist"))
                .andExpect(status().isNotFound());
    }
}
