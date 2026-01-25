package com.example.huggingapi.Repository;

/**
 * @author Emilija Sankauskaitė
 * Only interface is needed, because Spring Data JPA automatically creates an implementation.
 * No need to write SQL queries.
 */

import com.example.huggingapi.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{

}