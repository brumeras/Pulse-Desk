package com.example.huggingapi.Repository;

/**
 * @author Emilija Sankauskaitė
 * findByCommentI - Spring Data JPA reads method's name.
 * findBy + CommentId = generates SQL
 * Optional is used because the could be no ticket - null.
 * In that case it helps with NullPointerException.
 */

import com.example.huggingapi.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>
{
    Optional<Ticket> findByCommentId(Long commentId);
}