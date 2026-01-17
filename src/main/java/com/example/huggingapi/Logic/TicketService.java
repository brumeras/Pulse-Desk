package com.example.huggingapi.Logic;

import com.example.huggingapi.AnalysisResultFromAI;
import com.example.huggingapi.Model.Comment;
import com.example.huggingapi.Model.Ticket;
import com.example.huggingapi.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createTicket(Comment comment, AnalysisResultFromAI analysis) {

        Ticket ticket = new Ticket();
        ticket.setCommentId(comment.getId());
        ticket.setTitle(analysis.getTitle());
        ticket.setCategory(analysis.getCategoryEnum());
        ticket.setPriority(analysis.getPriorityEnum());
        ticket.setSummary(analysis.getSummary());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets()
    {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id)
    {
        return ticketRepository.findById(id);
    }

    public Optional<Ticket> getTicketByCommentId(Long commentId)
    {
        return ticketRepository.findByCommentId(commentId);
    }
}
