package com.example.huggingapi.Controller;
/**
 * @author Emilija Sankauskaitė
 * What happens: POST http://localhost:8080/comments ->
 * Body: {"text": "The app is broken!"} ->
 * Spring converts JSON to CommentRequest ->
 * Calls commentService.processComment() ->
 * Returns Comment object like JSON
 */
import com.example.huggingapi.Logic.CommentService;
import com.example.huggingapi.Model.Comment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController - @Controller + @ResponseBody (returns JSON)
 * @RequestMapping("/comments") - base URL path
 * @PostMapping - `POST /comments`
 * @GetMapping - `GET /comments`
 * @RequestBody - converts JSON to JAVA objects
 * @Valid - validates provided data
 */
@RestController
@RequestMapping("/comments")
public class CommentController
{

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment submitComment(@Valid @RequestBody CommentRequest request)
    {
        return commentService.processComment(request.getText());
    }

    @GetMapping
    public List<Comment> getAllComments()
    {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id)
    {
        return commentService.getCommentById(id).orElse(null);
    }

    public static class CommentRequest
    {
        private String text;
        public String getText()
        {
            return text;
        }
        public void setText(String text)
        {
            this.text = text;
        }
    }
}

