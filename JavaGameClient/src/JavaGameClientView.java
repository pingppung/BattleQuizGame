
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
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
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class JavaGameClientView extends JFrame {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel contentPane_1;
	private JTextField txtInput;
	private String UserName;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	public static ObjectOutputStream oos;

	private JLabel lblUserName;
	// private JTextArea textArea;
	private static JTextPane textArea = new JTextPane();

	private Frame frame;
	private FileDialog fd;
	public static JLabel lblCharacter = new JLabel("");
	private String Character = "src/Character.gif"; // 기본 캐릭터 지정

	private String Player;
	private static String[] PlayerName = new String[4];
	private static String[] PlayerToon = new String[4];
	private static String[] PlayerReady = new String[4];
	private int Num = 0;
	private JButton btnShop;
	private int ReadyNum = 0;
	private ImageIcon Changecharacter1;
	private ImageIcon Changecharacter2;
	private ImageIcon Changecharacter3;
	private ImageIcon Changecharacter4;

	public static JLabel lblCoin1 = new JLabel("");
	public static JLabel lblCoin2 = new JLabel("");
	public static JLabel lblCoin3 = new JLabel("");
	public static JLabel lblCoin4 = new JLabel("");

	//로그인한 플레이어 다 저장시키는 변수
	private String[] LoginPlayer = new String[4];
	private int LoginPlayerNum = 0;
	private int[] PlayerCoin = new int[4];
	
	//타임바
	private int ti = 10;
	
	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public JavaGameClientView(String username, String ip_addr, String port_no) {

		UserName = username;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 540);
		contentPane = new JPanel();

        ImageIcon face1;
        face1 = new ImageIcon("src/Right.gif");
        contentPane_1 = new JPanel() {
          public void paintComponent(Graphics g) {
             Dimension d = getSize();
             g.drawImage(face1.getImage(), 0, 0, d.width, d.height, this);
             setOpaque(false); //그림을 표시하게 설정,투명하게 조절
                 super.paintComponent(g);
          }
       };
        contentPane_1.setBackground(Color.PINK);
		contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane_1);
		contentPane_1.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 345, 375, 109);
		contentPane_1.add(scrollPane);
				scrollPane.setViewportView(textArea);
				textArea.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));

		txtInput = new JTextField();
		txtInput.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		txtInput.setBounds(12, 464, 375, 30);
		contentPane_1.add(txtInput);
		txtInput.setColumns(10);

	    //버튼 기본, 마우스 올렸을 때, 눌렀을때 사진 변화
	    ImageIcon normalIcon = new ImageIcon("src/btnRoom.png");
		ImageIcon rolloverIcon = new ImageIcon("src/btnRoom2.png");
		ImageIcon pressedIcon = new ImageIcon("src/btnRoom2.png");
		JButton btnEntry = new JButton(normalIcon);
		btnEntry.setPressedIcon(pressedIcon); // pressedIcon용 이미지 등록
		btnEntry.setRolloverIcon(rolloverIcon); // rolloverIcon용 이미지 등록
		btnEntry.setBounds(534, 373, 152, 57);
		contentPane_1.add(btnEntry);
		
        ImageIcon face2;
        face2 = new ImageIcon("src/BackgroundLabel.png");
        JPanel panel = new JPanel() {
          public void paintComponent(Graphics g) {
             Dimension d = getSize();
             g.drawImage(face2.getImage(), 0, 0, d.width, d.height, this);
             setOpaque(false); //그림을 표시하게 설정,투명하게 조절
                 super.paintComponent(g);
          }
       };
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.WHITE);
		panel.setBounds(0, 0, 696, 52);
		contentPane_1.add(panel);
		panel.setLayout(null);

		btnShop = new JButton("Shop");
		btnShop.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnShop.setBackground(Color.WHITE);
		btnShop.setBounds(508, 8, 105, 37);
		panel.add(btnShop);

		JButton btnNewButton = new JButton("->");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBounds(625, 8, 49, 37);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		lblCoin1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));

		lblCoin1.setBounds(12, 5, 133, 42);
		panel.add(lblCoin1);
		lblCoin2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));

		lblCoin2.setBounds(12, 5, 133, 42);
		panel.add(lblCoin2);
		lblCoin3.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));

		lblCoin3.setBounds(12, 5, 133, 42);
		panel.add(lblCoin3);
		lblCoin4.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));

		lblCoin4.setBounds(12, 5, 133, 42);
		panel.add(lblCoin4);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		
		ImageIcon basiccharacter = new ImageIcon(Character);
		lblCharacter.setBackground(Color.WHITE);
		lblCharacter.setIcon(basiccharacter);
		lblCharacter.setHorizontalAlignment(SwingConstants.CENTER);
		lblCharacter.setBounds(141, 117, 90, 90);
		contentPane_1.add(lblCharacter);

		lblUserName = new JLabel("Name");
		lblUserName.setBounds(172, 217, 152, 36);
		contentPane_1.add(lblUserName);
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		lblUserName.setBackground(new Color(255, 255, 255));
		lblUserName.setFont(new Font("배달의민족 주아", Font.BOLD, 25));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setText(username);
		
        ImageIcon face3;
        face3 = new ImageIcon("src/Cloud.png");
		
		/*
        ImageIcon face4;
        face4 = new ImageIcon("src/Main.gif");
        JPanel panel_1  = new JPanel() {
          public void paintComponent(Graphics g) {
             Dimension d = getSize();
             g.drawImage(face4.getImage(), 0, 0, d.width, d.height, this);
             setOpaque(false); //그림을 표시하게 설정,투명하게 조절
                 super.paintComponent(g);
          }
       };
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(12, 62, 375, 270);
		contentPane_1.add(panel_1);
		*/
		
		setVisible(true);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//         is = socket.getInputStream();
