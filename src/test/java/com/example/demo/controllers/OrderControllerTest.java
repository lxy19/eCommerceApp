package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);
    private final String expectedUserName = "User1";
    private final String expectedPassword = "Pass1";

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void verify_submit_username() {
        User user = getUser();
        Cart cart = getCart(user);
        user.setCart(cart);
        UserOrder order = getOrder(cart);
        when(userRepo.findByUsername(expectedUserName)).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit(expectedUserName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(getItems(), response.getBody().getItems());
    }

    private User getUser() {
        User user = new User();
        user.setUsername(expectedUserName);
        user.setPassowrd(expectedPassword);
        return user;
    }

    private Cart getCart(User user){
        List<Item> items = getItems();
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setId(1L);
        cart.setTotal(new BigDecimal(400));
        return cart;
    }

    private UserOrder getOrder(Cart cart) {
        UserOrder order = new UserOrder();
        order.setUser(cart.getUser());
        order.setItems(cart.getItems());
        order.setTotal(cart.getTotal());
        order.setId(cart.getId());
        return order;
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        String expectedItemName = "laptop";
        Long expectedItemId = 1L;
        BigDecimal expectedItemPrice = new BigDecimal(400);
        String expectedItemDesc = "Dell laptop";
        item.setName(expectedItemName);
        item.setPrice(expectedItemPrice);
        item.setId(expectedItemId);
        item.setDescription(expectedItemDesc);
        items.add(item);
        return items;

    }
}