package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import org.openqa.selenium.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CUProductService extends ProductServiceBase {

    private static final String CU_BASE_URL = "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=";
    private static final String CU_PB_URL = "https://cu.bgfretail.com/product/pb.do?category=product&depth2=1&sf=N";

    public CUProductService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Transactional
    public List<Product> crawlAndSavePBProducts() {
        List<Product> pbProducts = new ArrayList<>();
        try {
            driver.get(CU_PB_URL);

            // Wait for page load stabilization
            Thread.sleep(5000);

            // Use loadMoreProducts() to load all PB products if applicable
            loadMoreProducts();

            List<WebElement> productElements = driver.findElements(By.cssSelector("li.prod_list"));
            saveUniqueProducts(productElements, pbProducts, ConvenienceType.CU);

        } catch (Exception e) {
            System.err.println("An error occurred while processing products from CU PB URL.");
            e.printStackTrace();
        }

        return pbProducts;
    }


    @Override
    @Transactional
    public List<Product> crawlAndSaveProducts() {
        List<Product> allProducts = new ArrayList<>();
        try {
            for (int depth3 = 1; depth3 <= 6; depth3++) {
                driver.get(CU_BASE_URL + depth3);
                Thread.sleep(5000);
                loadMoreProducts();

                List<WebElement> productElements = driver.findElements(By.cssSelector("li.prod_list"));
                saveUniqueProducts(productElements, allProducts, ConvenienceType.CU);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while processing products from CU.");
            e.printStackTrace();
        }
        return allProducts;
    }

    @Transactional
    public void updateAvailableAtForPBProducts() {
        Set<String> pbProductNames = getPBProductNames();
        List<Product> allProducts = productRepository.findAll();

        for (Product product : allProducts) {
            if (pbProductNames.contains(product.getName())) {
                product.setAvailableAt(List.of(ConvenienceType.CU));
            } else {
                product.setAvailableAt(List.of(ConvenienceType.CU, ConvenienceType.GS, ConvenienceType.Seven));
            }
            productRepository.save(product);
        }
    }

    private Set<String> getPBProductNames() {
        // Logic for scraping PB product names from CU_PB_URL
        return Set.of();  // Return the set of PB product names
    }

    @Override
    protected String extractName(WebElement productElement) {
        String fullName = productElement.findElement(By.cssSelector(".prod_text .name p")).getText();
        int index = fullName.indexOf(")");
        return (index != -1) ? fullName.substring(index + 1).trim() : fullName;
    }

    @Override
    protected String extractPrice(WebElement productElement) {
        return productElement.findElement(By.cssSelector(".prod_text .price strong")).getText();
    }

    @Override
    protected String extractImageUrl(WebElement productElement) {
        String imgUrl = productElement.findElement(By.cssSelector(".prod_img img")).getAttribute("src");
        return imgUrl.startsWith("https:") ? imgUrl : "https:" + imgUrl;
    }
}
