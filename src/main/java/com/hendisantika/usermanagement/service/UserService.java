package com.hendisantika.usermanagement.service;

import com.hendisantika.usermanagement.entity.User;
import com.hendisantika.usermanagement.exception.CustomFieldValidationException;
import com.hendisantika.usermanagement.exception.UsernameOrIdNotFound;
import com.hendisantika.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 22/03/20
 * Time: 10.15
 */
@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Iterable<User> getAllUsers() {
        return repository.findAll();
    }

    private boolean checkUsernameAvailable(User user) throws Exception {
        Optional<User> userFound = repository.findByUsername(user.getUsername());
        if (userFound.isPresent()) {
            throw new CustomFieldValidationException("Username not available", "username");
        }
        return true;
    }

    private boolean checkPasswordValid(User user) throws Exception {
        if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
            throw new CustomFieldValidationException("Confirm Password is required", "confirmPassword");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new CustomFieldValidationException("Password and Confirm Password are not the same", "password");
        }
        return true;
    }

    public User createUser(User user) throws Exception {
        if (checkUsernameAvailable(user) && checkPasswordValid(user)) {
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user = repository.save(user);
        }
        return user;
    }

    public User getUserById(Long id) throws UsernameOrIdNotFound {
        return repository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("User id does not exist."));
    }

    public User updateUser(User fromUser) throws Exception {
        User toUser = getUserById(fromUser.getId());
        mapUser(fromUser, toUser);
        return repository.save(toUser);
    }


    /**
     * Map everythin but the password.
     *
     * @param from
     * @param to
     */
    protected void mapUser(User from, User to) {
        to.setUsername(from.getUsername());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setRoles(from.getRoles());
    }
}
