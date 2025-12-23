package com.example.claudeapi.product.presentation;

import com.example.claudeapi.product.application.ProductRequest;
import com.example.claudeapi.product.application.ProductResponse;
import com.example.claudeapi.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        return "products/form";
    }

    @PostMapping
    public String create(@ModelAttribute ProductRequest request) {
        productService.create(request);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProductResponse product = productService.findById(id);
        ProductRequest request = new ProductRequest(product.getId(), product.getName(), product.getSellerId(), product.getCategoryNumber());
        model.addAttribute("product", request);
        return "products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute ProductRequest request) {
        productService.update(id, request);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
