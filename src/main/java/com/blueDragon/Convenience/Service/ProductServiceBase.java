package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.FoodType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public abstract class ProductServiceBase {

    protected final ProductRepository productRepository;
    protected WebDriver driver = ChromeDriver.builder().build();

    @Transactional
    public abstract List<Product> crawlAndSaveProducts();

    protected abstract String extractName(WebElement productElement);
    protected abstract String extractPrice(WebElement productElement);
    protected abstract String extractImageUrl(WebElement productElement);


    protected void loadMoreProducts() throws InterruptedException {
        while (true) {
            try {
                WebElement loadMoreButton = driver.findElement(By.cssSelector(".prodListBtn-w a"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loadMoreButton);
                Thread.sleep(3000);
            } catch (NoSuchElementException e) {
                break;
            }
        }
        Thread.sleep(3000);  // Additional wait for page stabilization
    }

    protected void saveUniqueProducts(List<WebElement> productElements, List<Product> productsList, ConvenienceType storeType) {
        List<Product> newProducts = new ArrayList<>();
        for (WebElement productElement : productElements) {
            try {
                String name = extractName(productElement);
                String price = extractPrice(productElement);
                String imageUrl = extractImageUrl(productElement);

                if (!validation(name, price)) continue;

                // Check for duplicates
                if (productRepository.existsByNameAndPrice(name, price)) {
                    System.out.println("Duplicate product found: " + name + ", Price: " + price);
                    continue;
                }

                List<ConvenienceType> availableAt = List.of(storeType);
                FoodType foodType = classifyFoodTypeByKeywords(name);

                if (foodType != null) {
                    Product product = Product.customBuilder(name, price, availableAt, List.of(foodType), imageUrl);
                    newProducts.add(product);
                }
            } catch (StaleElementReferenceException e) {
                System.err.println("Stale element encountered. Skipping this element.");
            }
        }
        productRepository.saveAll(newProducts);
        productsList.addAll(newProducts);
    }

    protected FoodType classifyFoodTypeByKeywords(String productName) {
        productName = productName.toLowerCase();
        if (productName.contains("면") || productName.contains("우동") || productName.contains("라면") || productName.contains("파스타") || productName.contains("스파게티") || productName.contains("왕뚜껑")) {
            return FoodType.면류;
        } else if (productName.contains("밥") || productName.contains("덮밥") || productName.contains("비빔밥")) {
            return FoodType.밥류;
        } else if (productName.contains("과자") || productName.contains("초코") || productName.contains("쿠키") || productName.contains("간식") || productName.contains("튀김")) {
            return FoodType.간식류;
        } else if (productName.contains("음료") || productName.contains("주스") || productName.contains("커피") || productName.contains("차")) {
            return FoodType.음료류;
        } else if (productName.contains("다이어트") || productName.contains("샐러드") || productName.contains("저칼로리")) {
            return FoodType.다이어트류;
        } else if (productName.contains("빵") || productName.contains("케익") || productName.contains("크로와상") || productName.contains("버거") || productName.contains("샌") || productName.contains("햄")) {
            return FoodType.빵류;
        } else if (productName.contains("야식") || productName.contains("족발") || productName.contains("닭발") || productName.contains("피자") || productName.contains("치킨")) {
            return FoodType.야식류;
        } else {
            return FoodType.기타류;
        }
    }

    protected boolean validation(String fullName, String priceText) {
        if (fullName.isEmpty() || priceText.isEmpty()) {
            System.err.println("Warning: Missing product name or price. Skipping this product.");
            return false;
        }
        return true;
    }
}
