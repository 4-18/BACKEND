package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Service.CUProductService;
import com.blueDragon.Convenience.Service.GSProductService;
import com.blueDragon.Convenience.Service.ProductService;
import com.blueDragon.Convenience.Service.SevenProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final CUProductService cuProductService;
    private final GSProductService gsProductService;
    private final SevenProductService sevenProductService;
    private final ProductService productService;


//    @GetMapping("/")
//    public ResponseEntity<ResponseDTO<?>> getProuductList(@Valid @PathVariable String sort) {
//        List<ProductDto> list = productService.getList(sort);
//    }

    @GetMapping("/price/{price}")
    public ResponseEntity<ResponseDTO<?>> getProductsByPrice(@PathVariable int price) {
        List<ProductDto> list = productService.getProductByPrice(price);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping("/newest")
    public ResponseEntity<ResponseDTO<?>> getProductByNewest() {
        List<ProductDto> list = productService.getProductByNewest();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping()
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

}
