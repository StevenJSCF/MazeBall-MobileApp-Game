package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountController;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.Accounts.AccountService;

import com.example.Game_Backend.MapContent.MapContent;
import com.example.Game_Backend.MapContent.MapContentController;
import com.example.Game_Backend.MapContent.MapContentRepository;
import com.example.Game_Backend.module.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MapContentTest2 {

    @InjectMocks
    private MapContentController mapContentController;

    @Mock
    private MapContentRepository mapContentRepo;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testPostMapContentByBody() {
        // Mocking data
        MapContent newMap = new MapContent();
        Long accountId = 1L;
        int mapNumber = 1;

        // Mocking repository methods
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(new Account()));
        when(mapContentRepo.findByUserId(anyLong())).thenReturn(null);
        when(mapContentRepo.save(any(MapContent.class))).thenReturn(newMap);

        // Call the controller method
        Message responseMessage = mapContentController.PostMapContentByBody(newMap, accountId, mapNumber);

        // Assertions
        assertEquals("success", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository method invocations
        verify(accountRepository, times(1)).findById(accountId);
        verify(mapContentRepo, times(1)).findByUserId(accountId);
        verify(mapContentRepo, times(1)).save(any(MapContent.class));
    }



    @Test
    public void testPostMapContentByBody2() {
        // Mocking data
        MapContent newMap = new MapContent();
        Long accountId = 1L;
        int mapNumber = 2;

        // Mocking repository methods
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(new Account()));
        when(mapContentRepo.findByUserId(anyLong())).thenReturn(new MapContent());

        // Call the controller method
        Message responseMessage = mapContentController.PostMapContentByBody(newMap, accountId, mapNumber);

        // Assertions
        assertEquals("another map is being played", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository method invocations
        verify(accountRepository, times(1)).findById(accountId);
        verify(mapContentRepo, times(1)).findByUserId(accountId);
        verify(mapContentRepo, never()).save(any(MapContent.class));
    }

}
