package com.dhkstnaos.shoeorder.product.repository;

import com.dhkstnaos.shoeorder.product.domain.Category;
import com.dhkstnaos.shoeorder.product.domain.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static com.dhkstnaos.shoeorder.util.JdbcUtils.toLocalDateTime;
import static com.dhkstnaos.shoeorder.util.JdbcUtils.toUUID;

@Repository
@Profile({"jdbc", "test"})
public class ProductJdbcRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select * from products", productRowMapper);
    }

    @Override
    public Product insert(Product product) {
        var update = jdbcTemplate.update("INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)" +
                " VALUES (UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)", toParamMap(product));
        if (update != 1) {
            throw new RuntimeException("Noting was inserted");
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        int update = jdbcTemplate.update(
                "UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, created_at = :createdAt, updated_at = :updatedAt" +
                        " WHERE product_id = UUID_TO_BIN(:productId)",
                toParamMap(product));
        if (update != 1) {
            throw new RuntimeException("Noting was updated");
        }
        return product;
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                            Collections.singletonMap("productId", productId.toString().getBytes()), productRowMapper)
            );
        } catch (EmptyStackException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_name = :productName",
                            Collections.singletonMap("productName", productName), productRowMapper)
            );
        } catch (EmptyStackException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query("SELECT * FROM products WHERE category = :category",
                Collections.singletonMap("category", category.toString()), productRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", Collections.emptyMap());
    }

    @Override
    public void deleteById(UUID productId) {
        jdbcTemplate.update("DELETE FROM products where product_id = UUID_TO_BIN(:productId)",
                Collections.singletonMap("productId", productId.toString().getBytes()));
    }

    private static final RowMapper<Product> productRowMapper = (result, i) -> {
        UUID productId = toUUID(result.getBytes("product_id"));
        String productName = result.getString("product_name");
        Category category = Category.valueOf(result.getString("category"));
        long price = result.getLong("price");
        String description = result.getString("description");
        LocalDateTime createdAt = toLocalDateTime(result.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(result.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Product product) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());

        return paramMap;
    }
}
