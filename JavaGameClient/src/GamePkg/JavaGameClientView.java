package GamePkg;

// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class JavaGameClientView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	public static Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public static ObjectInputStream ois;
	public static ObjectOutputStream oos;

	private JPanel contentPane;
	private JTextField txtInput;
	private static String user_name;
	private JButton btnSend;
	private JLabel lblUserName;
	// private JTextArea textArea;
	private static JTextPane textArea = new JTextPane();
	private static JPanel topPane = new JPanel(); 
	
	private Frame frame;
	private FileDialog fd;

	private JavaGameClientRoom room;
	public static JLabel lblCharacter = new JLabel("");
	private String character = "src/images/Character.gif"; // 기본 캐릭터 지정

	public static JLabel[] lblCoinArr = new JLabel[2];
	private String coin = "src/images/Coin.gif"; // 코인 이미지
	public static JLabel lblCoin = new JLabel("10");
	
	private String[] addCoins = {"10", "5", "2", "0"};
	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public JavaGameClientView(String username, String ip_addr, String port_no) {

		user_name = username; // 문제 : 다른 클라이언트가 view 에 들어올시 변경됨
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 540);

		// 배경화면 지정
		ImageIcon bg = new ImageIcon("src/images/ViewBG.gif");

		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(bg.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		topPane.setBackground(new Color(255, 182, 193));
		topPane.setBounds(0, 0, 700, 55);
		
		
//		lblCoinArr[0]= new JLabel("");
//		lblCoinArr[0].setBackground(Color.WHITE);
//		ImageIcon coinIcon = new ImageIcon(coin);
//		lblCoinArr[0].setIcon(coinIcon);
//		lblCoinArr[0].setHorizontalAlignment(SwingConstants.CENTER);
//		lblCoinArr[0].setBounds(12, 100, 133, 42);
//		
//		lblCoinArr[1]= new JLabel("");
//		lblCoinArr[1].setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
//		
//		lblCoinArr[1].setBounds(150, 5, 133, 42);
//		lblCoinArr[1].setText("10개");
//		topPane.add(lblCoinArr[0]);
//		topPane.add(lblCoinArr[1]);
		
		lblCoin.setFont(new Font("나눔스퀘어", Font.PLAIN, 15));
		lblCoin.setHorizontalAlignment(SwingConstants.LEFT);
		lblCoin.setBounds(10, 20, 100, 15);
		contentPane.add(lblCoin);
		contentPane.add(topPane);
		
		// 채팅창
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 345, 375, 109);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));

		// 채팅 입력창
		txtInput = new JTextField();
		txtInput.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		txtInput.setBounds(12, 464, 375, 30);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		// 기본 캐릭터 설정
		ImageIcon basiccharacter = new ImageIcon(character);
		lblCharacter.setBackground(Color.WHITE);
		lblCharacter.setIcon(basiccharacter);
		lblCharacter.setHorizontalAlignment(SwingConstants.CENTER);
		lblCharacter.setBounds(141, 117, 90, 90);
		contentPane.add(lblCharacter);

		// 유저이름
		lblUserName = new JLabel("Name");
		lblUserName.setBounds(172, 217, 152, 36);
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setBackground(new Color(255, 255, 255));
		lblUserName.setFont(new Font("배달의민족 주아", Font.BOLD, 25));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblUserName);
		lblUserName.setText(user_name);

		// 방입장 버튼
		ImageIcon normalIcon = new ImageIcon("src/images/btnRoom.png");
		ImageIcon rolloverIcon = new ImageIcon("src/images/btnRoom2.png");
		ImageIcon pressedIcon = new ImageIcon("src/images/btnRoom2.png");
		JButton btnEntry = new JButton(normalIcon);
		btnEntry.setPressedIcon(pressedIcon); // pressedIcon용 이미지 등록
		btnEntry.setRolloverIcon(rolloverIcon); // rolloverIcon용 이미지 등록
		btnEntry.setBounds(534, 373, 152, 57);
		contentPane.add(btnEntry);

		// AppendText(ip_addr + " " + port_no);
		setVisible(true);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// 프로토콜 : 100 -> 서버 (로그인)
			ChatMsg obcm = new ChatMsg(user_name, "100", "Hello");
			obcm.character = character;
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();

			TextSendAction action = new TextSendAction();
			txtInput.addActionListener(action);
			txtInput.requestFocus();

			RoomEntryButtonClick action_entry = new RoomEntryButtonClick();
			btnEntry.addActionListener(action_entry);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
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
					case "200": // 대기방chatting
						if (cm.username.equals(user_name))
							AppendTextR(msg); // 내 메세지는 우측에
						else
							AppendText(msg);
						break;
					case "250": // 게임방 chatting
						// 여기서 javagameclientroom을 하면 렉이 먹는 것 같음
						// JavaGameClientRoom.AppendText(cm.username +" "+ user_name + " "+ msg);

						if (cm.username.equals(user_name))
							JavaGameClientRoom.AppendTextR(msg);
						else
							JavaGameClientRoom.AppendText(msg);
						break;
					case "400":
						lblCoin.setText(String.valueOf(cm.coin));
						setVisible(true);// view 창 다시 열기
						break;
					case "450":
						for (int i = 0; i < 4; i++) {
							JavaGameClientRoom.lblUserName[i].setText("");
							JavaGameClientRoom.lblUserName[i].setForeground(Color.WHITE);
							JavaGameClientRoom.lblUserCharacter[i].setIcon(null);
							JavaGameClientRoom.lblUserReady[i].setVisible(false);

						}
						break;

					case "500": // 플레이어리스트
						int idx = 0;
						
						for (String name : cm.playerlist.keySet()) {
							JavaGameClientRoom.lblUserName[idx].setText(name);
							ImageIcon character = new ImageIcon(cm.playerlist.get(name).get(0));
							JavaGameClientRoom.lblUserCharacter[idx].setIcon(character);
							if (user_name.equals(name)) {
								JavaGameClientRoom.lblUserName[idx].setForeground(new Color(102, 102, 204));
							}
							if (cm.playerlist.get(name).get(1).equals("Ready")) {
								JavaGameClientRoom.lblUserReady[idx].setVisible(true);
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
									JavaGameClientRoom.lblUserReady[idx2].setVisible(true);
								else
									JavaGameClientRoom.lblUserReady[idx2].setVisible(false);
							}
							idx2++;
						}
						break;
					case "600":
						JavaGameClientRoom.GameStart();
						try {
							Thread.sleep(2000);
							JavaGameClientRoom.lblQuestion.setFont(new Font("배달의민족 도현", Font.PLAIN, 18));
							JavaGameClientRoom.lblQuestion.setBounds(20, 25, 700, 30);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					case "650":
						String ans = "";
						for (Integer type : cm.quiz.keySet()) {
							JavaGameClientRoom.lblQuestion.setText(cm.quiz.get(type).get(0)); // 0번째가 문제
							for (int i = 0; i < 4; i++) {
								if (type.equals(1)) { // 객관식이면 보기가 4개
									JavaGameClientRoom.btn_quizV[i].setText(cm.quiz.get(type).get(i + 1));
									JavaGameClientRoom.btn_quizV[i].setVisible(true);
									if (i < 2)
										JavaGameClientRoom.btn_OX[i].setVisible(false);
								} else if (type.equals(2)) { // ox퀴즈는 보기가 없으므로 o, x버튼 만들기
									JavaGameClientRoom.btn_quizV[i].setVisible(false);
									if (i < 2)
										JavaGameClientRoom.btn_OX[i].setVisible(true);
								}
							}
							if (type.equals(1)) {
								ans = cm.quiz.get(type).get(5); //정답
							} else {
								ans = cm.quiz.get(type).get(1); //정답
							}
						}
						final String ans2 = ans;
						JavaGameClientRoom.timebar.setValue(10);
						JavaGameClientRoom.timebar.setMaximum(10);
						Timer timer = new Timer(true);
						TimerTask m_task = new TimerTask() { // 타이머 표시
							int time = 10;
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								JavaGameClientRoom.timebar.setValue(time);
								JavaGameClientRoom.lblTime.setText(time + "");
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								time--;
								if (time < 0) {
									if(JavaGameClientRoom.ans.equals(ans2)) {
										JavaGameClientRoom.getPlayerSeq(user_name);
										JavaGameClientRoom.ans = "";
										//JavaGameClientRoom.AppendText(Arrays.asList(JavaGameClientRoom.lblUserName).indexOf(user_name)+"");
										//JavaGameClientRoom.AppendText(JavaGameClientRoom);
										//JavaGameClientRoom.lblScore
//										ChatMsg obcm1 = new ChatMsg(user_name, "700", "Score");
//										SendObject(obcm1);
									}

									timer.cancel();
									
								}
							}

						};
						timer.schedule(m_task, 0, 1000);
