
// JavaObjClientView.java ObjecStram ��� Client
//�������� ä�� â
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
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
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
	private String Character = "src/Character.gif"; // �⺻ ĳ���� ����

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

	//�α����� �÷��̾� �� �����Ű�� ����
	private String[] LoginPlayer = new String[4];
	private int LoginPlayerNum = 0;
	private int[] PlayerCoin = new int[4];
	
	//Ÿ�ӹ�
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
             setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
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
				textArea.setFont(new Font("����������", Font.PLAIN, 14));

		txtInput = new JTextField();
		txtInput.setFont(new Font("����������", Font.PLAIN, 12));
		txtInput.setBounds(12, 464, 375, 30);
		contentPane_1.add(txtInput);
		txtInput.setColumns(10);

	    //��ư �⺻, ���콺 �÷��� ��, �������� ���� ��ȭ
	    ImageIcon normalIcon = new ImageIcon("src/btnRoom.png");
		ImageIcon rolloverIcon = new ImageIcon("src/btnRoom2.png");
		ImageIcon pressedIcon = new ImageIcon("src/btnRoom2.png");
		JButton btnEntry = new JButton(normalIcon);
		btnEntry.setPressedIcon(pressedIcon); // pressedIcon�� �̹��� ���
		btnEntry.setRolloverIcon(rolloverIcon); // rolloverIcon�� �̹��� ���
		btnEntry.setBounds(534, 373, 152, 57);
		contentPane_1.add(btnEntry);
		
        ImageIcon face2;
        face2 = new ImageIcon("src/BackgroundLabel.png");
        JPanel panel = new JPanel() {
          public void paintComponent(Graphics g) {
             Dimension d = getSize();
             g.drawImage(face2.getImage(), 0, 0, d.width, d.height, this);
             setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
                 super.paintComponent(g);
          }
       };
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.WHITE);
		panel.setBounds(0, 0, 696, 52);
		contentPane_1.add(panel);
		panel.setLayout(null);

		btnShop = new JButton("Shop");
		btnShop.setFont(new Font("����������", Font.PLAIN, 12));
		btnShop.setBackground(Color.WHITE);
		btnShop.setBounds(508, 8, 105, 37);
		panel.add(btnShop);

		JButton btnNewButton = new JButton("->");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBounds(625, 8, 49, 37);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("����������", Font.PLAIN, 14));
		lblCoin1.setFont(new Font("����������", Font.PLAIN, 12));

		lblCoin1.setBounds(12, 5, 133, 42);
		panel.add(lblCoin1);
		lblCoin2.setFont(new Font("����������", Font.PLAIN, 12));

		lblCoin2.setBounds(12, 5, 133, 42);
		panel.add(lblCoin2);
		lblCoin3.setFont(new Font("����������", Font.PLAIN, 12));

		lblCoin3.setBounds(12, 5, 133, 42);
		panel.add(lblCoin3);
		lblCoin4.setFont(new Font("����������", Font.PLAIN, 12));

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
		lblUserName.setFont(new Font("����ǹ��� �־�", Font.BOLD, 25));
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
             setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
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

	// īƮ ��ư ������ ���
	class Cartaction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JavaGameClientShop cart = new JavaGameClientShop(UserName);
			// setVisible(false);
		}
	}

	// �� ���� ��ư ������ ���
	// ��� - ���ӹ� ȭ�� ���� && 4�� ���� ����
	class RoomEntryButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			ChatMsg obcm = new ChatMsg(UserName, "500", "Entry");
			obcm.UserToon = Character;
			SendObject(obcm);
			// ���ӹ� ȭ�� ����
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

	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				txtInput.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}

	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
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
							lblCoin1.setText("���� ���� : " + PlayerCoin[0]);
						else if(UserName.equals(LoginPlayer[1]))
							lblCoin2.setText("���� ���� : " + PlayerCoin[1]);
						else if(UserName.equals(LoginPlayer[2]))
							lblCoin3.setText("���� ���� : " + PlayerCoin[2]);
						else if(UserName.equals(LoginPlayer[3]))
							lblCoin4.setText("���� ���� : " + PlayerCoin[3]);
						
					case "200": // chat message
						if (cm.UserName.equals(UserName))
							AppendTextR(msg); // �� �޼����� ������
						else
							AppendText(msg);
						break;
					case "250":
						if (cm.UserName.equals(UserName))
							JavaGameClientRoom.AppendTextR(msg);
						else
							JavaGameClientRoom.AppendText(msg);
						break;
					case "300": // Image ÷��
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						break;
					case "500":
						PlayerName = cm.PlayerN;
						PlayerToon = cm.Character; // �÷��̾� ĳ���� ���� �޾Ƶ��̱�
						JavaGameClientRoom.lblUserName1.setText(PlayerName[0]);
						JavaGameClientRoom.lblUserName2.setText(PlayerName[1]);
						JavaGameClientRoom.lblUserName3.setText(PlayerName[2]);
						JavaGameClientRoom.lblUserName4.setText(PlayerName[3]);

						// �ڱ� �̸� ��ũ������ ǥ��
						// ���⼭ ������!!! ù������ �� ����� �ι����� �÷��̾ ���ö����� �Ⱥ���....
						// �ڿ������� ������

						Changecharacter1 = new ImageIcon(PlayerToon[0]);
						Changecharacter2 = new ImageIcon(PlayerToon[1]);
						Changecharacter3 = new ImageIcon(PlayerToon[2]);
						Changecharacter4 = new ImageIcon(PlayerToon[3]);
						// ĳ���ʹ� �ּҸ� �ѱ�� seticon�ϴ� �κ��� room���� �Ұ�
						JavaGameClientRoom.lblToon1.setIcon(Changecharacter1);
						JavaGameClientRoom.lblToon2.setIcon(Changecharacter2);
						JavaGameClientRoom.lblToon3.setIcon(Changecharacter3);
						JavaGameClientRoom.lblToon4.setIcon(Changecharacter4);
						
						break;
					case "550":
						if (cm.UserName.equals(UserName)) {
							Character = cm.UserToon; // String���� �޾Ƶ��̱�
							ImageIcon character = new ImageIcon(Character); // icon���� �����
							lblCharacter.setIcon(character); // �󺧿� icon ����ֱ�
						}
						break;
					case "600":
						if (cm.i == 0) { // ù������ ���� �÷��̾��� Ready��
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady.setForeground(Color.yellow);
								PlayerReady[0] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady.setForeground(Color.WHITE);
								PlayerReady[0] = "Wait";
							}
						} else if (cm.i == 1) {// �ι����� ���� �÷��̾��� Ready��
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady2.setForeground(Color.yellow);
								PlayerReady[1] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady2.setForeground(Color.WHITE);
								PlayerReady[1] = "Wait";
							}
						} else if (cm.i == 2) {// �������� ���� �÷��̾��� Ready��
							if (cm.UserReady.equals("Ready")) {
								JavaGameClientRoom.lblReady3.setForeground(Color.yellow);
								PlayerReady[2] = "Ready";
							} else if (cm.UserReady.equals("Wait")) {
								JavaGameClientRoom.lblReady3.setForeground(Color.WHITE);
								PlayerReady[2] = "Wait";
							}
						} else if (cm.i == 3) {// �׹����� ���� �÷��̾��� Ready��
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
									// ox �ǳڰ� ��ư �ȳ�����
									JavaGameClientRoom.OX_Panel.setVisible(false);
									JavaGameClientRoom.lblQuestion2.setVisible(false);
									JavaGameClientRoom.lblQuestion2.setVisible(false);
									
									//Ÿ�ӹ�
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
												m_timer2.cancel(); // Ÿ�̸� ������
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

									// ������ �ǳڰ� ��ư �ȳ�����
									JavaGameClientRoom.Multi_Panel.setVisible(false);
									JavaGameClientRoom.lblQuestion1.setVisible(false);
									JavaGameClientRoom.btnAnswer1.setVisible(false);
									JavaGameClientRoom.btnAnswer2.setVisible(false);
									JavaGameClientRoom.btnAnswer3.setVisible(false);
									JavaGameClientRoom.btnAnswer4.setVisible(false);
									for (int j = 10; j >= 1; j--) {
										JavaGameClientRoom.timebar.setValue(j);										
									}
									
									//Ÿ�ӹ�
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
												m_timer2.cancel(); // Ÿ�̸� ������
												
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
						// ù��° �÷��̾� ����� ���� UI
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
						// �ι�° �÷��̾� ����� ���� UI
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
						// ����° �÷��̾� ����� ���� UI
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
						// �׹�° �÷��̾� ����� ���� UI
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
							lblCoin1.setText("���� ���� : " + PlayerCoin[0]);
							JavaGameClientRoom.RlblCoin1.setText("���� ���� : " + PlayerCoin[0]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
						else if(UserName.equals(LoginPlayer[1])) {
							lblCoin2.setText("���� ���� : " + cm.PlayerCoin[1]);
							JavaGameClientRoom.RlblCoin2.setText("���� ���� : " + cm.PlayerCoin[1]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
						else if(UserName.equals(LoginPlayer[2])) {
							lblCoin3.setText("���� ���� : " + cm.PlayerCoin[2]);
							JavaGameClientRoom.RlblCoin3.setText("���� ���� : " + cm.PlayerCoin[2]);
							if(cm.ToonBtn != null)
								ShopBtn(cm.ToonBtn);
						}
						else if(UserName.equals(LoginPlayer[3])) {
							lblCoin4.setText("���� ���� : " + cm.PlayerCoin[3]);
							JavaGameClientRoom.RlblCoin4.setText("���� ���� : " + cm.PlayerCoin[3]);
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
					} // catch�� ��
				} // �ٱ� catch����

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
		// ������ �̵�
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// ȭ�鿡 ���
	public synchronized void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.
		int len = textArea.getDocument().getLength();
		// ������ �̵�
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

	// ȭ�� ������ ���
	public synchronized void AppendTextR(String msg) {
		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.
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
		// Image�� �ʹ� ũ�� �ִ� ���� �Ǵ� ���� 200 �������� ��ҽ�Ų��.
		if (width > 200 || height > 200) {
			if (width > height) { // ���� ����
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // ���� ����
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
		// new_icon.addActionListener(viewaction); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
		// panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(),
		// Image.SCALE_DEFAULT);

	}

	// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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

	// Server���� network���� ����
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

	public synchronized void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("�޼��� �۽� ����!!\n");
			AppendText("SendObject Error");
		}
	}
}