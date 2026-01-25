package com.example.huggingapi;
/**
 * @author Emilija Sankauskaitė
 * This class is responsible for DTO - it saves an AI response.
 * @JsonProperty maps JSON fields to JAVA fields.
 * Since AI returns JSON file, Spring automatically converts this JSON to AnalysisResultFromAI object.
 */
import com.example.huggingapi.Model.Ticket;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AnalysisResultFromAI {

    @JsonProperty("needsTicket")
    private boolean needsTicket;

    @JsonProperty("title")
    private String title;

    @JsonProperty("category")
    private String category;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("summary")
    private String summary;

    public AnalysisResultFromAI()
    {
    }

    public AnalysisResultFromAI(boolean needsTicket, String title, String category, String priority, String summary)
    {
        this.needsTicket = needsTicket;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
    }

    public boolean isNeedsTicket() {
        return needsTicket;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getSummary() {
        return summary;
    }

    public void setNeedsTicket(boolean needsTicket) {
        this.needsTicket = needsTicket;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Methods getCategoryEnum() and getPriorityEnum() set categories and priorities to tickets.
     * AI returns text "bug", therefore, it needs to be converted to enum (Ticket.Category.BUG).
     * valueOf("BUG") converts to ENUM.
     * If AI makes a mistake and returns nonsense, category - other.
     */
    public Ticket.Category getCategoryEnum()
    {
        if(category == null)
        {
            return Ticket.Category.OTHER;
        }
        try
        {
            return Ticket.Category.valueOf(category.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return Ticket.Category.OTHER;
        }
    }

    public Ticket.Priority getPriorityEnum()
    {
        if(priority == null)
        {
            return Ticket.Priority.MEDIUM;
        }
        try
        {
            return Ticket.Priority.valueOf(priority.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return Ticket.Priority.MEDIUM;
        }
    }
}