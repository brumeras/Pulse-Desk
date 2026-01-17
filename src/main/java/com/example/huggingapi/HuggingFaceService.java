package com.example.huggingapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceService {

    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${huggingface.api.token}")
    private String apiToken;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    public HuggingFaceService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public AnalysisResultFromAI analyzeComment(String commentText) {
        try {
            logger.info("Analyzing comment with Hugging Face AI: {}", commentText);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String prompt = buildPrompt(commentText);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", prompt);
            requestBody.put("parameters", Map.of(
                    "max_new_tokens", 300,
                    "temperature", 0.1,
                    "return_full_text", false
            ));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            logger.debug("AI Response: {}", response.getBody());

            return parseAIResponse(response.getBody());

        } catch (Exception e) {
            logger.error("Error calling Hugging Face API: {}", e.getMessage(), e);
            return createFallbackAnalysis(commentText);
        }
    }

    private String buildPrompt(String comment) {
        return String.format("""
            Analyze this user comment and determine if it requires a support ticket.
            
            Comment: "%s"
            
            Respond ONLY with valid JSON in this exact format (no additional text):
            {
              "needsTicket": true,
              "title": "brief title here",
              "category": "bug",
              "priority": "high",
              "summary": "one sentence summary here"
            }
            
            Rules:
            - category must be one of: bug, feature, billing, account, other
            - priority must be one of: low, medium, high
            - If the comment is just a compliment or positive feedback, set needsTicket to false
            - If needsTicket is false, you can leave other fields empty
            
            JSON:
            """, comment);
    }

    private AnalysisResultFromAI parseAIResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);

            String generatedText;
            if (rootNode.isArray() && rootNode.size() > 0) {
                generatedText = rootNode.get(0).get("generated_text").asText();
            } else if (rootNode.has("generated_text")) {
                generatedText = rootNode.get("generated_text").asText();
            } else {
                generatedText = responseBody;
            }

            logger.debug("Extracted text: {}", generatedText);

            String jsonPart = extractJSON(generatedText);

            return objectMapper.readValue(jsonPart, AnalysisResultFromAI.class);

        } catch (JsonProcessingException e) {
            logger.error("Failed to parse AI response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    private String extractJSON(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }

        return text;
    }

    private AnalysisResultFromAI createFallbackAnalysis(String commentText) {
        logger.warn("Using fallback analysis for comment");

        boolean needsTicket = containsProblemKeywords(commentText);

        if (!needsTicket) {
            return new AnalysisResultFromAI(false, "", "", "", "");
        }

        return new AnalysisResultFromAI(
                true,
                "User reported issue",
                "other",
                "medium",
                "Issue requires investigation: " + commentText.substring(0, Math.min(100, commentText.length()))
        );
    }

    private boolean containsProblemKeywords(String text) {
        String lowerText = text.toLowerCase();
        String[] problemKeywords = {
                "bug", "error", "issue", "problem", "broken", "not working",
                "crash", "fail", "wrong", "help", "can't", "cannot", "unable"
        };

        for (String keyword : problemKeywords) {
            if (lowerText.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}