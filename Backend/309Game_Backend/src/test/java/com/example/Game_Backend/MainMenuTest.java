/*package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.CollaborativeEditing.CollaborativeEditing;
import com.example.Game_Backend.UserActivity.UserActivity;
import jakarta.websocket.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MainMenuTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private Session session;
    @Spy
    @InjectMocks
    private CollaborativeEditing collaborativeEditing;
    private Long userId = 1L;
    private Account mockAccount = new Account();

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockAccount.setUsername("testUser");
        when(accountRepository.findById(userId)).thenReturn(Optional.of(mockAccount));
    }

//    @Test
//    public void testOnOpen(){
//        collaborativeEditing.onOpen(session,userId);
//
//        verify(collaborativeEditing).broadcast("testUser Active");
//        assertEquals("Active",collaborativeEditing.getStatus(userId));
//    }

//    @Test
//    public void testDoNotDisturb(){
//        userActivity.onOpen(session,userId);
//        userActivity.doNotDisturb(userId);
//        verify(userActivity).broadcast("testUser Do Not Disturb");
//        assertEquals("Do Not Disturb",userActivity.getStatus(userId));
//    }
//
//    @Test
//    public void testAway(){
//        userActivity.onOpen(session,userId);
//        userActivity.away(userId);
//        verify(userActivity).broadcast("testUser Away");
//        assertEquals("Away",userActivity.getStatus(userId));
//    }
//
//    @Test
//    public void testActive(){
//        userActivity.onOpen(session,userId);
//        userActivity.active(userId);
//        verify(userActivity,times(2)).broadcast("testUser Active");
//        assertEquals("Active",userActivity.getStatus(userId));
//    }
//
//    @Test
//    public void testOnClose(){
//        userActivity.onOpen(session,userId);
//        userActivity.onClose(session);
//        verify(userActivity).broadcast("testUser Away");
//    }
}
*/