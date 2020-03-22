package com.hendisantika.usermanagement.controller;

import com.hendisantika.usermanagement.repository.RoleRepository;
import com.hendisantika.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
