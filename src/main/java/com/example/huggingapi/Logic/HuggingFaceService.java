package com.example.huggingapi.Logic;

/**
 * @author Emilija Sankauskaitė
 */

import com.example.huggingapi.AnalysisResultFromAI;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${huggingface.api.token}")
    private String apiToken;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    public HuggingFaceService()
    {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * This method is responsible for:
     * 1. Creating HTTP headers.
     * 2. Creating prompt AI.
     * 3. Creating request body.
     * 4. Sending to Hugging Face.
     * 5. Parsing the responce.
     * 6. If AI does not work, fallback is used.
     * @param commentText
     * @return
     */
    public AnalysisResultFromAI analyzeComment(String commentText)
    {
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String prompt = buildPrompt(commentText);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", prompt);

            requestBody.put("parameters", Map.of(
                    "max_new_tokens", 500,
                    "temperature", 0.3,
                    "return_full_text", false,
                    "do_sample", true,
                    "top_p", 0.95
            ));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            return parseAIResponse(response.getBody());

        }
        catch (Exception e)
        {
            return createFallbackAnalysis(commentText);
        }
    }

    /**
     * This method is crucial for AI analysis.
     * It gives a promt to AI. In other words - what to do.
     * Also describes a specific formatation for a response.
     * @param comment
     * @return
     */
    private String buildPrompt(String comment)
    {
        return String.format("""
            You are a support ticket analyzer. Analyze this user comment.
            
            User comment: "%s"
            
            Task: Determine if this needs a support ticket.
            
            Rules:
            - If it's a compliment or positive feedback → needsTicket: false
            - If it's a problem, bug, error, issue, question, or complaint → needsTicket: true
            
            Respond with ONLY this JSON structure:
            {
              "needsTicket": true or false,
              "title": "brief descriptive title",
              "category": "bug or feature or billing or account or other",
              "priority": "low or medium or high",
              "summary": "one sentence summary"
            }
            
            IMPORTANT: 
            - Problems like "can't login", "error", "not working", "broken" MUST have needsTicket: true
            - Priority should be high for: crashes, login issues, payment problems, data loss
            - Priority should be medium for: feature requests, minor bugs, account settings
            - Priority should be low for: questions, suggestions
            
            Now analyze and respond with JSON only:
            """, comment);
    }

    /**
     * This method is used for parsing the responce.
     * Since Hugging Face API returns [{"generated_text": "..."}].
     * This method extracts JSON part.
     * & converts to JSON object.
     * @param responseBody
     * @return
     */
    private AnalysisResultFromAI parseAIResponse(String responseBody)
    {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);

            String generatedText;

            if(rootNode.isArray() && rootNode.size() > 0)
            {
                generatedText = rootNode.get(0).get("generated_text").asText();
            }
            else if(rootNode.has("generated_text"))
            {
                generatedText = rootNode.get("generated_text").asText();
            }
            else
            {
                generatedText = responseBody;
            }

            String jsonPart = extractJSON(generatedText);

            return objectMapper.readValue(jsonPart, AnalysisResultFromAI.class);

        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    private String extractJSON(String text)
    {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start)
        {
            return text.substring(start, end + 1);
        }

        return text;
    }

    /**
     * Just in case AI does not work, analysis could be done here without AI.
     * for example, AI API is turned off, too many requests, internet issues.
     * @param commentText
     * @return
     */
    private AnalysisResultFromAI createFallbackAnalysis(String commentText)
    {

        boolean needsTicket = containsProblemKeywords(commentText);

        if (!needsTicket) {
            return new AnalysisResultFromAI(false, "", "", "", "");
        }

        String priority = determinePriority(commentText);
        String category = determineCategory(commentText);

        return new AnalysisResultFromAI(
                true,
                "User reported issue: " + commentText.substring(0, Math.min(60, commentText.length())),
                category,
                priority,
                commentText.substring(0, Math.min(150, commentText.length()))
        );
    }

    private boolean containsProblemKeywords(String text)
    {
        String lowerText = text.toLowerCase();
        String[] problemKeywords = {
                "bug", "error", "issue", "problem", "broken", "not working",
                "crash", "fail", "wrong", "help", "can't", "cannot", "unable",
                "impossible", "doesn't work", "won't", "freezes", "stuck",
                "lost", "missing", "disappeared", "charged", "refund"
        };

        for (String keyword : problemKeywords) {
            if (lowerText.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private String determinePriority(String text)
    {
        String lowerText = text.toLowerCase();

        String[] highKeywords = {
                "crash", "critical", "urgent", "immediately", "can't login",
                "cannot login", "impossible", "lost data", "data loss",
                "charged twice", "wrong charge", "refund", "security"
        };

        for(String keyword : highKeywords)
        {
            if(lowerText.contains(keyword))
            {
                return "high";
            }
        }

        String[] lowKeywords = {
                "suggestion", "could you", "would be nice", "maybe",
                "consider", "eventually", "question"
        };

        for (String keyword : lowKeywords)
        {
            if(lowerText.contains(keyword))
            {
                return "low";
            }
        }

        return "medium";
    }

    private String determineCategory(String text)
    {
        String lowerText = text.toLowerCase();

        if (lowerText.contains("charge") || lowerText.contains("payment") ||
                lowerText.contains("billing") || lowerText.contains("refund") ||
                lowerText.contains("subscription") || lowerText.contains("invoice")) {
            return "billing";
        }

        if (lowerText.contains("login") || lowerText.contains("password") ||
                lowerText.contains("account") || lowerText.contains("sign in") ||
                lowerText.contains("authentication") || lowerText.contains("reset")) {
            return "account";
        }

        if (lowerText.contains("feature") || lowerText.contains("add") ||
                lowerText.contains("would be") || lowerText.contains("suggest") ||
                lowerText.contains("request")) {
            return "feature";
        }

        if (lowerText.contains("bug") || lowerText.contains("crash") ||
                lowerText.contains("error") || lowerText.contains("broken") ||
                lowerText.contains("not working") || lowerText.contains("doesn't work")) {
            return "bug";
        }

        return "other";
    }
}