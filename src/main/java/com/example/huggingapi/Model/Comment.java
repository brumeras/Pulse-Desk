package com.example.huggingapi.Model;
/**
 * @author Emilija Sankauskaitė
 * This class provides a structure for a comment.
 * When application is ran, Spring automatically created a table,
 * which structure is based on this class.
 */
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @Entity and @Table identifies that this is a table in database.
 *
 * @Entity tells JPA that this is a DB table, @Id - primary key,
 * @GeneratedValue - will automatically generate an ID.
 * @Column(nullable = false) - table field can not be empty.
 */
@Entity
@Table(name = "comments")

public class Comment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean processed = false;

    @Column
    private Boolean needsTicket = false;

    public Comment()
    {
    }

    public Comment(String text)
    {
        this.text = text;
        this.timestamp = LocalDateTime.now();
        this.processed = false;
    }

    public Long getId()
    {
        return id;
    }

    public String getText()
    {
        return text;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public Boolean getNeedsTicket() {
        return needsTicket;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setProcessed(Boolean processed)
    {
        this.processed = processed;
    }

    public void setNeedsTicket(Boolean needsTicket)
    {
        this.needsTicket = needsTicket;
    }
}