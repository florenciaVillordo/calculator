package com.tenpo.calculator.security.service;

import com.tenpo.calculator.security.dto.UserDto;
import com.tenpo.calculator.security.model.Role;
import com.tenpo.calculator.security.model.RoleType;
import com.tenpo.calculator.security.model.User;
import com.tenpo.calculator.security.repository.RoleRepository;
import com.tenpo.calculator.security.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;


    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void save(UserDto userDto) {
        User user = User.builder().username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword()))
                .build();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findByRoleName(RoleType.USER));
        user.setRoles(userRoles);
        userRepository.save(user);
        log.debug("User {} was created", user.getUsername());
    }
}
