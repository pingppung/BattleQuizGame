package gamepkg.util;

// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChatMsg implements Serializable {

	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
	public String username;
	public String character;
	public int coin;
	public String data;
	public Integer[] costume = new Integer[8];
	public Map<String, List<String>> playerlist = new LinkedHashMap<>();
	public Map<Integer, List<String>> quiz = new LinkedHashMap<>(); // 타입, 문제[0]/보기
	public List<PlayerRank> rank;
	
	public ChatMsg(String username, String code, String msg) {
		this.code = code;
		this.username = username;
		this.data = msg;
	}

}