package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.FoodType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CUProductService {

    private final ProductRepository productRepository;
    private static final String BASE_URL = "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=";

    public List<Product> crawlAndSaveProducts() {
        List<Product> products = new ArrayList<>();
        WebDriver driver = ChromeDriver.builder().build();

        try {
            for (int depth3 = 1; depth3 <= 6; depth3++) {

                String url = BASE_URL + depth3;
                driver.get(url);

                // Load More 버튼 클릭
                while (true) {
                    try {
                        WebElement loadMoreButton = driver.findElement(By.cssSelector(".prodListBtn-w a"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loadMoreButton);
                        Thread.sleep(5000);
                    } catch (NoSuchElementException e) {
                        break;
                    }
                }
                Thread.sleep(5000);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".prodListWrap > ul")));

                List<WebElement> productElements = driver.findElements(By.cssSelector("li.prod_list"));
                for (WebElement productElement : productElements) {
                    String fullName = productElement.findElement(By.cssSelector(".prod_text .name p")).getText();
                    String priceText = productElement.findElement(By.cssSelector(".prod_text .price strong")).getText();
                    String imgUrl = productElement.findElement(By.cssSelector(".prod_img img")).getAttribute("src");

// fullName이 빈 문자열인 경우 "이름 없음"으로 설정하고, ")" 이후의 문자열만 저장
                    if (!fullName.isEmpty()) {
                        int index = fullName.indexOf(")");
                        fullName = (index != -1) ? fullName.substring(index + 1).trim() : fullName; // ")" 이후의 문자열만 저장
                    } else {
                        fullName = "이름 없음";
                    }

// 가격과 이미지 URL이 비어 있는 경우 기본값 설정
                    priceText = priceText.isEmpty() ? "0" : priceText;
                    imgUrl = imgUrl.isEmpty() ? "이미지 없음" : "https:" + imgUrl;
                    priceText = priceText.isEmpty() ? "0" : priceText;
                    imgUrl = imgUrl.isEmpty() ? "이미지 없음" : "https:" + imgUrl;

                    List<ConvenienceType> availableAt = List.of(ConvenienceType.CU);
                    // depth3 값에 따라 FoodType 설정
                    FoodType foodType;
                    if (depth3 == 3 || depth3 == 4) {
                        foodType = FoodType.간식류;
                    } else if (depth3 == 6) {
                        foodType = FoodType.음료류;
                    } else {
                        foodType = classifyFoodTypeByKeywords(fullName); // 나머지 경우는 키워드 기반 분류
                    }

                    if (foodType != null) {
                        Product product = Product.builder()
                                .price(priceText)
                                .imgUrl(imgUrl)
                                .name(fullName)
                                .availableAt(availableAt)
                                .foodTypes(List.of(foodType))
                                .build();
                        productRepository.save(product);
                        products.add(product);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while processing products.");
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return products;
    }

    private FoodType classifyFoodTypeByKeywords(String productName) {
        productName = productName.toLowerCase(); // 대소문자 구분 없이 비교하기 위해 소문자로 변환

        if (productName.contains("면") || productName.contains("우동") || productName.contains("라면") || productName.contains("파스타")) {
            return FoodType.면류;
        } else if (productName.contains("밥") || productName.contains("덮밥") || productName.contains("비빔밥")) {
            return FoodType.밥류;
        } else if (productName.contains("과자") || productName.contains("초코") || productName.contains("쿠키") || productName.contains("간식") || productName.contains("튀김")) {
            return FoodType.간식류;
        } else if (productName.contains("음료") || productName.contains("주스") || productName.contains("커피") || productName.contains("차")) {
            return FoodType.음료류;
        } else if (productName.contains("다이어트") || productName.contains("샐러드") || productName.contains("저칼로리")) {
            return FoodType.다이어트류;
        } else if (productName.contains("빵") || productName.contains("케익") || productName.contains("크로와상") || productName.contains("버거")) {
            return FoodType.빵류;
        } else if (productName.contains("야식") || productName.contains("족발") || productName.contains("닭발") || productName.contains("피자") || productName.contains("치킨")) {
            return FoodType.야식류;
        } else {
            return null; // 분류할 수 없는 경우 null 반환
        }
    }
}
