package com.example.huggingapi.Logic;
/**
 * @author Emilija Sankauskaitė
 */
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

    @Autowired
    private HuggingFaceService huggingFaceService;

    public Comment processComment(String text)
    {
        Comment comment = new Comment(text);
        comment = commentRepository.save(comment);

        AnalysisResultFromAI analysis = huggingFaceService.analyzeComment(text);

        comment.setProcessed(true);
        comment.setNeedsTicket(analysis.isNeedsTicket());

        if(analysis.isNeedsTicket())
        {
            ticketService.createTicket(comment, analysis);
        }

        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments()
    {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
}