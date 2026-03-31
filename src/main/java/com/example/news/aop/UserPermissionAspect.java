package com.example.news.aop;

import com.example.news.model.User;
import com.example.news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class UserPermissionAspect {

    private final UserService userService;

    @Before("@annotation(com.example.news.aop.UserPermission)")
    public void checkUserPermission(JoinPoint joinPoint) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();;
        User currentUser = userService.findByName(currentUsername);

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Long targetUserId =  Long.valueOf(pathVars.get("id"));
        User targetUser = userService.findById(targetUserId);

        if (!targetUser.getId().equals(currentUser.getId()) && !isAdminOrModerator(auth)) {
            throw new AccessDeniedException("Access only to your own profile or for Admin/Moderator!");
        }
    }

    private boolean isAdminOrModerator(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()) ||
                        "ROLE_MODERATOR".equals(a.getAuthority()));
    }
}
