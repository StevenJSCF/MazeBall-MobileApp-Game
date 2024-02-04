package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;

import com.example.Game_Backend.PlayerMapInfo.Map;
import com.example.Game_Backend.MapsInfo.MapInfoController;
import com.example.Game_Backend.MapsInfo.MapInfoRepository;
import com.example.Game_Backend.PlayerMapInfo.MapController;
import com.example.Game_Backend.PlayerMapInfo.MapRepository;
import com.example.Game_Backend.PlayerMapInfo.PlayerScore;
import com.example.Game_Backend.module.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PlayerMapInfoTest {

    @InjectMocks
    private MapController playerMapController;

    @Mock
    private MapRepository mapsRepository;
    @Mock
    private MapInfoRepository mapInfoRepo;
    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testGetPlayerHighScore() {
        // Mocking data
        int mapNumber = 1;
        Long userId = 1L;
        Map mockMap = new Map();
        mockMap.setPlayer_HighScore(100);

        // Mocking repository method
        when(mapsRepository.findByAccountIdAndMapNumber(userId, mapNumber)).thenReturn(Optional.of(mockMap));

        // Call the controller method
        Message responseMessage = playerMapController.getPlayerHighScore(mapNumber, userId);

        // Assertions
        assertEquals("HighScore 100Score", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository method invocations
        verify(mapsRepository, times(1)).findByAccountIdAndMapNumber(userId, mapNumber);
    }

    @Test
    public void testPostMapByBody() {
        // Mocking data
        int mapNumber = 1;
        Long userId = 1L;

        // Mocking repository method
        when(accountRepository.findById(userId)).thenReturn(Optional.of(new Account()));
        when(mapsRepository.findByAccountIdAndMapNumber(userId, mapNumber)).thenReturn(Optional.empty());
        when(mapsRepository.save(any(Map.class))).thenReturn(new Map());

        // Call the controller method
        Message responseMessage = playerMapController.postMapByBody(mapNumber, userId);

        // Assertions
        assertEquals("Map was saved", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository method invocations
        verify(accountRepository, times(1)).findById(userId);
        verify(mapsRepository, times(1)).findByAccountIdAndMapNumber(userId, mapNumber);
        verify(mapsRepository, times(1)).save(any(Map.class));
    }


    @Test
    public void testUpdatePlayerHighScore() {
        // Mocking data
        int newPlayerHighScore = 150;
        int mapNumber = 1;
        Long userId = 1L;
        Map mockMap = new Map();
        mockMap.setPlayer_HighScore(100);

        // Mocking repository method
        when(mapsRepository.findByAccountIdAndMapNumber(userId, mapNumber)).thenReturn(Optional.of(mockMap));
        when(mapsRepository.save(any(Map.class))).thenReturn(mockMap);

        // Call the controller method
        Message responseMessage = playerMapController.updatePlayerHighScore(newPlayerHighScore, mapNumber, userId);

        // Assertions
        assertEquals("new highscore", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository method invocations
        verify(mapsRepository, times(1)).findByAccountIdAndMapNumber(userId, mapNumber);
        verify(mapsRepository, times(1)).save(any(Map.class));
    }

}