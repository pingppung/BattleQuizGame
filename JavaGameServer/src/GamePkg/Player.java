package GamePkg;

import java.net.ServerSocket;

public class Player{
	private ServerSocket socket;
	private Room room;
	private String name;
	private String character;
	private PlayerStatus.Status playerStatus;
	
	public Player(String name, String character){
		this.name = name;
		this.character = character;
	}
	
	public void enterRoom(Room room) {
		//room.enterPlayer(this); // 룸에 입장시킨 후
		this.room = room; // 유저가 속한 방을 룸으로 변경한다. 
	}
	public void exitRoom(Room room){
        this.playerStatus = PlayerStatus.Status.View;
        this.room = null;
    }
	
    public PlayerStatus.Status getPlayerStatus() {
        return playerStatus;
    }
	public void setPlayerStatus(PlayerStatus.Status status) { // 유저의 상태를 설정
		this.playerStatus = status;
	}
	//플레이어 비교
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return name == player.name;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public ServerSocket getSocket() {
        return socket;
    }
    public void setSocket(ServerSocket socket) {
        this.socket = socket;
    }
    
}
