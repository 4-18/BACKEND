package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Service.CUProductService;
import com.blueDragon.Convenience.Service.GSProductService;
import com.blueDragon.Convenience.Service.SevenProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final CUProductService cuProductService;
    private final GSProductService gsProductService;
    private final SevenProductService sevenProductService;
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public String showProducts(Model model) {
        // 크롤링 및 저장된 제품 목록을 가져옴
        List<Product> products = cuProductService.crawlAndSaveProducts();
        model.addAttribute("products", products);  // 모델에 추가하여 템플릿에 전달
        return "products";  // "products.html" 템플릿으로 이동
    }

    @GetMapping("/pb-products/")
    public void getPBProducts() {
        cuProductService.updateAvailableAtForPBProducts();
    }

    @GetMapping("/pb-products/gs")
    public void getGSProducts() {
        gsProductService.crawlAndSaveProducts();
    }

    @GetMapping("/pb-products/seven")
    public void getSevenProducts() {
        sevenProductService.crawlAndSaveProducts();
    }

    @GetMapping("/all-products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
