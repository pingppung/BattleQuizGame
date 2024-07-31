// Server Message를 수신해서 화면에 표시
package gamepkg.network;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import javax.swing.Timer;

import gamepkg.state.ClientState;
import gamepkg.state.ClientStateManager;
import gamepkg.util.ChatMsg;
import gamepkg.util.PlayerRank;
import gamepkg.util.TextHandler;
import gamepkg.view.MainView;
import gamepkg.view.RoomView;
import gamepkg.view.ShopView;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class ClientSocketHandler extends Thread {
	private static final int BUF_LEN = 128;
	public static Socket socket; // 연결소켓
	public static ObjectInputStream ois;
	public static ObjectOutputStream oos;
	public ClientStateManager stateManager;
	public TextHandler textHandler;

	private MainView robby;
	private RoomView gameRoom;

	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private String username;

	// 서버에 연결
	public ClientSocketHandler(String ip_addr, String port_no) throws IOException {
		this.socket = new Socket(ip_addr, Integer.parseInt(port_no));
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.oos.flush();
		this.ois = new ObjectInputStream(socket.getInputStream());
		JTextPane initialChatPane = new JTextPane();
		// TextHandler 객체 생성
		this.textHandler = new TextHandler(initialChatPane);

		// ClientStateManager 객체 생성 및 TextHandler 주입
		this.stateManager = new ClientStateManager(textHandler);

	}

	// 각 상태에 맞는 채팅 패널 설정
	public void setGameRobby(MainView robby) {
		this.robby = robby;
		stateManager.setChatPaneForState(ClientState.LOBBY, (JTextPane) robby.chatPane.getViewport().getView());
	}

	public void setGameRoom(RoomView gameRoom) {
		this.gameRoom = gameRoom;
		stateManager.setChatPaneForState(ClientState.GAME_ROOM, (JTextPane) gameRoom.chatPane.getViewport().getView());
	}

	public void handleStateChange(ClientState newState) {
		stateManager.setState(newState);
	}

	public void run() {
		while (true) {
			try {

				Object obcm = null;
				String msg = null;
				ChatMsg cm;
				try {
					obcm = ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					break;
				}
				if (obcm == null)
					break;
				if (obcm instanceof ChatMsg) {
					cm = (ChatMsg) obcm;
					msg = String.format("[%s]\n%s", cm.username, cm.data);
				} else
					continue;
				switch (cm.code) {
				case "100": // 로그인\
					username = cm.username;
					System.out.println("요기");
					break;
				case "200": // 대기방chatting
					if (cm.username.equals(username))
						textHandler.appendTextR(msg); // 내 메세지는 우측에
					else
						textHandler.appendText(msg);
					break;
				case "250": // 게임방 chatting
					if (cm.username.equals(username))
						textHandler.appendTextR(msg);
					else
						textHandler.appendText(msg);
					break;
				case "300":// 상점 입장
					for (int i = 0; i < 8; i++) {
						if (cm.costume[i] != null && cm.costume[i] == 1) { // 보유하고 있는 캐릭터
							ShopView.btnSelect[i].setText("선택");
						}

					}
					break;
				case "350":// 캐릭터 구매 정보
					if (cm.data.equals("FAIL")) { // 구매 실패했을 경우 경고문
						JOptionPane.showMessageDialog(null, "코인 부족", "구매 실패", JOptionPane.PLAIN_MESSAGE);

					} else {
						MainView.lblCoin.setText(String.valueOf(cm.coin));
						for (int i = 0; i < 8; i++) {
							if (cm.costume[i] != null && cm.costume[i] == 1) { // 보유하고 있는 캐릭터
								ShopView.btnSelect[i].setText("선택");
							}
						}
					}
					break;
				case "400": // 게임 exit
					MainView.lblCoin.setText(String.valueOf(cm.coin));
					stateManager.setState(ClientState.LOBBY);
					robby.setVisible(true);// view 창 다시 열기
					break;
				case "450": // player리스트 초기화
					for (int i = 0; i < 4; i++) {
						gameRoom.lblUserName[i].setText("");
						gameRoom.lblUserName[i].setForeground(Color.WHITE);
						gameRoom.lblUserCharacter[i].setIcon(null);
						gameRoom.lblUserReady[i].setVisible(false);

					}
					break;

				case "500": // 플레이어리스트
					int idx = 0;
					for (String name : cm.playerlist.keySet()) {
						gameRoom.lblUserName[idx].setText(name);
						ImageIcon character = new ImageIcon(cm.playerlist.get(name).get(0));
						gameRoom.lblUserCharacter[idx].setIcon(character);
						if (username.equals(name)) {
							gameRoom.lblUserName[idx].setForeground(new Color(102, 102, 204));
						}
						if (cm.playerlist.get(name).get(1).equals("Ready")) {
							gameRoom.lblUserReady[idx].setVisible(true);
						}
						// JavaGameClientRoom.btn_Ready.setText("준비완료");
						idx++;
					}
					break;
				case "550":
					// 해당 유저만 바뀌게 할지 아님
					int idx2 = 0;
					for (String name : cm.playerlist.keySet()) {
						if (cm.username.equals(name)) { // ready 버튼 누른 유저 찾기
							if (cm.playerlist.get(name).get(1).equals("Ready"))
								gameRoom.lblUserReady[idx2].setVisible(true);
							else
								gameRoom.lblUserReady[idx2].setVisible(false);
						}
						idx2++;
					}
					break;
				case "600":
					gameRoom.GameStart();
					try {
						Thread.sleep(2000);
						RoomView.lblQuestion.setFont(new Font("배달의민족 도현", Font.PLAIN, 18));
						RoomView.lblQuestion.setBounds(20, 25, 700, 30);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					break;
				case "650":
					String ans = "";
					for (Integer type : cm.quiz.keySet()) {
						RoomView.lblQuestion.setText(cm.quiz.get(type).get(0)); // 0번째가 문제
						for (int i = 0; i < 4; i++) {
							if (type.equals(1)) { // 객관식이면 보기가 4개
								gameRoom.btn_quizV[i].setText(cm.quiz.get(type).get(i + 1));
								gameRoom.btn_quizV[i].setVisible(true);
								if (i < 2)
									gameRoom.btn_OX[i].setVisible(false);
							} else if (type.equals(2)) { // ox퀴즈는 보기가 없으므로 o, x버튼 만들기
								gameRoom.btn_quizV[i].setVisible(false);
								if (i < 2)
									gameRoom.btn_OX[i].setVisible(true);
							}
						}
						if (type.equals(1)) {
							ans = cm.quiz.get(type).get(5); // 정답
						} else {
							ans = cm.quiz.get(type).get(1); // 정답
						}
					}
					final String ans2 = ans;
					gameRoom.timebar.setValue(10);
					gameRoom.timebar.setMaximum(10);
					Timer timer = new Timer(1000, new ActionListener() {
						int timeLeft = 10;

						@Override
						public void actionPerformed(ActionEvent e) {
							gameRoom.timebar.setValue(timeLeft);
							gameRoom.lblTime.setText(Integer.toString(timeLeft));

							if (timeLeft == 0) {
								((Timer) e.getSource()).stop();
								if (gameRoom.ans.equals(ans2)) {
									gameRoom.getPlayerSeq(username);
									gameRoom.ans = "오엥";
								}
							} else {
								timeLeft--;
							}
						}
					});

					timer.start();

					break;
				case "700":
					int index = Integer.valueOf(cm.data);
					gameRoom.lblScore[index].setText(Integer.valueOf(gameRoom.lblScore[index].getText()) + 1 + "");
					break;
				case "750":
					if (cm.data.equals("GameOver")) {
						gameRoom.GameOver();
						gameRoom.lblQuestion.setFont(new Font("나눔스퀘어", Font.PLAIN, 18));
						gameRoom.lblQuestion.setBounds(20, 25, 700, 30);
						gameRoom.lblQuestion.setText("게임 결과");

					} else if (cm.data.equals("Rank")) {
						int i = 0;
						for (PlayerRank rank : cm.rank) {
							String playerName = rank.getPlayerName();
					        int rewardCoins = rank.getRewardCoins();
					        int rankNum = rank.getRank();
							int diff = 50 - String.valueOf(rewardCoins).length();
							gameRoom.rank[i]
									.setText(rankNum + "등     " + playerName + " ".repeat(diff) + "+" + rewardCoins);
							i++;
						}
						// 5초 후에 작업을 비동기적으로 실행
				        Timer exitGameTimer = new Timer(5000, new ActionListener() {
				            @Override
				            public void actionPerformed(ActionEvent e) {
				                // 게임 결과 뜬 후 10초 후에 로비방으로 or 게임 레디부터
				                ChatMsg obcm1 = new ChatMsg(username, "400", "GameFinishExit");
				                gameRoom.dispose();
				                SendObject(obcm1);
				            }
				        });
				        exitGameTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
				        exitGameTimer.start();
					}
					break;
				}
			} catch (IOException e) {
				// TextHandler.appendText("ois.readObject() error");
				try {
					ois.close();
					oos.close();
					socket.close();

					break;
				} catch (Exception ee) {
					break;
				}
			}

		}
	}

	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		for (int i = 0; i < BUF_LEN; i++) {
			packet[i] = 0;
		}
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		for (int i = 0; i < bb.length; i++) {
			packet[i] = bb[i];
		}
		return packet;
	}

	public void SendMessage(String username, String code, String msg) {// 대기룸 채팅 = 200 / 게임방 채팅 = 250
		try {
			ChatMsg obcm = new ChatMsg(username, code, msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// TextHandler.appendText("oos.writeObject() error");
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) {
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// TextHandler.appendText("SendObject Error");
		}
	}
}