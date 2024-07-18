package gamepkg.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gamepkg.player.Player;

public class Room {
	private int id; // 룸 ID
	private List playerList;
	private String roomName; // 방 이름
	private Map<String, Integer> rank = new HashMap<>();

	public Room(int roomId) { // 유저가 방을 만들때
		this.id = roomId;
		playerList = new ArrayList();
	}

	public Room(int roomId, Player player) { // 유저가 방을 만들때
		playerList = new ArrayList();
		this.id = roomId;
		// player.enterRoom(this);
		// playerList.add(player); // 유저를 추가시킨 후
	}

	public void enterPlayer(Player player) {
		player.enterRoom(this);
		playerList.add(player);
	}

	public boolean exitPlayer(Player player) {
		player.exitRoom(this);
		playerList.remove(player); // 해당 유저를 방에서 내보냄

		if (playerList.size() < 1) { // 모든 인원이 다 방을 나갔다면
			RoomManager.removeRoom(this); // 이 방을 제거한다.
			return true; // 방을 제거하면 true를 리턴
		}
		return false;
	}

	public void putRank(String player, int score) {
		rank.put(player, score);
	}

	public Map getRank() {
		return rank;
	}

	public void close() {
		if (playerList != null) { // playerList가 null인지 확인
			for (int i = 0; i < playerList.size(); i++) {
				Player player = (Player) playerList.get(i); // Player 객체 가져오기
				player.exitRoom(this);
			}
			playerList.clear(); // 리스트 비우기
			playerList = null; // playerList를 null로 설정
		}
	}

	public int getPlayerSize() { // 유저의 수를 리턴
		return playerList.size();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List playerList) {
		this.playerList = playerList;
	}

	public Player getPlayerByName(String name) { // 닉네임을 통해서 방에 속한 유저를 리턴함
		for (int i = 0; i < playerList.size(); i++) {
			Player player = (Player) playerList.get(i);
			// for (Player player : playerList) {
			if (player.getName().equals(name)) {
				return player; // 유저를 찾았다면
			}
		}
		return null; // 찾는 유저가 없다면
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Room gameRoom = (Room) o;

		return id == gameRoom.id;
	}

	public int hashCode() {
		return id;
	}
}
