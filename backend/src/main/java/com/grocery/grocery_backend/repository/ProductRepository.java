package com.grocery.grocery_backend.repository;

import com.grocery.grocery_backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByIsActiveTrue();

    List<Product> findByCategoryAndIsActiveTrue(String category);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'isActive': true }")
    List<Product> searchByName(String keyword);

    @Query("{ 'price': { $gte: ?0, $lte: ?1 }, 'isActive': true }")
    List<Product> findByPriceRange(double min, double max);

    @Query("{ $and: [ " +
            "{ 'isActive': true }, " +
            "{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'category': { $regex: ?0, $options: 'i' } } ] }, " +
            "{ 'price': { $gte: ?1, $lte: ?2 } } " +
            "] }")
    List<Product> searchWithFilters(String keyword, double minPrice, double maxPrice);
}
