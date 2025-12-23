package com.example.claudeapi.coupon.presentation;

import com.example.claudeapi.coupon.application.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponViewController {

    private final CouponService couponService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("coupons", couponService.findAll());
        return "coupons/list";
    }

    @GetMapping("/new")
    public String createForm() {
        return "coupons/form";
    }

    @GetMapping("/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("coupon", couponService.findById(id));
        return "coupons/form";
    }
}
