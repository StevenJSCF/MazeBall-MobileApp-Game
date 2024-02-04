package com.example.Game_Backend;

import com.example.Game_Backend.Items.Item;
import com.example.Game_Backend.Items.ItemController;
import com.example.Game_Backend.Items.ItemRepository;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {

    @Mock
    private ItemRepository mockItemRepository;

    @InjectMocks
    private ItemController mockItemController;

    private Item mockItem=new Item();
    private final Long mockItemID=1L;

    @Before
    public void setUp(){
        // Config
        RestAssured.baseURI="http://coms-309-021.class.las.iastate.edu";
        RestAssured.port=8080;
        mockItem.setId(mockItemID);
        mockItem.setName("Testing Item");
        mockItem.setPrice(10);
        mockItemRepository.save(mockItem);
    }

    @Test
    public void testGetItemPrice(){
        when(mockItemRepository.findById(mockItemID)).thenReturn(Optional.of(mockItem));
        int result=mockItemController.getItemPrice(mockItemID);
        assertEquals(10,result);
    }

    @Test
    public void testGetItem(){
        when(mockItemRepository.findById(mockItemID)).thenReturn(Optional.of(mockItem));
        Item result=mockItemController.getItem(mockItemID);
        assertEquals(mockItem,result);
    }

    @Test
    public void testFetchDetailsByID(){
        when(mockItemRepository.findById(mockItemID)).thenReturn(Optional.of(mockItem));
        Item result=mockItemController.fetchDetailsById(mockItemID);
        assertEquals(mockItem,result);
    }

    @Test
    public void testUpdateName(){
        when(mockItemRepository.findById(mockItemID)).thenReturn(Optional.of(mockItem));
        Item mockNewName=mockItem;
        mockNewName.setName("new name");
        Item result=mockItemController.updateName(mockItemID,"new name");
        assertEquals(mockNewName,result);
    }

    @Test
    public void testUpdatePrice(){
        when(mockItemRepository.findById(mockItemID)).thenReturn(Optional.of(mockItem));
        Item mockNewPrice=mockItem;
        mockNewPrice.setPrice(15);
        Item result=mockItemController.updatePrice(mockItemID,15);
        assertEquals(mockNewPrice,result);
    }

}
