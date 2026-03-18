package com.grocery.grocery_backend.config;

import com.grocery.grocery_backend.model.Product;
import com.grocery.grocery_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return; // seed once only

        List<Product> products = List.of(
                makeProduct("Fresh Bananas",     "Ripe bananas, 1 dozen",        29.99, "FRUITS_VEGETABLES", "Farm Fresh", 100, 0),
                makeProduct("Organic Tomatoes",  "Red tomatoes, 500g pack",      49.99, "FRUITS_VEGETABLES", "Organic India", 80, 10),
                makeProduct("Amul Butter",       "Salted butter, 100g",          55.00, "DAIRY_EGGS",        "Amul",        60, 0),
                makeProduct("Farm Eggs",         "Free range eggs, pack of 12",  89.00, "DAIRY_EGGS",        "Country Farm", 50, 5),
                makeProduct("Coca Cola",         "Chilled cola, 2L bottle",      95.00, "BEVERAGES",         "Coca Cola",   120, 0),
                makeProduct("Green Tea",         "Tulsi green tea, 25 bags",     199.0, "BEVERAGES",         "Organic India", 70, 15),
                makeProduct("Lays Classic",      "Salted chips, 90g",            20.00, "SNACKS",            "Lays",        200, 0),
                makeProduct("Digestive Biscuits","Whole wheat biscuits, 400g",   65.00, "SNACKS",            "McVitie's",   90, 0),
                makeProduct("Whole Wheat Bread", "Freshly baked, 400g loaf",     45.00, "BAKERY",            "Harvest Gold", 40, 0),
                makeProduct("Surf Excel",        "Detergent powder, 1kg",        185.0, "HOUSEHOLD",         "Surf Excel",  75, 10)
        );

        productRepository.saveAll(products);
        System.out.println("✅ Sample products seeded.");
    }

    private Product makeProduct(String name, String desc, double price,
                                String category, String brand,
                                int stock, double discount) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setCategory(category);
        p.setBrand(brand);
        p.setStock(stock);
        p.setDiscountPercent(discount);
        p.setImageUrl("https://via.placeholder.com/300x300?text=" +
                name.replace(" ", "+"));
        return p;
    }
}
