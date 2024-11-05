package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GSProductService extends ProductServiceBase {

    private static final String GS25_URL = "http://gs25.gsretail.com/gscvs/ko/products/youus-freshfood#";

    public GSProductService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    @Transactional
    public List<Product> crawlAndSaveProducts() {
        List<Product> gs25Products = new ArrayList<>();
        try {
            driver.get(GS25_URL);

            for (int page = 1; page <= 5; page++) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                // Wait for the product list to load on the current page
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prod_list")));

                // Collect product elements on the current page
                List<WebElement> productElements = driver.findElements(By.cssSelector("div.prod_box"));
                Thread.sleep(3000);
                saveUniqueProducts(productElements, gs25Products, ConvenienceType.GS);

                if (page < 5) {  // Only attempt to go to the next page if within limit
                    try {
                        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        WebElement nextPageButton = driver.findElement(By.xpath("//a[contains(@class, 'next')]"));

                        // Scroll into view and click next page button
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageButton);

                        // Wait for the new page to load
                        Thread.sleep(3000);
                        System.out.println("Navigated to page " + (page + 1));

                        // Wait for product elements on the new page to load
                        wait.until(ExpectedConditions.stalenessOf(productElements.get(0))); // Wait for old elements to become stale
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prod_list")));
                    } catch (NoSuchElementException e) {
                        System.out.println("Next page button not found. Ending pagination.");
                        break;
                    } catch (TimeoutException e) {
                        System.out.println("Next page button not clickable. Ending pagination.");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while processing products from GS25.");
            e.printStackTrace();
        }
        return gs25Products;
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
