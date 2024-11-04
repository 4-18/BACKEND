package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.FoodType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CUProductService {
    private final ProductRepository productRepository;
    private static final Map<String, FoodType> foodTypeMap = new HashMap<>();

    static {
        foodTypeMap.put("주)", FoodType.밥류);
        foodTypeMap.put("김)", FoodType.밥류);
        foodTypeMap.put("도)", FoodType.밥류);
        foodTypeMap.put("샌)", FoodType.빵류);
        foodTypeMap.put("햄)", FoodType.빵류);
        foodTypeMap.put("면)", FoodType.면류);
        foodTypeMap.put("샐)", FoodType.다이어트류);
    }

    private static final String BASE_URL = "https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=";

    public List<Product> crawlAndSaveProducts() {
        List<Product> products = new ArrayList<>();

        for (int depth3 = 1; depth3 <= 6; depth3++) {
            // depth3 값이 2일 때는 스킵
            if (depth3 == 2) {
                continue;
            }

            String url = BASE_URL + depth3;

            try {
                Document doc;
                // depth3가 6일 때는 "면"을 검색어로 추가하여 요청
                if (depth3 == 6) {
                    doc = Jsoup.connect(url)
                            .data("searchKeyword", "면")  // 검색어 "면"을 쿼리 파라미터로 추가
                            .get();
                } else {
                    doc = Jsoup.connect(url).get();
                }

                Elements productElements = doc.select("li.prod_list"); // 각 제품 리스트 항목

                for (Element productElement : productElements) {
                    // 제품명 추출
                    String fullName = productElement.select(".name p").text();

                    // 가격 추출
                    String priceText = productElement.select(".price strong").text();
                    int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
                    List<ConvenienceType> availableAt = List.of(ConvenienceType.CU);  // CU 편의점에서만 구매 가능
                    FoodType foodType = null;

                    // depth3에 따른 기본 FoodType 설정
                    if (depth3 == 3 || depth3 == 4) {
                        foodType = FoodType.간식류;
                    } else if (depth3 == 5) {
                        foodType = FoodType.음료류;
                    } else if (depth3 == 6 && fullName.contains("면")) {
                        foodType = FoodType.면류;
                    }

                    // foodType이 설정된 경우에만 저장
                    if (foodType != null) {
                        Product product = Product.builder()
                                .price(price)
                                .name(fullName)
                                .availableAt(availableAt)
                                .foodTypes(List.of(foodType))
                                .build();

                        productRepository.save(product);
                        products.add(product);
                    }
                }

            } catch (IOException e) {
                System.err.println("Failed to retrieve data from URL: " + url);
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("An error occurred while processing products on page: " + url);
                e.printStackTrace();
            }
        }

        return products;
    }



    public Product createProduct(String fullName, Integer price, List<ConvenienceType> availableAt) {
        int endIndex = fullName.indexOf(")") + 1;
        String name = fullName;

        FoodType foodType = null;

        if (endIndex > 0 && endIndex < fullName.length()) {
            // Extract prefix and name if ')' is found
            String prefix = fullName.substring(0, endIndex);
            foodType = foodTypeMap.get(prefix);
            name = fullName.substring(endIndex).trim(); // Use name after prefix
        } else {
            System.out.println("Prefix not found or invalid format, using full name");
        }

        // Override FoodType based on keywords in name
        if (name.contains("빵")) {
            foodType = FoodType.빵류;
        } else if (name.contains("샐러드")) {
            foodType = FoodType.다이어트류;
        } else if (name.contains("피자") || name.contains("치킨") || name.contains("족발")) {
            foodType = FoodType.야식류;
        }

        // If foodType is still null, decide whether to assign a default or allow null
        if (foodType == null) {
            System.out.println("FoodType not determined for product: " + fullName);
        }

        Product product = Product.builder()
                .price(price)
                .name(name)
                .availableAt(availableAt)
                .foodTypes(foodType != null ? List.of(foodType) : new ArrayList<>())
                .build();

        return productRepository.save(product);  // Save and return the product
    }

}
