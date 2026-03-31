package com.example.news.aop;

import com.example.news.exception.NotAuthorException;
import com.example.news.model.Comment;
import com.example.news.model.News;
import com.example.news.model.User;
import com.example.news.service.CommentService;
import com.example.news.service.NewsService;
import com.example.news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorOnlyAspect {

    private final UserService userService;
    private final NewsService newsService;
    private final CommentService commentService;

    @Before("@annotation(AuthorOnly)")
    public void checkAuthor(JoinPoint joinPoint, AuthorOnly onlyUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();;
        User currentUser = userService.findByName(currentUsername);

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Long entityId = Long.valueOf(pathVars.get("id"));
        String path = request.getRequestURI();

        if (path.contains("/news")) {
            News news = newsService.findById(entityId);
            if (!news.getUser().getId().equals(currentUser.getId())) {
                throw new NotAuthorException("Only Author can update/delete news!");
            }
        } else if (path.contains("/comments")) {
            Comment comment = commentService.findById(entityId);
            if (!comment.getUser().getId().equals(currentUser.getId())) {
                throw new NotAuthorException("Only Author can update/delete comment!");
            }
        }
    }
}
