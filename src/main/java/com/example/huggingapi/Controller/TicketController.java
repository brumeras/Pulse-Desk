package com.example.huggingapi.Controller;
/**
 * @author Emilija Sankauskaitė
 */
import com.example.huggingapi.Logic.TicketService;
import com.example.huggingapi.Model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/tickets")
public class TicketController
{

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<Ticket> getAllTickets()
    {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id)
    {
        return ticketService.getTicketById(id).orElse(null);
    }
}
