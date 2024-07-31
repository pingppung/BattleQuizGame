package gamepkg.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gamepkg.GameManager;
import gamepkg.ServerWindow;
import gamepkg.player.Player;
import gamepkg.player.PlayerStatus;
import gamepkg.room.Room;
import gamepkg.room.RoomManager;
import gamepkg.util.ChatMsg;

//클라이언트와의 통신을 담당
public class UserService extends Thread{
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private UserManager userManager;
    private ServerWindow serverUI;
    private Player player;

    public UserService(Socket clientSocket, UserManager userManager, ServerWindow serverUI) throws IOException {
        this.clientSocket = clientSocket;
        this.userManager = userManager;
        this.serverUI = serverUI;
        this.ois = new ObjectInputStream(clientSocket.getInputStream());
        this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
        this.oos.flush();
    }

    @Override
    public void run() {
        while (true) {	// 사용자 접속을 계속해서 받기 위해 while문
            try {
                Object obj = ois.readObject();
                if (obj instanceof ChatMsg) {
                    ChatMsg cm = (ChatMsg) obj;
                    handleMessage(cm);
                }
            } catch (IOException | ClassNotFoundException e) {
                handleClientDisconnection();
                break;
            }
        }
    }
    public void handleMessage(ChatMsg cm) {
        switch (cm.code) {
            case "100":
                handleLogin(cm);
                break;
            case "200":
                handleLobbyMessage(cm);
                break;
            case "250":
                handleGameRoomMessage(cm);
                break;
            case "300":
                handleCharacterShopEntry(cm);
                break;
            case "350":
                handleCharacterPurchaseOrChange(cm);
                break;
            case "400":
                handleExitOrGameFinish(cm);
                break;
            case "500":
                handleGameRoomEntry(cm);
                break;
            case "550":
                handleReadyStatusChange(cm);
                break;
            case "700":
                handleQuizScoreRefresh(cm);
                break;
            case "750":
                handleQuizScoreCalculation(cm);
                break;
            default:
                handleDefault(cm);
                break;
        }
    }
    
    private void handleLogin(ChatMsg cm) {
        this.player = new Player(cm.username, cm.character);
        userManager.writeOne(cm);
        
    }
    private void handleLobbyMessage(ChatMsg cm) {
        String msg = String.format("[%s] %s", cm.username, cm.data);
        serverUI.appendText(msg);
        userManager.writeRobby(cm);
    }

    private void handleGameRoomMessage(ChatMsg cm) {
        int id = player.getRoom().getId();
        userManager.writeRoom(player.getRoom(), cm);
    }

    private void handleCharacterShopEntry(ChatMsg cm) {
        serverUI.appendText(cm.username + "님 코인샵 입장");
        cm.coin = player.getCoin();
        cm.costume = player.getCoustume().clone();
        userManager.writeOne(cm);
    }

    private void handleCharacterPurchaseOrChange(ChatMsg cm) {
        if (isNumber(cm.data)) {
            if (player.getCoin() >= 10) {
                player.purchaseCoustume(Integer.parseInt(cm.data));
                player.setCoin(player.getCoin() - 10);
                cm.data = "SUCCESS";
            } else {
                cm.data = "FAIL";
            }
        } else {
            player.setCharacter(cm.data);
            cm.data = "CHANGE";
        }
        cm.coin = player.getCoin();
        cm.costume = player.getCoustume().clone();
        userManager.writeOne(cm);
    }
    private void handleExitOrGameFinish(ChatMsg cm) {
        Room room = player.getRoom();
        ChatMsg obcm = null;
        if (room != null) {
            room.removePlayer(player);
            if (player.getRoom() == null || cm.data.equals("GameFinishExit")) { //게임이 끝나서 보상까지
                cm.coin = player.getCoin();
                userManager.writeOne(cm);
                return;
            }
            obcm = new ChatMsg("SERVER", "450", "changePlayer");
            userManager.writeRoom(player.getRoom(),obcm);

            obcm = new ChatMsg("SERVER", "500", "changePlayer");
            List<Player> playerlist = room.getPlayerList();
            for (Player p : playerlist) {
            	obcm.playerlist.put(p.getName(), Arrays.asList(p.getCharacter(), p.getStatus().toString()));
            }
            userManager.writeRoom(player.getRoom(),obcm);
        }
    }

