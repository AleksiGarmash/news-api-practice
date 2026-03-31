package com.example.news.service;

import com.example.news.exception.UpdateStateException;
import com.example.news.model.News;
import com.example.news.web.filter.NewsFilter;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public interface NewsService {
    List<News> filterBy(NewsFilter filter);
    List<News> findAll();
    News findById(Long id);
    News save(News news);
    News update(News news);
    void deleteById(Long id);


    default void checkForUpdate(Long newsId) {
        News currentNews = findById(newsId);
        Instant now = Instant.now();

        Duration duration = Duration.between(currentNews.getUpdatedAt(), now);

        if (duration.getSeconds() > 5) {
            throw new UpdateStateException("Unavailable to update Order!");
        }
    }
}
