package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Service.CUProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final CUProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public String showProducts(Model model) {
        // 크롤링 및 저장된 제품 목록을 가져옴
        List<Product> products = productService.crawlAndSaveProducts();
        model.addAttribute("products", products);  // 모델에 추가하여 템플릿에 전달
        return "products";  // "products.html" 템플릿으로 이동
    }

    @GetMapping("/all-products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
