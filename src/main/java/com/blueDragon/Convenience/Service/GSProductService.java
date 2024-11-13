package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ConvenienceEntityRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GSProductService extends ProductServiceBase {

    private static final String GS25_URL = "http://gs25.gsretail.com/gscvs/ko/products/youus-freshfood#";
    private final ConvenienceEntityRepository convenienceEntityRepository;

    public GSProductService(ProductRepository productRepository, ConvenienceEntityRepository convenienceEntityRepository) {
        super(null, productRepository);
        this.convenienceEntityRepository = convenienceEntityRepository;
    }

    @Override
    @Transactional
    public List<Product> crawlAndSaveProducts() {
        List<Product> gs25Products = new ArrayList<>();
        try {
            driver.get(GS25_URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            for (int page = 1; page <= 5; page++) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prod_list")));

                // Collect product elements on the current page
                List<WebElement> productElements = driver.findElements(By.cssSelector("div.prod_box"));
                saveUniqueProducts(productElements, gs25Products, "GS");

                // Check for next page and navigate if applicable
                if (page < 5) {
                    navigateToNextPage(wait, productElements);
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while processing products from GS25.", e);
        }
        return gs25Products;
    }

    private void navigateToNextPage(WebDriverWait wait, List<WebElement> productElements) {
        try {
            WebElement nextPageButton = driver.findElement(By.xpath("//a[contains(@class, 'next')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageButton);

            // Wait for the next page's product elements to load
            wait.until(ExpectedConditions.stalenessOf(productElements.get(0)));  // Wait for old elements to become stale
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prod_list")));
        } catch (NoSuchElementException | TimeoutException e) {
            log.warn("Next page button not found or not clickable. Ending pagination.");
        }
    }

    @Override
    protected String extractName(WebElement productElement) {
        String fullName = productElement.findElement(By.cssSelector(".tit")).getText();
        int index = fullName.indexOf(")");
        return (index != -1) ? fullName.substring(index + 1).trim() : fullName;
    }

    @Override
    protected String extractPrice(WebElement productElement) {
        String priceText = productElement.findElement(By.cssSelector(".price span")).getText();
        int endIndex = priceText.indexOf("원");
        return (endIndex != -1) ? priceText.substring(0, endIndex).trim() : priceText;
    }

    @Override
    protected String extractImageUrl(WebElement productElement) {
        String imgUrl = productElement.findElement(By.cssSelector(".img img")).getAttribute("src");
        return imgUrl.startsWith("https:") ? imgUrl : "https:" + imgUrl;
    }
}
