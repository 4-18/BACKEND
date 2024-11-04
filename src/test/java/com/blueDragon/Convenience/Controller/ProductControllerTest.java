package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Service.CUProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc  // MockMvc를 자동으로 설정하고 주입하도록 합니다.
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CUProductService productService;

    @Test
    public void testCrawlAndSaveProductsEndpoint() throws Exception {
        // Assuming the endpoint is GET /api/products/crawl
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());


        // 서비스 메서드가 한 번 호출되었는지 검증
        verify(productService, times(1)).crawlAndSaveProducts();
    }
}