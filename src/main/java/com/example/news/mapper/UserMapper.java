package com.example.news.mapper;

import com.example.news.model.User;
import com.example.news.web.model.UserListResponse;
import com.example.news.web.model.UserRequest;
import com.example.news.web.model.UserResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(UserMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface UserMapper {
    User requestToUser(UserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UserRequest request);

    UserResponse userToResponse(User user);

    default UserListResponse userListToResponseList(List<User> users) {
        UserListResponse response = new UserListResponse();

        response.setUsers(users.stream()
                .map(this::userToResponse)
                .collect(Collectors.toList()));

        return response;
    }
}
