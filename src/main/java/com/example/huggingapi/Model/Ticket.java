package com.example.huggingapi.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor

public class Ticket
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(length = 1000)
    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Category
    {
        BUG, FEATURE, BILLING, ACCOUNT, OTHER
    }

    public enum Priority
    {
        LOW, MEDIUM, HIGH
    }

    public Ticket(Long commentId, String title, Category category, Priority priority, String summary)
    {
        this.commentId = commentId;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
        this.createdAt = LocalDateTime.now();
    }
}