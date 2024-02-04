package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.Chat.PublicChat;
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

import java.io.IOException;

import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PublicChatTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Session session;

    @Spy
    @InjectMocks
    private PublicChat publicChat;

    private Account mockAccount=new Account();

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockAccount.setUsername("testUser");
    }

    @Test
    public void testOnOpen() throws IOException {
        publicChat.onOpen(session,mockAccount.getUsername());
        verify(publicChat).broadcast("User: "+mockAccount.getUsername()+" has joined the chat");
    }

    @Test
    public void testOnClose() throws IOException {
        publicChat.onOpen(session,mockAccount.getUsername());
        publicChat.onClose(session);
        verify(publicChat).broadcast(mockAccount.getUsername()+" disconnected");
    }
}
