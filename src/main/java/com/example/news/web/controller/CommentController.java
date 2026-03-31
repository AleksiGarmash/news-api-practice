package com.example.news.web.controller;

import com.example.news.aop.AuthorOnly;
import com.example.news.mapper.CommentMapper;
import com.example.news.model.Comment;
import com.example.news.service.CommentService;
import com.example.news.web.model.CommentListResponse;
import com.example.news.web.model.CommentRequest;
import com.example.news.web.model.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news-service/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/to-news/{newsId}")
    public ResponseEntity<CommentListResponse> findAllByNews(@PathVariable Long newsId) {
        return ResponseEntity.ok(
                commentMapper.commentListToComment(commentService.findAllByNews(newsId))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                commentMapper.commentToResponse(commentService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentRequest request) {
        Comment comment = commentService.save(commentMapper.requestToComment(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.commentToResponse(comment));
    }

    @PutMapping("/{id}")
    @AuthorOnly
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody @Valid CommentRequest request) {
        Comment comment = commentService.update(commentMapper.requestToComment(id, request));
        return ResponseEntity.ok(commentMapper.commentToResponse(comment));
    }

    @DeleteMapping("/{id}")
    @AuthorOnly
    public ResponseEntity<CommentResponse> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
