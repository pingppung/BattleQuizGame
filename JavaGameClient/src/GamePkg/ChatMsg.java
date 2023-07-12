package GamePkg;

// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
	public String UserName;
	public String character;
	public String data;
	public String[] players_name = new String[4];
	public String[] players_character = new String[4];
	//public ArrayList<JavaGameClientRoom.Player> playerList = new ArrayList<>();
	//public Player[] playerList = new Player[3];
	public ChatMsg(String username, String code, String msg) {
		this.code = code;
		this.UserName = username;
		this.data = msg;
	}
}