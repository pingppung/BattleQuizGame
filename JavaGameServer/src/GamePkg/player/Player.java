package gamepkg.player;

import java.net.ServerSocket;

import gamepkg.room.Room;

public class Player {
	private ServerSocket socket;
	private Room room;
	private String name;
	private String character;
	private PlayerStatus.Status playerStatus;
	private int coin; // 기본 코인
	private Integer[] costume = new Integer[8];

	public Player(String name, String character) {// , Integer c){
		this.name = name;
		this.character = character;
		costume[0] = 1; // 첫번째 캐릭터는 기본 캐릭터
		this.coin = 10;
		// this.coin = c;
	}

	public void enterRoom(Room room) {
		// room.enterPlayer(this); // 룸에 입장시킨 후
		this.room = room; // 유저가 속한 방을 룸으로 변경한다.
	}

	public void exitRoom(Room room) {
		this.playerStatus = PlayerStatus.Status.Robby;
		this.room = null;
	}

	public void purchaseCoustume(int idx) { // 상점 들어갈시 샀던 캐릭터들 정보 0-없음 1- 있음
		costume[idx] = 1;
	}

	public Integer[] getCoustume() { // 상점 들어갈시 샀던 캐릭터들 정보 0-없음 1- 있음
		return costume;
	}

	public PlayerStatus.Status getPlayerStatus() {
		return playerStatus;
	}

	public void setPlayerStatus(PlayerStatus.Status status) { // 유저의 상태를 설정
		this.playerStatus = status;
	}

	// 플레이어 비교
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
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

	public int getCoin() {
		return coin;
	}

	public void setCoin(int c) {
		this.coin = c;
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
