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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@Tag(name = "상품 API")
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
    @Operation(summary = "상품 리스트 - 가격순", description = "상품 리스트를 가격순으로 필터링합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getProductsByPrice(
            @Parameter(required = true, description = "기준이 되는 금액")
            @PathVariable int price) {
        List<ProductDto> list = productService.getProductByPrice(price);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping("/newest")
    @Operation(summary = "상품 리스트 - 최신순", description = "상품 리스트를 최신순으로 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getProductByNewest() {
        List<ProductDto> list = productService.getProductByNewest();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping("/liked")
    @Operation(summary = "내가 좋아요 누른 상품 불러오기", description = "토큰을 사용하여 내가 좋아요 누른 상품을 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getLikedProductsByUser(@Valid @AuthenticationPrincipal User user) {
        List<ProductDto> list = productService.getLikedProductsByUser(user.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping("/popular")
    public ResponseEntity<ResponseDTO<?>> getProductByPopular() {
        List<ProductDto> list = productService.getProductByPopular();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO<?>> getProductByCategory(@PathVariable String category) {
        List<ProductDto> list = productService.getProductByCategory(category);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping("/convenience/{convenience}")
    public ResponseEntity<ResponseDTO<?>> getProductByConvenience(@PathVariable("convenience") String name) {
        List<ProductDto> list = productService.getProductsByConvenience(name);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT_LIST, list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> getProductByCategory(@PathVariable Long id) {
        ProductDto res = productService.getProductById(id);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_PRODUCT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_PRODUCT, res));
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
