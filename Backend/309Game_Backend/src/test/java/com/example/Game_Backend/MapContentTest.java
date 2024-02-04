package com.example.Game_Backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.MapContent.MapContent;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.MapContent.MapContentController;
import com.example.Game_Backend.MapContent.MapContentRepository;
import com.example.Game_Backend.Player.Player;
import com.example.Game_Backend.Player.PlayerRepository;
import com.example.Game_Backend.module.Message;
import io.restassured.RestAssured;
import org.apache.commons.lang3.reflect.FieldUtils;
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

import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class MapContentTest {
    @Mock
    private MapContentRepository mockMapContentRepository;
    @InjectMocks
    private MapContentController mockMapContentController;
    private MapContent mockMapContent = new MapContent();
    private final Long mockMapContentID = 1L;
    @Before
    public void setUp(){
        // Config
        RestAssured.baseURI="http://coms-309-021.class.las.iastate.edu";
        RestAssured.port=8080;
        mockMapContent.setMap_ID(mockMapContentID);
        mockMapContent.setMapNumber(1);
        mockMapContent.setBody("1212121212121");
    }

    @Test
    public void testGetMapContentByUser() {
        // Mock the behavior of findByUserId to return the mockMapContent when the user exists
        when(mockMapContentRepository.findByUserId(any())).thenReturn(mockMapContent);

        // Call the method to be tested
        Message result = mockMapContentController.getMapContentByUser1(1L); // Use any suitable user ID

        // Assertions
        assertNotNull(result);
        assertEquals("{\"message\":\"1212121212121\",\"status\":\"true\"}", "{\"message\":"+"\"" +  result.getMessage() +"\"" + "," +"\"status\":\"true\"}");
    }
    @Test
    public void testUpdateColumnFail() {
        // Mock the behavior of findByUserId to return the mockMapContent when the user exists
        when(mockMapContentRepository.findByUserId(any())).thenReturn(mockMapContent);

        // Call the method to be tested
        Message result = mockMapContentController.updateColumn(1L, "body", new MapContent());

        // Assertions
        assertNotNull(result);
        assertEquals(" updateColumn failed", result.getMessage());
    }
//
//    @Test
//    public void testGetAccountByUsername(){
//        String actualUsername = "testUser";
//        when(mockAccountRepository.findByUsername(actualUsername)).thenReturn(mockAccount);
//        Account result = mockAccountController.getAccountByUsername(actualUsername);
//        assertNotNull(result);
//        assertEquals(mockAccountID, result.getId());
//        assertEquals(actualUsername, result.getUsername());
//    }
//
//    @Test
//    public void testCreateAccount1() throws IllegalAccessException {
//        FieldUtils.writeField(mockAccountController, "playerRepo", mockPlayerRepository, true);
//        Account newAccount = new Account();
//        newAccount.setUsername("newUser");
//        newAccount.setPassword("456");
//        Message result = mockAccountController.createAccount1(newAccount);
//        assertNotNull(result);
//        assertEquals("Success", result.getMessage());
//        assertEquals("true", result.getStatus());
//    }
//
//    @Test
//    public void testLogin(){
//        Account loginRequest = new Account();
//        loginRequest.setUsername("testUser");
//        loginRequest.setPassword("123");
//        String result = mockAccountController.login1(loginRequest);
//        assertNotNull(result);
//        assertEquals("{\"message\":\"-1\",\"status\":\"false\"}", result);
//    }
}
