package com.example.huggingapi.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {

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

    public Comment(String text) {
        this.text = text;
        this.timestamp = LocalDateTime.now();
        this.processed = false;
    }
}