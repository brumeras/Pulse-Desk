package com.example.huggingapi.Logic;
/**
 * @author Emilija Sankauskaitė
 * @Autowired helps with DI
 * This class works only with comments in order to not break Single Responsibility Principle.
 * Work flow: User submits a comment -> Save to DB (get ID) -> Send AI for analysis
 * -> Update comment (processed=true) -> Create ticket if needed -> Return updated comment
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

    /**
     * This method is one of the most essential.
     * 1. It creates a comment (Comment comment = new Comment(text);)
     * 2. Sends it to analyse for AI.
     * 3. Updates a comment (comment.setProcessed(true);)
     * 4. If needed, a ticket is created (ticketService.createTicket(comment, analysis);)
     * @param text
     * @return
     */
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