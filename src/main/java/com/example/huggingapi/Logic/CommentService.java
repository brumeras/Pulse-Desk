package com.example.huggingapi.Logic;

import com.example.huggingapi.AnalysisResultFromAI;
import com.example.huggingapi.HuggingFaceService;
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
    private HuggingFaceService huggingFaceService;

    @Autowired
    private TicketService ticketService;

    public Comment processComment(String text)
    {

        Comment comment = new Comment(text);
        comment = commentRepository.save(comment);

        try
        {
            AnalysisResultFromAI analysis = huggingFaceService.analyzeComment(text);

            comment.setProcessed(true);
            comment.setNeedsTicket(analysis.isNeedsTicket());

            if(analysis.isNeedsTicket())
            {
                ticketService.createTicket(comment, analysis);
            }

            comment = commentRepository.save(comment);

        }
        catch (Exception e)
        {
            comment.setProcessed(false);
            comment = commentRepository.save(comment);
        }

        return comment;
    }

    public List<Comment> getAllComments()
    {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id)
    {
        return commentRepository.findById(id);
    }
}
