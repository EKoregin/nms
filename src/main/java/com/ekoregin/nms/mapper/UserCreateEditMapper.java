package com.ekoregin.nms.mapper;

import com.ekoregin.nms.dto.UserCreateEditDto;
import com.ekoregin.nms.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(UserCreateEditDto fromObject, User toUser) {
        toUser.setUsername(fromObject.getUsername());
        toUser.setBirthDate(fromObject.getBirthDate());
        toUser.setFirstname(fromObject.getFirstname());
        toUser.setLastname(fromObject.getLastname());
        toUser.setRole(fromObject.getRole());

        Optional.ofNullable(fromObject.getRawPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(toUser::setPassword);
    }
}