package com.example.claudedemo.user.presentation;

import com.example.claudedemo.user.application.UserRequest;
import com.example.claudedemo.user.application.UserResponse;
import com.example.claudedemo.user.application.UserService;
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
        model.addAttribute("user", new UserRequest());
        return "users/form";
    }

    @PostMapping
    public String create(@ModelAttribute UserRequest request) {
        userService.create(request);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UserResponse user = userService.findById(id);
        UserRequest request = new UserRequest(user.getId(), user.getName(), user.getEmail(), user.getAge());
        model.addAttribute("user", request);
        return "users/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UserRequest request) {
        userService.update(id, request);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
