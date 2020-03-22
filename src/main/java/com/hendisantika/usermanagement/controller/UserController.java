package com.hendisantika.usermanagement.controller;

import com.hendisantika.usermanagement.entity.Role;
import com.hendisantika.usermanagement.entity.User;
import com.hendisantika.usermanagement.repository.RoleRepository;
import com.hendisantika.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 22/03/20
 * Time: 10.39
 */
@Controller
public class UserController {

    private final String TAB_FORM = "formTab";
    private final String TAB_LIST = "listTab";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping({"/", "/login"})
    public String index() {
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        Role userRole = roleRepository.findByName("USER");
        List<Role> roles = Arrays.asList(userRole);

        model.addAttribute("signup", true);
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roles);
        return "user-form/user-signup";
    }
}
