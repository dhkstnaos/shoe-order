package com.dhkstnaos.shoeorder.order.repository;

import com.dhkstnaos.shoeorder.order.domain.Email;
import com.dhkstnaos.shoeorder.order.domain.Order;
import com.dhkstnaos.shoeorder.order.domain.OrderItem;
import com.dhkstnaos.shoeorder.order.domain.OrderStatus;
import com.dhkstnaos.shoeorder.product.domain.Category;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.dhkstnaos.shoeorder.util.JdbcUtils.toLocalDateTime;
import static com.dhkstnaos.shoeorder.util.JdbcUtils.toUUID;

@Repository
@Profile({"jdbc", "test"})
public class OrderJdbcRepository implements OrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query("select * from orders", orderRowMapper);
        for (Order order : orders) {
            List<OrderItem> orderItems = jdbcTemplate.query("SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
                    Collections.singletonMap("orderId", order.getOrderId().toString().getBytes()), orderItemRowMapper);
            order.setOrderItems(orderItems);
        }
        return orders;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            List<OrderItem> orderItems = jdbcTemplate.query("SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
                    Collections.singletonMap("orderId", orderId.toString().getBytes()), orderItemRowMapper);
            Order order = jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                    Collections.singletonMap("orderId", orderId.toString().getBytes()), orderRowMapper);
            order.setOrderItems(orderItems);
            return Optional.ofNullable(
                    order
            );
        } catch (EmptyStackException e) {
            return Optional.empty();
        }
    }

    @Override
    public Order update(Order order) {
        var update = jdbcTemplate.update(
                "UPDATE orders SET order_status = :orderStatus" +
                        " WHERE order_id = UUID_TO_BIN(:orderId)",
                toOrderParamMap(order)
        );
        if (update != 1) {
            throw new RuntimeException("Nothing was updated");
        }
        return order;
    }

    @Override
    public void deleteById(UUID orderId) {
        jdbcTemplate.update("DELETE FROM order_items where order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", orderId.toString().getBytes()));
        jdbcTemplate.update("DELETE FROM orders where order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", orderId.toString().getBytes()));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM order_items", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");
        return new OrderItem(productId, category, price, quantity);
    };

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("category", item.category().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }
}
