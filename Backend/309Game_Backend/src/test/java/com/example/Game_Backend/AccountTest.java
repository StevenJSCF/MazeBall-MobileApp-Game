package com.example.Game_Backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountController;
import com.example.Game_Backend.Accounts.AccountRepository;
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
public class AccountTest {
    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private PlayerRepository mockPlayerRepository;
    @InjectMocks
    private AccountController mockAccountController;
    private Account mockAccount = new Account();
    private final Long mockAccountID = 1L;

    @Before
    public void setUp(){
        // Config
        RestAssured.baseURI="http://coms-309-021.class.las.iastate.edu";
        RestAssured.port=8080;
        mockAccount.setId(mockAccountID);
        mockAccount.setUsername("testUser");
        mockAccount.setPassword("123");
        mockAccountRepository.save(mockAccount);

        when(mockAccountRepository.count()).thenReturn(1L);

        // Mock the behavior of playerRepo
        when(mockPlayerRepository.save(any())).thenReturn(new Player());
    }

    @Test
    public void testGetUsernameById(){
        when(mockAccountRepository.findById(mockAccountID)).thenReturn(Optional.of(mockAccount));
        String result = mockAccountController.getUsernameById(mockAccountID);
        assertEquals("{\"message\":\"testUser\",\"status\":\"true\"}", result);
    }
    @Test
    public void testFetchDetailsById(){
        when(mockAccountRepository.findById(mockAccountID)).thenReturn(Optional.of(mockAccount));
        Account result = mockAccountController.fetchDetailsById(mockAccountID);
        assertNotNull(result);
        assertEquals(mockAccountID, result.getId());
        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testGetAccountByUsername(){
        String actualUsername = "testUser";
        when(mockAccountRepository.findByUsername(actualUsername)).thenReturn(mockAccount);
        Account result = mockAccountController.getAccountByUsername(actualUsername);
        assertNotNull(result);
        assertEquals(mockAccountID, result.getId());
        assertEquals(actualUsername, result.getUsername());
    }

    @Test
    public void testCreateAccount1() throws IllegalAccessException {
        FieldUtils.writeField(mockAccountController, "playerRepo", mockPlayerRepository, true);
        Account newAccount = new Account();
        newAccount.setUsername("newUser");
        newAccount.setPassword("456");
        Message result = mockAccountController.createAccount1(newAccount);
        assertNotNull(result);
        assertEquals("Success", result.getMessage());
        assertEquals("true", result.getStatus());
    }

    @Test
    public void testLogin(){
        Account loginRequest = new Account();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("123");
        String result = mockAccountController.login1(loginRequest);
        assertNotNull(result);
        assertEquals("{\"message\":\"-1\",\"status\":\"false\"}", result);
    }
}
