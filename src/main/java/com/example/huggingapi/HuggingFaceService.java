package com.example.huggingapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceService
{
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${huggingface.api.token}")
    private String apiToken;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    public AnalysisResultFromAI analyzeComment(String commentText)
    {
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String prompt =
                    "Analyze the following user comment and decide if a support ticket is needed.\n" +
                            "Return the result as JSON with fields: needsTicket, title, category, priority, summary.\n\n" +
                            "Comment: \"" + commentText + "\"";

            Map<String, Object> body = new HashMap<>();
            body.put("inputs", prompt);

            Map<String, Object> params = new HashMap<>();
            params.put("max_new_tokens", 200);
            body.put("parameters", params);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(apiUrl, request, String.class);

            String responseBody = response.getBody();

            JsonNode root = objectMapper.readTree(responseBody);
            String generatedText = root.get(0).get("generated_text").asText();

            int start = generatedText.indexOf("{");
            int end = generatedText.lastIndexOf("}");

            String json = generatedText.substring(start, end + 1);

            return objectMapper.readValue(json, AnalysisResultFromAI.class);

        }
        catch(Exception e)
        {
            return new AnalysisResultFromAI
                    (
                    false,
                    "",
                    "",
                    "",
                    ""
            );
        }
    }
}
