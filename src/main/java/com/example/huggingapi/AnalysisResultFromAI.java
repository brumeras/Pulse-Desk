package com.example.huggingapi;

import com.example.huggingapi.Model.Ticket;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AnalysisResultFromAI
{
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
        catch(IllegalArgumentException e)
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
