package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.CUProductService;
import com.blueDragon.Convenience.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc  // MockMvc를 자동으로 설정하고 주입하도록 합니다.
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    private CUProductService cuProductService;

    @Test
    public void testCrawlAndSaveProductsEndpoint() throws Exception {
        // Assuming the endpoint is GET /api/products/crawl
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());


        // 서비스 메서드가 한 번 호출되었는지 검증
        verify(cuProductService, times(1)).crawlAndSaveProducts();
    }

}