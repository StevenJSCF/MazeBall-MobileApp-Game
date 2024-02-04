package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;

import com.example.Game_Backend.MapsInfo.MapInfo;
import com.example.Game_Backend.MapsInfo.MapInfoController;
import com.example.Game_Backend.MapsInfo.MapInfoRepository;
import com.example.Game_Backend.module.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MapInfoTest {

    @InjectMocks
    private MapInfoController mapInfoController;

    @Mock
    private MapInfoRepository mapInfoRepo;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testGetAmountOfLevels() {
        // Mocking repository method with a generic count value
        when(mapInfoRepo.count()).thenReturn(5L);

        // Call the controller method
        String response = mapInfoController.ammountOfLevels();

        // Assertions
        assertEquals("{\"levels\":\"5\"}", response);

        // Verify repository method invocations
        verify(mapInfoRepo, times(1)).count();
    }

    @Test
    public void testGetMapContentByUser() {
        // Mocking data
        Long mapNumber = 1L;
        MapInfo mockMapInfo = new MapInfo();
        mockMapInfo.setBody("Mocked Map Body");

        // Mocking repository method
        when(mapInfoRepo.findById(anyLong())).thenReturn(Optional.of(mockMapInfo));

        // Call the controller method
        Message responseMessage = mapInfoController.getMapContentByUser(mapNumber);

        // Assertions
        assertEquals("Mocked Map Body", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository method invocations
        verify(mapInfoRepo, times(1)).findById(mapNumber);
    }

    @Test
    public void testPostMapByPath() {
        // Input values
        String creator = "Nick";
        int map_highestScore = 999999;
        String username = "Josh";

        // Mocking repository save method
        when(mapInfoRepo.save(any(MapInfo.class))).thenReturn(new MapInfo()); // You can adjust this based on your MapInfo class

        // Call the controller method
        Message responseMessage = mapInfoController.PostMapByPath(creator, map_highestScore, username);

        // Assertions
        assertEquals("success", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository save method invocations
        verify(mapInfoRepo, times(1)).save(any(MapInfo.class));

        // Additional assertions if needed
        ArgumentCaptor<MapInfo> mapInfoCaptor = ArgumentCaptor.forClass(MapInfo.class);
        verify(mapInfoRepo).save(mapInfoCaptor.capture());
        MapInfo savedMap = mapInfoCaptor.getValue();
        assertEquals(creator, savedMap.getCreator());
        assertEquals(map_highestScore, savedMap.getMap_highestScore());
        assertEquals(username, savedMap.getHighScoreHolder());
    }

    @Test
    public void testPostMap() {
        // Input values
        Long userId = 1L;
        MapInfo map = new MapInfo();
        map.setBody("Map Body");

        // Mocking account repository method
        Account mockAccount = new Account();
        mockAccount.setId(userId);
        mockAccount.setUsername("MockUser");
        when(accountRepository.findById(userId)).thenReturn(Optional.of(mockAccount));

        // Mocking repository save method
        when(mapInfoRepo.save(any(MapInfo.class))).thenReturn(new MapInfo()); // Adjust based on your MapInfo class

        // Call the controller method
        Message responseMessage = mapInfoController.postMap(userId, map);

        // Assertions
        assertEquals("player map was stored", responseMessage.getMessage());
        assertEquals("true", responseMessage.getStatus());

        // Verify repository and account repository method invocations
        verify(mapInfoRepo, times(1)).save(any(MapInfo.class));
        verify(accountRepository, times(1)).findById(userId);

        // Additional assertions if needed
        ArgumentCaptor<MapInfo> mapInfoCaptor = ArgumentCaptor.forClass(MapInfo.class);
        verify(mapInfoRepo).save(mapInfoCaptor.capture());
        MapInfo savedMap = mapInfoCaptor.getValue();
        assertEquals("MockUser", savedMap.getCreator());
        assertEquals("Map Body", savedMap.getBody());
    }

}