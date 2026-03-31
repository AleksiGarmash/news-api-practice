package com.example.news.web.controller;

import com.example.news.aop.AuthorOnly;
import com.example.news.mapper.NewsMapper;
import com.example.news.model.News;
import com.example.news.service.NewsService;
import com.example.news.web.model.NewsListResponse;
import com.example.news.web.model.NewsRequest;
import com.example.news.web.model.NewsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news-service/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @GetMapping
    public ResponseEntity<NewsListResponse> findAll() {
        return ResponseEntity.ok(
                newsMapper.newsListToNews(newsService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                newsMapper.newsToResponse(newsService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid NewsRequest request) {
        News news = newsService.save(newsMapper.requestToNews(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsMapper.newsToResponse(news));
    }

    @PutMapping("/{id}")
    @AuthorOnly
    public ResponseEntity<NewsResponse> update(@PathVariable Long id, @RequestBody @Valid NewsRequest request) {
        News news = newsService.update(newsMapper.requestToNews(id, request));
        return ResponseEntity.ok(newsMapper.newsToResponse(news));
    }

    @DeleteMapping("/{id}")
    @AuthorOnly
    public ResponseEntity<NewsResponse> delete(@PathVariable Long id) {
        newsService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
