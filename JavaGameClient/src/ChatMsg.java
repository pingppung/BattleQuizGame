
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
	public String UserName;
	public String UserToon;
	public String data;
	public ImageIcon img;
	public String[] PlayerN = new String[4];
	public String[] Character = new String[4];
	public int RoomEntryPlayerNum;
	public String UserReady;
	public String[] PlayerReady = new String[4];
	public int i;

	public int ReadyNum;
	public int QuizPanel;
	public String type;
	public String Quiz;
	public String[] choice = new String[4];
	public String[] RoomName = new String[4];
	public int answ;
	// 플레이어가 누른 답 저장
	public int Answer;
	public int RealAnswer;
	public int[] Score = new int[4];
	public String[] EndName = new String[4];
	public int[] EndScore = new int[4];
	
	public String[] LoginPlayer = new String[4];
	public int LoginPlayerNum = 0;
	public int[] PlayerCoin = new int[4];
	public String ToonBtn;
	public int ToonPrice;
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
}