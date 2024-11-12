package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceEntity;
import com.blueDragon.Convenience.Model.FoodTypeEntity;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.FoodTypeRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class ProductServiceBase {
    protected final FoodTypeRepository foodTypeRepository;
    protected final ProductRepository productRepository;
    protected WebDriver driver;

    @Transactional
    public abstract List<Product> crawlAndSaveProducts();

    protected abstract String extractName(WebElement productElement);
    protected abstract String extractPrice(WebElement productElement);
    protected abstract String extractImageUrl(WebElement productElement);

    protected WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");  // Run Chrome in headless mode
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    protected void loadMoreProducts() throws InterruptedException {
        driver = getDriver();  // Ensure driver is initialized

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

    protected void saveUniqueProducts(List<WebElement> productElements, List<Product> productsList, String storeType) {
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

                List<String> availableAt = new ArrayList<>(Integer.parseInt(storeType));
                FoodTypeEntity foodType = classifyFoodTypeByKeywords(name);

                if (foodType != null) {
                    Product product = Product.customBuilder(name, price, availableAt, List.of(String.valueOf(foodType)), imageUrl);
                    newProducts.add(product);
                }
            } catch (StaleElementReferenceException e) {
                System.err.println("Stale element encountered. Skipping this element.");
            }
        }
        productRepository.saveAll(newProducts);
        productsList.addAll(newProducts);
    }

    protected FoodTypeEntity classifyFoodTypeByKeywords(String productName) {
        productName = productName.toLowerCase();
        if (productName.contains("면") || productName.contains("우동") || productName.contains("라면") || productName.contains("파스타") || productName.contains("스파게티") || productName.contains("왕뚜껑")) {
            return foodTypeRepository.findByName("면류");
        } else if (productName.contains("밥") || productName.contains("덮밥") || productName.contains("비빔밥")) {
            return foodTypeRepository.findByName("밥류");
        } else if (productName.contains("과자") || productName.contains("초코") || productName.contains("쿠키") || productName.contains("간식") || productName.contains("튀김")) {
            return foodTypeRepository.findByName("간식류");
        } else if (productName.contains("음료") || productName.contains("주스") || productName.contains("커피") || productName.contains("차")) {
            return foodTypeRepository.findByName("음료류");
        } else if (productName.contains("다이어트") || productName.contains("샐러드") || productName.contains("저칼로리")) {
            return foodTypeRepository.findByName("다이어트류");
        } else if (productName.contains("빵") || productName.contains("케익") || productName.contains("크로와상") || productName.contains("버거") || productName.contains("샌") || productName.contains("햄")) {
            return foodTypeRepository.findByName("빵류");
        } else if (productName.contains("야식") || productName.contains("족발") || productName.contains("닭발") || productName.contains("피자") || productName.contains("치킨")) {
            return foodTypeRepository.findByName("야식류");
        } else {
            return foodTypeRepository.findByName("기타류");
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
