package com.example.Game_Backend;

import com.example.Game_Backend.Accounts.Account;
import com.example.Game_Backend.Accounts.AccountRepository;
import com.example.Game_Backend.Items.Item;
import com.example.Game_Backend.Items.ItemRepository;
import com.example.Game_Backend.Player.FriendNotifications;
import com.example.Game_Backend.Player.Player;
import com.example.Game_Backend.Player.PlayerController;
import com.example.Game_Backend.Player.PlayerRepository;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PlayerControllerTests {

    @Mock
    private PlayerRepository mockPlayerRepository;

    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    private PlayerController playerController;

    @InjectMocks
    private FriendNotifications friendNotifications;

    // Main testing Player object
    private Player mockPlayer=new Player();
    private Account mockAccount=new Account();
    private Long playerId=1L;

    @Before
    public void setUp(){
        // Config
        MockitoAnnotations.initMocks(this);
        RestAssured.baseURI="http://coms-309-021.class.las.iastate.edu";
        RestAssured.port=8080;

        // Setting up mockPlayer
        mockPlayer.setId(playerId);
        mockPlayer.setMoney(1000);
        mockPlayer.setFriends(Arrays.asList(2L,3L));
        mockPlayer.setFriendInvites(Arrays.asList(4L,5L));
        mockPlayer.setPendingFriends(Arrays.asList(6L,7L));
        mockPlayer.setInventory(Map.of(1L,0,2L,0,3L,0));
        // mockAccount for mockPlayer
        mockAccount.setId(1L);
        mockAccount.setUsername("User");

        //Creating friend Players and corresponding Accounts for testings
        Player[] players={new Player(),new Player(),new Player(),new Player(),new Player(),new Player()};
        Account[] accounts={new Account(),new Account()};
        players[0].setId(2L);
        accounts[0].setId(2L);
        accounts[0].setUsername("Paddy");
        players[1].setId(3L);
        accounts[1].setId(3L);
        accounts[1].setUsername("notPaddy");
        players[2].setId(4L);
        players[3].setId(5L);
        players[4].setId(6L);
        players[5].setId(7L);
        players[0].setFriends(List.of(1L));
        players[1].setFriends(List.of(1L));
        players[2].setPendingFriends(List.of(1L));
        players[3].setPendingFriends(List.of(1L));
        players[4].setFriendInvites(List.of(1L));
        players[5].setFriendInvites(List.of(1L));

        // Mock Repository setup
        mockPlayerRepository.save(mockPlayer);
        mockPlayerRepository.saveAll(Arrays.asList(players));
        mockAccountRepository.save(mockAccount);
        mockAccountRepository.save(accounts[0]);
        mockAccountRepository.save(accounts[1]);

        when(mockPlayerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
    }

    @Test
    public void testGetAllPlayers(){
        // Arrange
        List<Player> players = Arrays.asList(new Player(), new Player());
        when(mockPlayerRepository.findAll()).thenReturn(players);
        // Act
        List<Player> result = playerController.getAllPlayers();
        // Assert
        assertEquals(players, result);
    }

    @Test
    public void testFetchDetailsById() {
        // Arrange
        //when(mockPlayerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
        // Act
        Player result = playerController.fetchDetailsById(playerId);
        // Assert
        assertEquals(mockPlayer, result);
    }

    @Test
    public void testGetUser() {
        // Arrange
        //when(mockPlayerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
        // Act
        Player result = playerController.getUser(playerId);
        // Assert
        assertEquals(mockPlayer, result);
    }

    @Test
    public void testGetMoney() {
        // Arrange
        Integer mockMoney = 1000;
        // Act
        Integer result = playerController.getMoney(playerId);
        // Assert
        assertEquals(mockMoney, result);
    }

    @Test
    public void testGetInventory(){
        // Arrange
        Map<Long,Integer> mockInventory=Map.of(1L,0,2L,0,3L,0);
        // Act
        Map<Long,Integer> result=playerController.getInventory(playerId);
        // Assert
        assertEquals(mockInventory,result);
    }

    @Test
    public void testGetFriends(){
        // Arrange
        List<Long> mockFriends=Arrays.asList(2L,3L);
        // Act
        List<Long> result=playerController.getFriends(playerId);
        // Assert
        assertEquals(mockFriends,result);
    }

    @Test
    public void testGetFriendNames(){
        Account p2=new Account();
        p2.setId(2L);
        p2.setUsername("Paddy");
        Account p3=new Account();
        p3.setId(3L);
        p3.setUsername("notPaddy");
        when(mockAccountRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(mockAccountRepository.findById(3L)).thenReturn(Optional.of(p3));
        // Arrange
        List<String> mockFriendNames=Arrays.asList("Paddy","notPaddy");
        // Act
        List<String> result=playerController.getFriendNames(playerId);
        // Assert
        assertEquals(mockFriendNames,result);
    }

    @Test
    public void testGetRequests(){
        //Arrange
        List<Long> mockRequests=Arrays.asList(6L,7L);
        //Act
        List<Long> result=playerController.getRequests(playerId);
        //Assert
        assertEquals(mockRequests,result);
    }

    @Test
    public void testGetInvites(){
        //Arrange
        List<Long> mockInvites=Arrays.asList(4L,5L);
        //Act
        List<Long> result=playerController.getInvites(playerId);
        //Assert
        assertEquals(mockInvites,result);
    }

    @Test
    public void testGetName(){
        //Arrange
        String mockName="User";
        //Act
        when(mockAccountRepository.findById(playerId)).thenReturn(Optional.of(mockAccount));
        String result=playerController.getName(playerId);
        //Assert
        assertEquals(mockName,result);
    }

    @Test
    public void testSetMoney() {
        // Send request and receive response
        playerController.setMoney(playerId,900);
        // Check player money for correct value
        int result = mockPlayer.getMoney();
        assertEquals(900,result);
    }

    @Test
    public void testBuy() {
        Integer mockMoney=mockPlayer.getMoney()-10;
        Integer mockItemCount=mockPlayer.getInventory().get(1L)+1;
        playerController.buy(playerId,1L);
        Integer resultMoney=mockPlayer.getMoney();
        assertEquals(mockMoney,resultMoney);
        Integer resultItemCount=mockPlayer.getInventory().get(1L);
        assertEquals(mockItemCount,resultItemCount);
    }

    @Test
    public void testAddFriend(){
        ArrayList<Long> mockFriends = new ArrayList<>(mockPlayer.getFriends());
        mockFriends.add(8L);
        Player p8=new Player();
        p8.setId(8L);
        when(mockPlayerRepository.findById(8L)).thenReturn(Optional.of(p8));
        List<Long> result=playerController.addFriend(playerId,8L);
        assertEquals(mockFriends,result);
    }

    @Test
    public void testReqFriend(){
        Player p8=new Player();
        p8.setId(8L);
        when(mockPlayerRepository.findById(8L)).thenReturn(Optional.of(p8));
        ArrayList<Long> mockReqs = new ArrayList<>(mockPlayer.getPendingFriends());
        mockReqs.add(8L);
        playerController.reqFriend(playerId,8L);
        ArrayList<Long> result=new ArrayList<>(mockPlayer.getPendingFriends());
        assertEquals(mockReqs,result);
    }

    @Test
    public void testAcceptFriend(){
        Player p4=new Player();
        p4.setId(4L);
        p4.setPendingFriends(Collections.singletonList(playerId));
        when(mockPlayerRepository.findById(4L)).thenReturn(Optional.of(p4));
        ArrayList<Long> mockFriends=new ArrayList<>(mockPlayer.getFriends());
        mockFriends.add(4L);
        playerController.acceptFriend(playerId,4L);
        ArrayList<Long> result=new ArrayList<>(mockPlayer.getFriends());
        assertEquals(mockFriends,result);
    }

    @Test
    public void testDeclineFriend(){
        Player p5=new Player();
        p5.setId(5L);
        p5.setPendingFriends(Collections.singletonList(playerId));
        when(mockPlayerRepository.findById(5L)).thenReturn(Optional.of(p5));
        ArrayList<Long> mockInvites=new ArrayList<>(mockPlayer.getFriendInvites());
        mockInvites.remove(5L);
        playerController.declineFriend(playerId,5L);
        ArrayList<Long> result=new ArrayList<>(mockPlayer.getFriendInvites());
        assertEquals(mockInvites,result);
    }

    @Test
    public void testRetractFriend(){
        Player p6=new Player();
        p6.setId(6L);
        p6.setFriendInvites(Collections.singletonList(playerId));
        when(mockPlayerRepository.findById(6L)).thenReturn(Optional.of(p6));
        ArrayList<Long> mockPending=new ArrayList<>(mockPlayer.getPendingFriends());
        mockPending.remove(6L);
        playerController.retractFriend(playerId,6L);
        ArrayList<Long> result=new ArrayList<>(mockPlayer.getPendingFriends());
        assertEquals(mockPending,result);
    }

    @Test
    public void testAddMoney(){
        Integer mockMoney=mockPlayer.getMoney()+20;
        playerController.addMoney(playerId,20);
        Integer result=mockPlayer.getMoney();
        assertEquals(mockMoney,result);
    }

    @Test
    public void testAddItem(){
        Integer mockItemCount=mockPlayer.getInventory().get(1L)+1;
        playerController.addItem(playerId,1L);
        Integer result=mockPlayer.getInventory().get(1L);
        assertEquals(mockItemCount,result);
    }

    @Test
    public void testDelFriend(){
        ArrayList<Long> mockFriends=new ArrayList<>(mockPlayer.getFriends());
        mockFriends.remove(3L);
        ArrayList<Long> result=new ArrayList<>(playerController.delFriend(playerId,3L));
        assertEquals(mockFriends,result);
    }

    @Test
    public void testGetAllNames(){
        List<Account> mockAccounts=Arrays.asList(new Account(),new Account(),new Account());
        mockAccounts.get(0).setId(2L);
        mockAccounts.get(0).setUsername("test1");
        mockAccounts.get(1).setId(3L);
        mockAccounts.get(1).setUsername("test2");
        mockAccounts.get(2).setId(4L);
        mockAccounts.get(2).setUsername("test3");
        List<Player> mockPlayers=Arrays.asList(new Player(),new Player(),new Player());
        mockPlayers.get(0).setId(2L);
        mockPlayers.get(1).setId(3L);
        mockPlayers.get(2).setId(4L);
        when(mockPlayerRepository.findAll()).thenReturn(mockPlayers);
        when(mockAccountRepository.findById(2L)).thenReturn(Optional.of(mockAccounts.get(0)));
        when(mockAccountRepository.findById(3L)).thenReturn(Optional.of(mockAccounts.get(1)));
        when(mockAccountRepository.findById(4L)).thenReturn(Optional.of(mockAccounts.get(2)));
        Map<Long,String> mockNames=new Hashtable<>();
        mockNames.put(2L,"test1");
        mockNames.put(3L,"test2");
        mockNames.put(4L,"test3");
        assertEquals(mockNames,playerController.getAllNames());
    }

    @Test
    public void testImage(){
        mockPlayer.setImageId(1);
        Integer result=playerController.getImage(1L);
        assertEquals(Integer.valueOf(1),result);
        playerController.setImageId(1L,2);
        result=playerController.getImage(1L);
        assertEquals(Integer.valueOf(2),result);
    }

    @Test
    public void testBlock(){
        List<Long> mockBlocks=List.of(2L);
        playerController.blockPlayer(1L,2L);
        assertEquals(mockBlocks,playerController.getBlocked(1L));
        playerController.unblockPlayer(1L,2L);
        assertEquals(List.of(),playerController.getBlocked(1L));
    }

    @Test
    public void testSellItem(){
        Map<Long,Integer> mockInv=mockPlayer.getInventory();
        mockInv.put(1L,mockPlayer.getInventory().get(1L)-1);
        Item thing=new Item();
        thing.setPrice(10);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(thing));
        Integer mockMoney=mockPlayer.getMoney()+10;
        playerController.sellItem(1L,1L);
        assertEquals(mockInv,mockPlayer.getInventory());
        assertEquals(mockMoney,Integer.valueOf(playerController.getMoney(1L)));
    }

    @Test
    public void testUseItem(){
        Map<Long,Integer> mockInv=mockPlayer.getInventory();
        mockInv.put(1L,mockPlayer.getInventory().get(1L)-1);
        Item thing=new Item();
        playerController.useItem(1L,1L);
        assertEquals(mockInv,mockPlayer.getInventory());
    }
}