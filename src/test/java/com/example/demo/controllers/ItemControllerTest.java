package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_findByItemName() {
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
        when(itemRepo.findByName(expectedItemName)).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(expectedItemName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> actualItems = response.getBody();
        assertNotNull(actualItems);
        Item actualItem = actualItems.get(0);
        assertEquals(expectedItemId, actualItem.getId());
        assertEquals(expectedItemName, actualItem.getName());
        assertEquals(expectedItemPrice, actualItem.getPrice());
        assertTrue(expectedItemDesc.equals(actualItem.getDescription()));
    }

    @Test
    public void verify_failed_findByItemName() {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        String expectedItemName = "laptop";
        when(itemRepo.findByName(expectedItemName)).thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> response = itemController.getItemsByName(expectedItemName);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}