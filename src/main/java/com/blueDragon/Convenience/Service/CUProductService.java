package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.FoodType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CUProductService {

    private final ProductRepository productRepository;
    private static final String BASE_URL = "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=";

    @Transactional // 트랜잭션 사용
    public List<Product> crawlAndSaveProducts() {
        List<Product> allProducts = new ArrayList<>();
        WebDriver driver = ChromeDriver.builder().build();

        try {
            for (int depth3 = 1; depth3 <= 6; depth3++) {
                // depth3가 5일 때 가공식사 항목 클릭
                if (depth3 == 5) {
                    driver.get(BASE_URL + "5");
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                    try {
                        // JavaScript를 사용해 식품 카테고리 강제 선택
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("document.querySelector('li.eventInfo_02 a').click();");

                        // 클릭 후 안정화를 위해 대기
                        Thread.sleep(3000);

                        // 카테고리가 활성화되었는지 확인
                        WebElement activeCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.eventInfo_02.on")));
                        if (activeCategory == null) {
                            System.err.println("Category did not switch to '가공식사'");
                        }
                    } catch (TimeoutException e) {
                        System.err.println("Timed out waiting for '가공식사' category to load and activate.");
                    }
                } else {
                    String url = BASE_URL + depth3;
                    driver.get(url);
                }

                // 페이지 로드 후 안정화 대기
                Thread.sleep(7000); // 충분한 대기 시간 설정

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prodListWrap")));

                // Load More 버튼 반복 클릭
                while (true) {
                    try {
                        WebElement loadMoreButton = driver.findElement(By.cssSelector(".prodListBtn-w a"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loadMoreButton);
                        Thread.sleep(4000);
                    } catch (NoSuchElementException e) {
                        break;
                    }
                }

                Thread.sleep(3000);  // 추가 안정화 대기

                List<WebElement> productElements;
                System.out.println("여기까진 옴?");
                try {
                    System.out.println("대기 시작 ;;;");
                    productElements = driver.findElements(By.cssSelector("li.prod_list"));
                    System.out.println("모든 productElements 로드 완료" + productElements);
                } catch (TimeoutException e) {
                    System.err.println("Timed out waiting for product elements to load.");
                    continue;
                }

                List<Product> products = new ArrayList<>();

                for (WebElement productElement : productElements) {
                    try {
                        String fullName = productElement.findElement(By.cssSelector(".prod_text .name p")).getText();
                        String priceText = productElement.findElement(By.cssSelector(".prod_text .price strong")).getText();
                        String imgUrl = productElement.findElement(By.cssSelector(".prod_img img")).getAttribute("src");

                        // 이름이 없으면 건너뜀
                        if (fullName.isEmpty()) {
                            System.err.println("Warning: Product name is empty. Skipping this product.");
                            continue;
                        }

                        // 가격이 없으면 건너뜀
                        if (priceText.isEmpty()) {
                            System.err.println("Warning: Product price is empty. Skipping this product.");
                            continue;
                        }

                        priceText = priceText.isEmpty() ? "0" : priceText;
                        imgUrl = imgUrl.isEmpty() ? "이미지 없음" : (imgUrl.startsWith("https:") ? imgUrl : "https:" + imgUrl);

                        List<ConvenienceType> availableAt = List.of(ConvenienceType.CU);

                        FoodType foodType;
                        if (depth3 == 3 || depth3 == 4) {
                            foodType = FoodType.간식류;
                        } else if (depth3 == 6) {
                            foodType = FoodType.음료류;
                        } else {
                            foodType = classifyFoodTypeByKeywords(fullName);
                        }

                        if (foodType != null) {
                            Product product = Product.builder()
                                    .price(priceText)
                                    .imgUrl(imgUrl)
                                    .name(fullName)
                                    .availableAt(availableAt)
                                    .foodTypes(List.of(foodType))
                                    .build();
                            products.add(product);
                        }
                    } catch (StaleElementReferenceException e) {
                        System.err.println("Stale element encountered. Skipping this element.");
                    }
                }

                // 모든 Product 엔티티 일괄 저장 후 확인
                productRepository.saveAll(products);
                productRepository.flush();
                allProducts.addAll(products);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while processing products.");
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return allProducts;
    }

    private FoodType classifyFoodTypeByKeywords(String productName) {
        productName = productName.toLowerCase();

        if (productName.contains("면") || productName.contains("우동") || productName.contains("라면") || productName.contains("파스타") || productName.contains("스파게티") || productName.contains("왕뚜껑")) {
            return FoodType.면류;
        } else if (productName.contains("밥") || productName.contains("덮밥") || productName.contains("비빔밥") || productName.contains("주)") || productName.contains("김)") || productName.contains("도)")) {
            return FoodType.밥류;
        } else if (productName.contains("과자") || productName.contains("초코") || productName.contains("쿠키") || productName.contains("간식") || productName.contains("튀김")) {
            return FoodType.간식류;
        } else if (productName.contains("음료") || productName.contains("주스") || productName.contains("커피") || productName.contains("차")) {
            return FoodType.음료류;
        } else if (productName.contains("다이어트") || productName.contains("샐러드") || productName.contains("저칼로리")) {
            return FoodType.다이어트류;
        } else if (productName.contains("빵") || productName.contains("케익") || productName.contains("크로와상") || productName.contains("버거") || productName.contains("샌)") || productName.contains("햄)")) {
            return FoodType.빵류;
        } else if (productName.contains("야식") || productName.contains("족발") || productName.contains("닭발") || productName.contains("피자") || productName.contains("치킨")) {
            return FoodType.야식류;
        } else {
            return FoodType.기타류;
        }
    }
}
