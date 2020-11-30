package com.example.sshomework.service.user;

import com.example.sshomework.dto.UserDto;
import com.example.sshomework.entity.User;
import com.example.sshomework.mappers.UserMapper;
import com.example.sshomework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Aleksey Romodin
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public boolean addNewUser(UserDto user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(userMapper.toEntity(user));
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