    private void handleGameRoomEntry(ChatMsg cm) {
        player.setStatus(PlayerStatus.Status.StandBy);
        int roomCnt = RoomManager.roomCount(); // roomlist 마지막 방에서 플레이어 4명일 경우 새로 방 만들기
        Room room = null; 
        if (roomCnt == 0) {	// 게임방이 아무것도 없을때 
            room = RoomManager.createRoom(player);
        } else {
        	boolean newRoom = false;	 // 새로운 방을 만들어야하는지
            for (int i = 0; i < roomCnt; i++) {
                room = RoomManager.getRoom(i);
                if (room.getPlayerCount() <= 3) { // 3명 이하일때만 들어가기
                    room.addPlayer(player);
                    newRoom = false;
                    break;
                } else {
                    newRoom = true;
                }
            }
            if (newRoom) {	 //  방이 있는데 4명으로 정원초과일때
                room = RoomManager.createRoom(player);
            }
        }
        player.enterRoom(room); //  방 정보 저장
        List<Player> playerlist = room.getPlayerList();// 본인이 속한 방의 유저리스트들을 찾아서 클라이언트에게 넘겨주기
        ChatMsg obcm = new ChatMsg("SERVER", "500", "changePlayer");
        for (Player p : playerlist) {
            obcm.playerlist.put(p.getName(), Arrays.asList(p.getCharacter(), p.getStatus().toString()));
        }
        userManager.writeRoom(player.getRoom(),obcm);
        
        room.setUserManager(userManager);
    }

    private void handleReadyStatusChange(ChatMsg cm) {
    	// 여기서 해당 유저에 대한 상태를 레디로 바꾸고 방안에 있는 사람들한테 유저 상태 변경 전달
		ChatMsg obcm = new ChatMsg(cm.username, "550", "playerReadyList");
		Room room = player.getRoom();
		if (room != null) {
			Player player = room.findPlayerByName(cm.username);
			if (cm.data.equals("준비완료")) {
				player.setStatus(PlayerStatus.Status.Ready);
			} else {
				player.setStatus(PlayerStatus.Status.StandBy);
			}
		}

		// 방 안의 모든 플레이어에게 상태 변경 알림
        List<Player> playerlist = room.getPlayerList();
        for (Player p : playerlist) { 
            obcm.playerlist.put(p.getName(), Arrays.asList(p.getCharacter(), p.getStatus().toString()));
        }
        //상태 변경 전달
        userManager.writeRoom(player.getRoom(),obcm);
        
     // 방의 모든 플레이어가 레디 상태인지 확인하고, 레디 상태이면 게임 시작
        room.checkAndStartGame();
	
    }

    private void handleQuizScoreRefresh(ChatMsg cm) { // 퀴즈마다 스코어 갱신
		userManager.writeRoomOthers(player.getRoom(),cm); // 본인을 제외한 플레이어에게 해당 플레이어가 문제를 맞춰서 이 메서드가 실행된다(그냥 화면 전환용)
    }

    private void handleQuizScoreCalculation(ChatMsg cm) { //퀴즈 다 끝난 후의 최종 계산
		int score = Integer.valueOf(cm.data);
		Room room = player.getRoom();
		GameManager gameManager = room.getGameManager();
		//cm.data = "Rank";
		gameManager.handleQuizScoreCalculation(cm.username, score);
		
		//이제 퀴즈 보내는 거랑 점수 계산후 보내는 부분 writeRoom 을 어떻게 해야될지 userService에서 하는게 맞는 것 같긴한데
		//userManager.writeRoom(player.getRoom(),cm);
    }

    private void handleDefault(ChatMsg cm) {
        userManager.writeRoom(player.getRoom(),cm);
    }
    
    public void write(Object obj) {
        try {
            oos.writeObject(obj);
        } catch (IOException e) {
            handleClientDisconnection();
        }
    }
    
    private void handleClientDisconnection() {
        serverUI.appendText(player.getName() + "님의 연결이 끊어졌습니다!! error: " + clientSocket.getInetAddress());
        userManager.removeUser(this);
        closeSocket();
    }
    
    private void closeSocket() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Player getPlayer() {
		return player;
	}
	
	//유저가 방에 입장해 있는지 여부
	public boolean isInRoom() {
	    return player.getRoom() != null;
	}
}
