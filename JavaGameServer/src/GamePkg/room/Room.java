package gamepkg.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gamepkg.GameManager;
import gamepkg.network.UserManager;
import gamepkg.network.UserService;
import gamepkg.player.Player;
import gamepkg.util.ChatMsg;

public class Room {
	private int id; // 룸 ID
	private List<Player> playerList;
	private UserManager userManager;
	public GameManager gameManager;
	
	public Room(int roomId, Player player) { // 유저가 방을 만들때
		playerList = new ArrayList();
		this.id = roomId;
		this.gameManager = new GameManager(this);
		// player.enterRoom(this);
		// playerList.add(player); // 유저를 추가시킨 후
	}

	public void addPlayer(Player player) {
		player.enterRoom(this);
		playerList.add(player);
	}

	public boolean removePlayer(Player player) {
		if (playerList.remove(player)) {
			player.exitRoom(this);
			if (playerList.isEmpty()) {
				RoomManager.removeRoom(this);
				return true;
			}
		}
		return false;
	}

	public void closeRoom() {
		for (Player player : new ArrayList<>(playerList)) {
            removePlayer(player);
        }
	}

	public int getPlayerCount() { // 유저의 수를 리턴
		return playerList.size();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

	public void set1PlayerList(List playerList) {
		this.playerList = playerList;
	}

	public Player findPlayerByName(String name) { // 닉네임을 통해서 방에 속한 유저를 리턴함
		for (Player player : playerList) {
			if (player.getName().equals(name)) {
				return player; // 유저를 찾았다면
			}
		}
		return null;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void checkAndStartGame() {
        if (gameManager.allPlayersReady()) {
            gameManager.startGame(); // 모든 플레이어가 레디 상태이면 퀴즈 시작
        }
	}
	public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
	 // 같은 방에 있는 모든 사용자에게 메시지 전송
    public void sendMessageToAll(ChatMsg cm) {
        userManager.writeRoom(this, cm);
    }
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Room room = (Room) o;
		return id == room.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

}
