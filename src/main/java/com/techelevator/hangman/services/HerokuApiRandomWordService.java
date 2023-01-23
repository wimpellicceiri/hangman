package com.techelevator.hangman.services;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class HerokuApiRandomWordService implements RandomWordService {
    private static final String API_BASE_URL = "https://random-word-api.herokuapp.com/word";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getRandomWord(int maxLength) {
        String url = API_BASE_URL;
        if (maxLength > 0) {
            url += "?length=" + maxLength;
        }

        try {
            String[] words = restTemplate.getForObject(url, String[].class);
            if (words != null && words.length >= 1) {
                return words[0];
            }
        } catch (ResourceAccessException | RestClientResponseException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
