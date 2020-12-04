package com.example.sshomework.service.user;

import com.example.sshomework.dto.user.UserDto;
import com.example.sshomework.entity.User;
import com.example.sshomework.exception.NotUniqueValueException;
import com.example.sshomework.mappers.UserMapper;
import com.example.sshomework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserDto> getAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public void addNewUser(UserDto user) throws NotUniqueValueException {
            User userInput = userMapper.toEntity(user);
            String pass = bCryptPasswordEncoder.encode(userInput.getPassword());
            userInput.setPassword(pass);
            try{
                userRepository.save(userInput);
            } catch (DataIntegrityViolationException ex) {
                if (((SQLException) ex.getMostSpecificCause()).getSQLState().equals("23505")) {
                    throw new NotUniqueValueException("Аккаунт с таким именем уже существует");
                }
            }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Пользователь не найден"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
