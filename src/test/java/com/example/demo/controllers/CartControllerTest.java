package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp()
    {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_addToCart()
    {
        ModifyCartRequest modifyCartReq = getModifiedCartReq();
        User user = getUser();
        Item item = getItem();
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartReq);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(getItem(), response.getBody().getItems().get(0));
    }
    @Test
    public void verify_notFoundItem_whenAddToCart()
    {
        ModifyCartRequest modifyCartReq = getModifiedCartReq();
        User user = getUser();
        Item item = getItem();
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartReq);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void verify_removeFromCart()
    {
        ModifyCartRequest modifyCartReq = getModifiedCartReq();
        User user = getUser();
        Item item = getItem();
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartReq);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().getItems().size());
    }

    @Test
    public void verify_notFoundUsername_whenRemoveFromCart()
    {
        ModifyCartRequest modifyCartReq = getModifiedCartReq();
        Item item = getItem();
        when(userRepo.findByUsername("test")).thenReturn(new User());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartReq);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void verify_notFoundItem_whenRemoveFromCart()
    {
        ModifyCartRequest modifyCartReq = getModifiedCartReq();
        User user = getUser();
        Item item = getItem();
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartReq);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private ModifyCartRequest getModifiedCartReq() {
        ModifyCartRequest modifyCartReq = new ModifyCartRequest();
        modifyCartReq.setItemId(1);
        modifyCartReq.setUsername("test");
        modifyCartReq.setQuantity(1);
        return modifyCartReq;
    }

    private User getUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassowrd("password");
        user.setCart(new Cart());
        return user;
    }
    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("laptop");
        item.setDescription("This is a dell laptop");
        item.setPrice(BigDecimal.valueOf(800));
        return item;
    }
}
