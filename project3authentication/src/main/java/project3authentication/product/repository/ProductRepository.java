package project3authentication.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project3authentication.product.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

//    List<Product> findByNameContainingIgnoreCase(String name);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

//    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

//    Page<Product> findByCategoryIdAndPriceBetween(Long categoryId, Double minPrice, Double maxPrice, Pageable pageable);

//    @Query("""
//            SELECT p FROM Product p
//            LEFT JOIN FETCH p.category c
//            WHERE (:categoryId IS NULL OR c.id = :categoryId)
//              AND (:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
//              AND (:minPrice IS NULL OR p.price >= :minPrice)
//              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
//            """)
//    List<Product> findProductsByFilters(
//            @Param("categoryId") Long categoryId,
//            @Param("name") String name,
//            @Param("minPrice") Double minPrice,
//            @Param("maxPrice") Double maxPrice
//    );

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN FETCH p.category c
            WHERE (:categoryId IS NULL OR c.id = :categoryId)
              AND (:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    Page<Product> findProductsByFilters(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
