package com.hendisantika.usermanagement.controller;

import com.hendisantika.usermanagement.entity.Role;
import com.hendisantika.usermanagement.entity.User;
import com.hendisantika.usermanagement.exception.CustomFieldValidationException;
import com.hendisantika.usermanagement.repository.RoleRepository;
import com.hendisantika.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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
@Slf4j
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

        log.info("Accesing singup page");
        model.addAttribute("signup", true);
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roles);
        return "user-form/user-signup";
    }

    @PostMapping("/signup")
    public String signupAction(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        Role userRole = roleRepository.findByName("USER");
        List<Role> roles = Arrays.asList(userRole);
        log.info("Creating user");
        model.addAttribute("userForm", user);
        model.addAttribute("roles", roles);
        model.addAttribute("signup", true);

        if (result.hasErrors()) {
            return "user-form/user-signup";
        } else {
            try {
                userService.createUser(user);
            } catch (CustomFieldValidationException cfve) {
                result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
            }
        }
        return index();
    }

}