//						ChatMsg obcm1 = new ChatMsg(user_name, "400", "RoomExit");
//						SendObject(obcm);
						// ChatMsg obcm1 = new ChatMsg(user_name, "700", "Entry"); //선택한 답 서버에 넘겨서 계산
						// SendObject(obcm);

						break;
					case "700":
						int index = Integer.valueOf(cm.data);
						JavaGameClientRoom.lblScore[index].setText(Integer.valueOf(JavaGameClientRoom.lblScore[index].getText())+1+"");
						break;
					case "750":
						if(cm.data.equals("GameOver")) {
							JavaGameClientRoom.GameOver();
							JavaGameClientRoom.lblQuestion.setFont(new Font("나눔스퀘어", Font.PLAIN, 18));
							JavaGameClientRoom.lblQuestion.setBounds(20, 25, 700, 30);
							JavaGameClientRoom.lblQuestion.setText("게임 결과");
							
						} else if(cm.data.equals("Rank")){
							int i = 0;
							for(String ranks : cm.rank.keySet()) {
								
								//JavaGameClientRoom.AppendText(ranks+"등 "+ cm.rank.get(ranks).get(0) +"  +"+cm.rank.get(ranks).get(1));
								JavaGameClientRoom.rank[i].setText(ranks+"등 "+ cm.rank.get(ranks).get(0) +"  +"+cm.rank.get(ranks).get(1));
//								if(ranks.equals(cm.username)) {
//									lblCoin.setText(String.valueOf(Integer.valueOf(lblCoin.getText())+cm.rank.get(ranks).get(1))); 
//								}
								i++;
							}
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//게임 결과 뜬 후 10초 후에 로비방으로 or 게임 레디부터
							JavaGameClientRoom.Restart();
							
//							Timer t = new Timer(true);
//							TimerTask task = new TimerTask() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									
//								}
//								
//							};
						}
						
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}

	class RoomEntryButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 문제점 : 대기방에 여러창 띄워놓은 다음 게임방에 들어갈때 오류남
			if (room == null)
				room = new JavaGameClientRoom(user_name, character);
			else {
				room.setVisible(true);
			}
			setVisible(false);
			ChatMsg obcm = new ChatMsg(user_name, "500", "Entry");
			obcm.character = character;
			obcm.coin = Integer.valueOf(lblCoin.getText());
			SendObject(obcm);

		}
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == txtInput) {
				String msg = null;

				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public static void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		// textArea.setCaretPosition(len);
		// textArea.replaceSelection(msg + "\n");

		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", left);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 화면 우측에 출력
	public void AppendTextR(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);
		doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", right);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public static void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(user_name, "200", msg);
			// else if(page.equals("room")) obcm = new ChatMsg(user, "250", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public static void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}
