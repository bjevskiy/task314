package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserService;


import javax.validation.Valid;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model, Principal principal) {
        model.addAttribute("admin", userService.getUserByName(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "admin";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("authorities") List<String> values) {
        user.setRoles(roleService.getRole(values));
                userService.addUser(user);
        return "redirect:/admin";
    }

    @RequestMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @ModelAttribute("user") User user,
            @RequestParam("authorities") List<String> values) {
        user.setRoles(roleService.getRole(values));
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}
