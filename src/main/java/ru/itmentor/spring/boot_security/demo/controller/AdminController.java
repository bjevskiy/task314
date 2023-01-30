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
    public String showAllUsers(Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/newUser")
    public String createUserForm(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "add-user-form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("userForm") @Valid User userForm,
                           BindingResult bindingResult,
                           @RequestParam("authorities") List<String> values) {
        userForm.setRoles(roleService.getRole(values));
        if (bindingResult.hasErrors()) {
            return "add-user-form";
        }
        userService.addUser(userForm);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String showFormForUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "update-user-form";
    }

    @RequestMapping("/updateUser/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            @RequestParam("authorities") List<String> values) {
        user.setRoles(roleService.getRole(values));
        if (bindingResult.hasErrors()) {
            return "update-user-form";
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @RequestMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}
