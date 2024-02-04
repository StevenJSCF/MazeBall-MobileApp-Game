package com.cs309.tutorial.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookService {

    private final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper

    @Autowired
    public BookService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/search")
    public ResponseEntity<String> searchBookByTitle(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");

        // Build the URL for the Google Books API request
        String apiUrl = GOOGLE_BOOKS_API_URL + "?q=" + title;

        // Make the API request and handle the response
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        // Check if the response status is OK (HTTP status code 200)
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Parse the JSON response using Jackson
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode items = rootNode.get("items");
                if (items != null && items.isArray() && items.size() > 0) {
                    JsonNode bookInfo = items.get(0).get("volumeInfo");

                    String publisher = bookInfo.get("publisher").asText();
                    String publishedDate = bookInfo.get("publishedDate").asText();
                    String description = bookInfo.get("description").asText();
                    String t = bookInfo.get("title").asText();

                    //hadeling if there are multiple authors or just one
                    JsonNode authorsNode = bookInfo.get("authors");
                    String[] authors = null;

                    int authorNumber = 0;
                    String totalAuthors = "";

                    if (authorsNode != null && authorsNode.isArray()) {
                        int authorCount = authorsNode.size();
                        authorNumber = authorsNode.size();

                        authors = new String[authorCount];

                        for (int i = 0; i < authorCount; i++) {
                            authors[i] = authorsNode.get(i).asText();
                        }
                    }

                    for(int i = 0; i < authorNumber; i++){
                        totalAuthors += authors[i] + ", ";

                    }

                    // Create a custom response with the extracted information
                    String customResponse = "Title: " + t + "\nAuthor: " + totalAuthors + "\nPublisher: " + publisher  + "\nPublished Date: " + publishedDate + "\nDescription: " + description;
                    return ResponseEntity.ok(customResponse);
                } else {
                    return ResponseEntity.ok("Book not found");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing JSON response");
            }
        } else {
            // Handle error cases
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }
}


