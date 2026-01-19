package com.example.huggingapi.Logic;

import com.example.huggingapi.AnalysisResultFromAI;
import com.example.huggingapi.Model.Comment;
import com.example.huggingapi.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService
{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketService ticketService;

    public Comment processComment(String text)
    {
        Comment comment = new Comment(text);
        comment = commentRepository.save(comment);

        AnalysisResultFromAI analysis = simpleAnalysis(text);
        comment.setProcessed(true);
        comment.setNeedsTicket(analysis.isNeedsTicket());

        if(analysis.isNeedsTicket())
        {
            ticketService.createTicket(comment, analysis);
        }

        return commentRepository.save(comment);
    }

    private AnalysisResultFromAI simpleAnalysis(String text)
    {
        String lower = text.toLowerCase();
        boolean isPositive = lower.contains("love") || lower.contains("great") || lower.contains("amazing");

        if (isPositive)
        {
            return new AnalysisResultFromAI(false, "", "", "", "");
        }

        return new AnalysisResultFromAI(
                true,
                "User issue: " + text.substring(0, Math.min(50, text.length())),
                "bug",
                "medium",
                text.substring(0, Math.min(100, text.length()))
        );
    }

    public List<Comment> getAllComments()
    {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
}