//         dis = new DataInputStream(is);
//         os = socket.getOutputStream();
//         dos = new DataOutputStream(os);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();

			TextSendAction action = new TextSendAction();
			txtInput.addActionListener(action);
			txtInput.requestFocus();

			RoomEntryButtonClick action3 = new RoomEntryButtonClick();
			btnEntry.addActionListener(action3);

			Cartaction cartaction = new Cartaction();
			btnShop.addActionListener(cartaction);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	// 카트 버튼 눌렀을 경우
	class Cartaction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JavaGameClientShop cart = new JavaGameClientShop(UserName);
			// setVisible(false);
		}
	}

	// 방 입장 버튼 눌렀을 경우
	// 기능 - 게임방 화면 띄우기 && 4명만 입장 가능
	class RoomEntryButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			ChatMsg obcm = new ChatMsg(UserName, "500", "Entry");
			obcm.UserToon = Character;
			SendObject(obcm);
			// 게임방 화면 띄우기
			JavaGameClientRoom room = new JavaGameClientRoom(UserName, Character);
			setVisible(false);
			if (UserName.equals(PlayerName[0])) {
				JavaGameClientRoom.lblUserName1.setForeground(new Color(102, 102, 204));
				JavaGameClientRoom.RlblCoin1.setVisible(true);
			} else if (UserName.equals(PlayerName[1])) {
				JavaGameClientRoom.lblUserName2.setForeground(new Color(102, 102, 204));
				JavaGameClientRoom.RlblCoin2.setVisible(true);
			} else if (UserName.equals(PlayerName[2])) {
				JavaGameClientRoom.lblUserName3.setForeground(new Color(102, 102, 204));
				JavaGameClientRoom.RlblCoin3.setVisible(true);
			} else if (UserName.equals(PlayerName[3])) {
				JavaGameClientRoom.lblUserName4.setForeground(new Color(102, 102, 204));
				JavaGameClientRoom.RlblCoin4.setVisible(true);
			}
		}
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
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
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
					} else
						continue;
					switch (cm.code) {
					
					case "150":
						LoginPlayer[0] = cm.LoginPlayer[0];
						LoginPlayer[1] = cm.LoginPlayer[1];
						LoginPlayer[2] = cm.LoginPlayer[2];
						LoginPlayer[3] = cm.LoginPlayer[3];
						PlayerCoin[0] = cm.PlayerCoin[0];
						PlayerCoin[1] = cm.PlayerCoin[1];
						PlayerCoin[2] = cm.PlayerCoin[2];
						PlayerCoin[3] = cm.PlayerCoin[3];
						if(UserName.equals(LoginPlayer[0]))
							lblCoin1.setText("코인 개수 : " + PlayerCoin[0]);
						else if(UserName.equals(LoginPlayer[1]))
							lblCoin2.setText("코인 개수 : " + PlayerCoin[1]);
						else if(UserName.equals(LoginPlayer[2]))
							lblCoin3.setText("코인 개수 : " + PlayerCoin[2]);
						else if(UserName.equals(LoginPlayer[3]))
							lblCoin4.setText("코인 개수 : " + PlayerCoin[3]);
						
					case "200": // chat message
						if (cm.UserName.equals(UserName))
							AppendTextR(msg); // 내 메세지는 우측에
						else
							AppendText(msg);
						break;
					case "250":
						if (cm.UserName.equals(UserName))
							JavaGameClientRoom.AppendTextR(msg);
						else
							JavaGameClientRoom.AppendText(msg);
						break;
					case "300": // Image 첨부
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						break;
					case "500":
						PlayerName = cm.PlayerN;
						PlayerToon = cm.Character; // 플레이어 캐릭터 정보 받아들이기
						JavaGameClientRoom.lblUserName1.setText(PlayerName[0]);
						JavaGameClientRoom.lblUserName2.setText(PlayerName[1]);
						JavaGameClientRoom.lblUserName3.setText(PlayerName[2]);
						JavaGameClientRoom.lblUserName4.setText(PlayerName[3]);

						// 자기 이름 핑크색으로 표시
						// 여기서 문제점!!! 첫번쨰로 들어간 사람은 두번쨰로 플레이어가 들어올때까지 안변함....
						// 뒤에서부턴 괜찮음

						Changecharacter1 = new ImageIcon(PlayerToon[0]);
						Changecharacter2 = new ImageIcon(PlayerToon[1]);
						Changecharacter3 = new ImageIcon(PlayerToon[2]);
						Changecharacter4 = new ImageIcon(PlayerToon[3]);
						// 캐릭터는 주소만 넘기고 seticon하는 부분은 room에서 할게
						JavaGameClientRoom.lblToon1.setIcon(Changecharacter1);
						JavaGameClientRoom.lblToon2.setIcon(Changecharacter2);
						JavaGameClientRoom.lblToon3.setIcon(Changecharacter3);
						JavaGameClientRoom.lblToon4.setIcon(Changecharacter4);
						
						break;
					case "550":
						if (cm.UserName.equals(UserName)) {
							Character = cm.UserToon; // String으로 받아들이기
							ImageIcon character = new ImageIcon(Character); // icon으로 만들기
							lblCharacter.setIcon(character); // 라벨에 icon 집어넣기
						}
						break;
					case "600":
						if (cm.i == 0) { // 첫번쨰로 들어온 플레이어의 Ready값
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady.setForeground(Color.yellow);
								PlayerReady[0] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady.setForeground(Color.WHITE);
								PlayerReady[0] = "Wait";
							}
						} else if (cm.i == 1) {// 두번쨰로 들어온 플레이어의 Ready값
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady2.setForeground(Color.yellow);
								PlayerReady[1] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady2.setForeground(Color.WHITE);
								PlayerReady[1] = "Wait";
							}
						} else if (cm.i == 2) {// 세번쨰로 들어온 플레이어의 Ready값
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady3.setForeground(Color.yellow);
								PlayerReady[2] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady3.setForeground(Color.WHITE);
								PlayerReady[2] = "Wait";
							}
						} else if (cm.i == 3) {// 네번쨰로 들어온 플레이어의 Ready값
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady4.setForeground(Color.yellow);
								PlayerReady[3] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady4.setForeground(Color.WHITE);
								PlayerReady[3] = "Wait";
							}
						}
						break;
					case "700":
						
						JavaGameClientRoom.lblReady.setVisible(false);
						JavaGameClientRoom.lblReady2.setVisible(false);
						JavaGameClientRoom.lblReady3.setVisible(false);
						JavaGameClientRoom.lblReady4.setVisible(false);
						JavaGameClientRoom.btnReady.setVisible(false);
						JavaGameClientRoom.lblScore.setVisible(true);
						JavaGameClientRoom.lblScore2.setVisible(true);
						JavaGameClientRoom.lblScore3.setVisible(true);
						JavaGameClientRoom.lblScore4.setVisible(true);
						//for (int j = 10; j >= 1; j--) {
						//	JavaGameClientRoom.timebar.setValue(j);
						//}
						
						if (cm.QuizPanel == 1) {

							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.Multi_Panel.setVisible(true);
									JavaGameClientRoom.lblQuestion1.setVisible(true);
									JavaGameClientRoom.btnAnswer1.setVisible(true);
									JavaGameClientRoom.btnAnswer2.setVisible(true);
									JavaGameClientRoom.btnAnswer3.setVisible(true);
									JavaGameClientRoom.btnAnswer4.setVisible(true);

									JavaGameClientRoom.lblQuestion1.setText(cm.Quiz);
									JavaGameClientRoom.btnAnswer1.setText(cm.choice[0]);
									JavaGameClientRoom.btnAnswer2.setText(cm.choice[1]);
									JavaGameClientRoom.btnAnswer3.setText(cm.choice[2]);
									JavaGameClientRoom.btnAnswer4.setText(cm.choice[3]);
									// ox 판넬과 버튼 안나오게
									JavaGameClientRoom.OX_Panel.setVisible(false);
									JavaGameClientRoom.lblQuestion2.setVisible(false);
									JavaGameClientRoom.lblQuestion2.setVisible(false);
									
									//타임바
									ti = 10;
									Timer m_timer2 = new Timer();
									TimerTask m_task2 = new TimerTask() {
										public void run() {
											JavaGameClientRoom.timebar.setValue(ti);
											String time = Integer.toString(ti);
											JavaGameClientRoom.lblTime.setText(time);
											ti--;	
											if (ti <= -1) {
												//m_task2.cancel();
												m_timer2.cancel(); // 타이머 끝내기
											}
										}
									};

									m_timer2.schedule(m_task2, 0, 1000);
								}
							};
							m_timer.schedule(m_task, 2000);

						} else if (cm.QuizPanel == 2) {
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									AppendText("a");
									JavaGameClientRoom.OX_Panel.setVisible(true);
									JavaGameClientRoom.lblQuestion2.setVisible(true);
									JavaGameClientRoom.lblQuestion2.setVisible(true);
									JavaGameClientRoom.lblQuestion2.setText(cm.Quiz);

									// 객관식 판넬과 버튼 안나오게
									JavaGameClientRoom.Multi_Panel.setVisible(false);
									JavaGameClientRoom.lblQuestion1.setVisible(false);
									JavaGameClientRoom.btnAnswer1.setVisible(false);
									JavaGameClientRoom.btnAnswer2.setVisible(false);
									JavaGameClientRoom.btnAnswer3.setVisible(false);
									JavaGameClientRoom.btnAnswer4.setVisible(false);
									for (int j = 10; j >= 1; j--) {
										JavaGameClientRoom.timebar.setValue(j);										
									}
									
									//타임바
									ti = 10;
									Timer m_timer2 = new Timer();
									TimerTask m_task2 = new TimerTask() {
										public void run() {
											JavaGameClientRoom.timebar.setValue(ti);
											String time = Integer.toString(ti);
											JavaGameClientRoom.lblTime.setText(time);
											ti--;	
											if (ti <= -1) {
												//m_task2.cancel();
												m_timer2.cancel(); // 타이머 끝내기
												
											}
										}
									};
									m_timer2.schedule(m_task2, 0, 1000);
									
								}
							};
							m_timer.schedule(m_task, 2000);
						}
						break;
					case "800":
						// 첫번째 플레이어 정답과 오답 UI
						ImageIcon correct = new ImageIcon("src/spongesuc.gif");
						ImageIcon wrong = new ImageIcon("src/spongefail.gif");
						if (cm.RealAnswer == 0) {
							//JavaGameClientRoom.Right_Panel.setVisible(true);
							JavaGameClientRoom.lblToon1.setIcon(correct);
							String score = Integer.toString(cm.Score[0]);
							JavaGameClientRoom.lblScore.setText(score);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									//JavaGameClientRoom.Right_Panel.setVisible(false);
									JavaGameClientRoom.lblToon1.setIcon(Changecharacter1);
								}
							};
							m_timer.schedule(m_task, 2000);
						} else if (cm.RealAnswer == 1) {
							//avaGameClientRoom.Wrong_Panel.setVisible(true);
							JavaGameClientRoom.lblToon1.setIcon(wrong);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									//JavaGameClientRoom.Wrong_Panel.setVisible(false);
									JavaGameClientRoom.lblToon1.setIcon(Changecharacter1);
								}
							};
							m_timer.schedule(m_task, 2000);
						}
						// 두번째 플레이어 정답과 오답 UI
						if (cm.RealAnswer == 2) {
							JavaGameClientRoom.lblToon2.setIcon(correct);
							String score2 = Integer.toString(cm.Score[1]);
							JavaGameClientRoom.lblScore2.setText(score2);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.lblToon2.setIcon(Changecharacter2);
								}
							};
							m_timer.schedule(m_task, 2000);
						} else if (cm.RealAnswer == 3) {
							JavaGameClientRoom.lblToon2.setIcon(wrong);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.lblToon2.setIcon(Changecharacter2);
								}
							};
							m_timer.schedule(m_task, 2000);
						}
						// 세번째 플레이어 정답과 오답 UI
						if (cm.RealAnswer == 4) {
							JavaGameClientRoom.lblToon3.setIcon(correct);
							String score3 = Integer.toString(cm.Score[2]);
							JavaGameClientRoom.lblScore3.setText(score3);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.lblToon3.setIcon(Changecharacter3);
								}
							};
							m_timer.schedule(m_task, 2000);
						} else if (cm.RealAnswer == 5) {
							JavaGameClientRoom.lblToon3.setIcon(wrong);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.lblToon3.setIcon(Changecharacter3);
								}
							};
							m_timer.schedule(m_task, 2000);
						}
						// 네번째 플레이어 정답과 오답 UI
						if (cm.RealAnswer == 6) {
							JavaGameClientRoom.lblToon4.setIcon(correct);
							String score4 = Integer.toString(cm.Score[3]);
							JavaGameClientRoom.lblScore4.setText(score4);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.lblToon4.setIcon(Changecharacter4);
								}
							};
							m_timer.schedule(m_task, 2000);
						} else if (cm.RealAnswer == 7) {
							JavaGameClientRoom.lblToon4.setIcon(wrong);
							Timer m_timer = new Timer();
							TimerTask m_task = new TimerTask() {
								public void run() {
									JavaGameClientRoom.lblToon4.setIcon(Changecharacter4);
								}
							};
							m_timer.schedule(m_task, 2000);
						}
						break;
					case "850":
						JavaGameClientRoom.Multi_Panel.setVisible(false);
						JavaGameClientRoom.OX_Panel.setVisible(false);
						JavaGameClientRoom.btnAnswer1.setVisible(false);
						JavaGameClientRoom.btnAnswer2.setVisible(false);
						JavaGameClientRoom.btnAnswer3.setVisible(false);
						JavaGameClientRoom.btnAnswer4.setVisible(false);
						JavaGameClientRoom.btnO.setVisible(false);
						JavaGameClientRoom.btnX.setVisible(false);
						JavaGameClientRoom.End_panel.setVisible(true);

						JavaGameClientRoom.lblEnd_Name.setText(cm.EndName[0]);
						JavaGameClientRoom.lblEnd_Name2.setText(cm.EndName[1]);
						JavaGameClientRoom.lblEnd_Name3.setText(cm.EndName[2]);
						JavaGameClientRoom.lblEnd_Name4.setText(cm.EndName[3]);
						System.out.println(cm.EndName[0]);
						System.out.println(cm.EndName[1]);
						System.out.println(cm.EndName[2]);
						System.out.println(cm.EndName[3]);

						String endscore = Integer.toString(cm.EndScore[0]);
						JavaGameClientRoom.lblEnd_Score.setText(endscore);
						String endscore2 = Integer.toString(cm.EndScore[1]);
						JavaGameClientRoom.lblEnd_Score2.setText(endscore2);
						String endscore3 = Integer.toString(cm.EndScore[2]);
						JavaGameClientRoom.lblEnd_Score3.setText(endscore3);
						String endscore4 = Integer.toString(cm.EndScore[3]);
						JavaGameClientRoom.lblEnd_Score4.setText(endscore4);
						
						Timer m_timer = new Timer();
						TimerTask m_task = new TimerTask() {
							public void run() {
								setVisible(true);
							}
						};
						m_timer.schedule(m_task, 2000);
						
					case "900":
						System.out.println(cm.ToonBtn);
						LoginPlayer[0] = cm.LoginPlayer[0];
						LoginPlayer[1] = cm.LoginPlayer[1];
						LoginPlayer[2] = cm.LoginPlayer[2];
						LoginPlayer[3] = cm.LoginPlayer[3];
						PlayerCoin[0] = cm.PlayerCoin[0];
						PlayerCoin[1] = cm.PlayerCoin[1] ;
						PlayerCoin[2] = cm.PlayerCoin[2] ;
						PlayerCoin[3] = cm.PlayerCoin[3] ;
						if(UserName.equals(LoginPlayer[0])) {
							lblCoin1.setText("코인 개수 : " + PlayerCoin[0]);
							JavaGameClientRoom.RlblCoin1.setText("코인 개수 : " + PlayerCoin[0]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
						else if(UserName.equals(LoginPlayer[1])) {
							lblCoin2.setText("코인 개수 : " + cm.PlayerCoin[1]);
							JavaGameClientRoom.RlblCoin2.setText("코인 개수 : " + cm.PlayerCoin[1]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
						else if(UserName.equals(LoginPlayer[2])) {
							lblCoin3.setText("코인 개수 : " + cm.PlayerCoin[2]);
							JavaGameClientRoom.RlblCoin3.setText("코인 개수 : " + cm.PlayerCoin[2]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
						else if(UserName.equals(LoginPlayer[3])) {
							lblCoin4.setText("코인 개수 : " + cm.PlayerCoin[3]);
							JavaGameClientRoom.RlblCoin4.setText("코인 개수 : " + cm.PlayerCoin[3]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
					
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//                  dos.close();
//                  dis.close();
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
	public synchronized void ShopBtn(String ToonBtn) {
		System.out.println("asd");
		if(ToonBtn.equals("btnChar"))
			JavaGameClientShop.btnChar.setVisible(true);
		else if(ToonBtn.equals("btnChar2"))
			JavaGameClientShop.btnChar2.setVisible(true);
		else if(ToonBtn.equals("btnChar3"))
			JavaGameClientShop.btnChar3.setVisible(true);
		else if(ToonBtn.equals("btnChar4"))
			JavaGameClientShop.btnChar4.setVisible(true);
		else if(ToonBtn.equals("btnChar5"))
			JavaGameClientShop.btnChar5.setVisible(true);
		else if(ToonBtn.equals("btnChar6"))
			JavaGameClientShop.btnChar6.setVisible(true);
		else if(ToonBtn.equals("btnChar7"))
			JavaGameClientShop.btnChar7.setVisible(true);
		else if(ToonBtn.equals("btnChar8"))
			JavaGameClientShop.btnChar8.setVisible(true);
	}
	
	public synchronized void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public synchronized void AppendText(String msg) {
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
	public synchronized void AppendTextR(String msg) {
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

	public synchronized void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else {
			textArea.insertIcon(ori_icon);
			new_img = ori_img;
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
		// ImageViewAction viewaction = new ImageViewAction();
		// new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
		// panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(),
		// Image.SCALE_DEFAULT);

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
	public synchronized void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//         byte[] bb;
//         bb = MakePacket(msg);
//         dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//            dos.close();
//            dis.close();
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

	public synchronized void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}