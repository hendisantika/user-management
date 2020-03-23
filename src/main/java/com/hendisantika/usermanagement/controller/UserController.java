package com.hendisantika.usermanagement.controller;

import com.hendisantika.usermanagement.dto.ChangePasswordForm;
import com.hendisantika.usermanagement.entity.Role;
import com.hendisantika.usermanagement.entity.User;
import com.hendisantika.usermanagement.exception.CustomFieldValidationException;
import com.hendisantika.usermanagement.exception.UsernameOrIdNotFound;
import com.hendisantika.usermanagement.repository.RoleRepository;
import com.hendisantika.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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
        Role userRole;
        List<Role> roles = new ArrayList<>();
        List<Role> userRoleList = (List<Role>) roleRepository.findAll();
        if (userRoleList == null | userRoleList.size() == 0) {
            Role role1 = new Role();
            role1.setId(1L);
            role1.setName("SUPER ADMIN");
            role1.setDescription("ROLE SUPER ADMIN");

            Role role2 = new Role();
            role2.setId(2L);
            role2.setName("ADMIN");
            role2.setDescription("ROLE ADMIN");

            Role role3 = new Role();
            role3.setId(3L);
            role3.setName("USER");
            role3.setDescription("ROLE USER");
            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);
            userRole = roleRepository.findByName("USER");
            roles = asList(userRole);
        } else {
            userRole = roleRepository.findByName("USER");
            roles = asList(userRole);
        }
        log.info("User Role List {}", userRole);
        log.info("Accesing singup page");
        model.addAttribute("signup", true);
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roles);
        return "user-form/user-signup";
    }

    @PostMapping("/signup")
    public String signupAction(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        Role userRole = roleRepository.findByName("USER");
        List<Role> roles = asList(userRole);
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

    private void baseAttributerForUserForm(Model model, User user, String activeTab) {
        model.addAttribute("userForm", user);
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute(activeTab, "active");
    }

    @GetMapping("/userForm")
    public String userForm(Model model) {
        baseAttributerForUserForm(model, new User(), TAB_LIST);
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            baseAttributerForUserForm(model, user, TAB_FORM);
        } else {
            try {
                userService.createUser(user);
                log.info("User created succesfully.");
                baseAttributerForUserForm(model, new User(), TAB_LIST);

            } catch (CustomFieldValidationException cfve) {
                result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
                baseAttributerForUserForm(model, user, TAB_FORM);
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                baseAttributerForUserForm(model, user, TAB_FORM);
                log.info("Error  on User creation.");
            }
        }
        log.info("Show user-view page");
        return "user-form/user-view";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
        User userToEdit = userService.getUserById(id);
        log.info("Show  user-edit page.");
        baseAttributerForUserForm(model, userToEdit, TAB_FORM);
        model.addAttribute("editMode", "true");
        model.addAttribute("passwordForm", new ChangePasswordForm(id));

        return "user-form/user-view";
    }

    @PostMapping("/editUser")
    public String postEditUserForm(@Valid @ModelAttribute("userForm") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            baseAttributerForUserForm(model, user, TAB_FORM);
            model.addAttribute("editMode", "true");
            model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
        } else {
            try {
                userService.updateUser(user);
                baseAttributerForUserForm(model, new User(), TAB_LIST);
                log.info("User updated successfully.");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());

                baseAttributerForUserForm(model, user, TAB_FORM);
                model.addAttribute("editMode", "true");
                model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
            }
        }
        return "user-form/user-view";

    }

    @GetMapping("/userForm/cancel")
    public String cancelEditUser() {
        return "redirect:/userForm";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(Model model, @PathVariable(name = "id") Long id) {
        try {
            userService.deleteUser(id);
            log.info("User deleted successfully.");
        } catch (UsernameOrIdNotFound uoin) {
            model.addAttribute("listErrorMessage", uoin.getMessage());
        }
        return userForm(model);
    }


    @PostMapping("/editUser/changePassword")
    public ResponseEntity postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String result = errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(""));

                throw new Exception(result);
            }
            userService.changePassword(form);
            log.info("Change password successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

}
