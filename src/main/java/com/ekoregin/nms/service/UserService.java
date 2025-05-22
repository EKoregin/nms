package com.ekoregin.nms.service;

import com.ekoregin.nms.database.entity.User;
import com.ekoregin.nms.dto.UserCreateEditDto;
import com.ekoregin.nms.dto.UserReadDto;
import com.ekoregin.nms.mapper.UserCreateEditMapper;
import com.ekoregin.nms.mapper.UserReadMapper;
import com.ekoregin.nms.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCreateEditMapper userCreateEditMapper;
    private final UserReadMapper userReadMapper;

    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        log.info("Start create user method");
        log.info(String.valueOf(userDto));
        return Optional.of(userDto)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(userEntity -> userCreateEditMapper.map(userDto, userEntity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
