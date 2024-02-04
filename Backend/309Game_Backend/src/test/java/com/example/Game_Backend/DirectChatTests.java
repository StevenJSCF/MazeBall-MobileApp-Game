/*package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Chat.ChatRepository;
import com.example.Game_Backend.Chat.DirectChat;
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
public class DirectChatTests {

    @Mock
    ChatRepository chatRepository;

    @Mock
    private Session session;

    @Spy
    @InjectMocks
    private DirectChat directChat;

    private Account mockAccount=new Account();
    private Account mockContact=new Account();

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockAccount.setUsername("testUser");
        mockContact.setUsername("testContact");
    }

    @Test
    public void testOnOpen() throws IOException {
        directChat.onOpen(session,mockAccount.getUsername(),mockContact.getUsername());
        verify(directChat).sendMessageToPArticularUser(mockAccount.getUsername(),"Welcome to the chat server, "+mockAccount.getUsername());
    }

    @Test
    public void testOnClose() throws IOException {
        directChat.onOpen(session,mockAccount.getUsername(),mockContact.getUsername());
        verify(directChat).sendMessageToPArticularUser(mockAccount.getUsername(),"Welcome to the chat server, "+mockAccount.getUsername());
        directChat.onClose(session);
        verify(directChat).sendMessageToPArticularUser(mockContact.getUsername(),mockAccount.getUsername()+" disconnected");
    }
}
*/