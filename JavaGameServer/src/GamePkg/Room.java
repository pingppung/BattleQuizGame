package GamePkg;

import java.util.*;

public class Room {
	private int id; // 룸 ID
	private List playerList;
	private String roomName; // 방 이름
	
	public Room(int roomId) { // 유저가 방을 만들때
		this.id = roomId;
		playerList = new ArrayList();
	}
	public Room(int roomId, Player player) { // 유저가 방을 만들때
		playerList = new ArrayList();
		this.id = roomId;
		//player.enterRoom(this);
		//playerList.add(player); // 유저를 추가시킨 후
	}

	public void enterPlayer(Player player) {
		player.enterRoom(this);
		playerList.add(player);
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
            if (player.getName().equals(name)) {
                return player; // 유저를 찾았다면
            }
        }
        return null; // 찾는 유저가 없다면
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room gameRoom = (Room) o;

        return id == gameRoom.id;
    }
    public int hashCode() {
        return id;
    }
}
