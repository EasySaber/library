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

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public boolean addNewUser(UserDto user) {
        if (userUnique(user.getUsername())) {
            userRepository.save(userMapper.toEntity(user));
            return true;
        }
        return false;
    }

    public boolean userUnique(String userName) {
        return userRepository.findByUsername(userName) == null;
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
