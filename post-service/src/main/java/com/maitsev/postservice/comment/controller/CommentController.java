package com.maitsev.postservice.comment.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maitsev.postservice.comment.dto.CommentDto;
import com.maitsev.postservice.comment.service.CommentService;
import com.maitsev.postservice.post.service.PostService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PostService postService;  // Inject the PostService

    @GetMapping("/posts/{id}/comments")
    public List<CommentDto> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/posts/{id}/comments/{commentId}")
    public Optional<CommentDto> getComment(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    @PostMapping("/posts/{id}/comments")
    public void addComment(@RequestBody CommentDto commentDto) {
        commentService.addComment(commentDto);
    }

    @PutMapping("/posts/{id}/comments/{commentId}")
    public void updateComment(@RequestBody CommentDto commentDto, @PathVariable Long commentId) {
        commentService.updateComment(commentId, commentDto);
    }

    @DeleteMapping("/posts/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
