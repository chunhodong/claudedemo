package com.example.claudedemo.controller;

import com.example.claudedemo.dto.UserDto;
import com.example.claudedemo.entity.User;
import com.example.claudedemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "users/form";
    }

    @PostMapping
    public String create(@ModelAttribute UserDto dto) {
        userService.create(dto);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        UserDto dto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge());
        model.addAttribute("user", dto);
        return "users/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UserDto dto) {
        userService.update(id, dto);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
