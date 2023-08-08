
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
import javax.swing.JProgressBar;

public class JavaGameClientRoom extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInput;
	private static String UserName;
	private static String toon; // 캐릭터의 줄임말
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	// private JTextArea textArea;
	private static JTextPane textArea = new JTextPane();

	private Frame frame;
	private FileDialog fd;

	private static String[] PlayerName = new String[4];
	private static String[] PlayerCharacter = new String[4];
	private static String p;
	private static String c;
	private int PlayerNum = 0;
	private JLabel[] lblNewLabel = new JLabel[4];
	public static JButton btnReady = new JButton("Ready");

	public static JLabel RlblCoin1 = new JLabel("");
	public static JLabel RlblCoin2 = new JLabel("");
	public static JLabel RlblCoin3 = new JLabel("");
	public static JLabel RlblCoin4 = new JLabel("");
	
	public static JLabel lblUserName1 = new JLabel("");
	public static JLabel lblUserName2 = new JLabel("");
	public static JLabel lblUserName3 = new JLabel("");
	public static JLabel lblUserName4 = new JLabel("");

	public static JLabel lblToon1 = new JLabel("");
	public static JLabel lblToon2 = new JLabel("");
	public static JLabel lblToon3 = new JLabel("");
	public static JLabel lblToon4 = new JLabel("");

	public static JLabel lblReady = new JLabel("Ready");
	public static JLabel lblReady2 = new JLabel("Ready");
	public static JLabel lblReady3 = new JLabel("Ready");
	public static JLabel lblReady4 = new JLabel("Ready");

	public static JLabel lblScore = new JLabel("0000");
	public static JLabel lblScore2 = new JLabel("0000");
	public static JLabel lblScore3 = new JLabel("0000");
	public static JLabel lblScore4 = new JLabel("0000");

	public static JPanel Right_Panel = new JPanel();
	public static JPanel Right_Panel4;
	public static JPanel Right_Panel3;
	public static JPanel Right_Panel2;
	public static JPanel Wrong_Panel = new JPanel();
	public static JPanel Wrong_Panel4;
	public static JPanel Wrong_Panel3;
	public static JPanel Wrong_Panel2;

	public static JPanel End_panel = new JPanel();
	public static JLabel lblEnd_Name = new JLabel("1등 이름");
	public static JLabel lblEnd_Name2 = new JLabel("2등 이름");
	public static JLabel lblEnd_Name3 = new JLabel("3등 이름");
	public static JLabel lblEnd_Name4 = new JLabel("4등 이름");
	public static JLabel lblEnd_Score = new JLabel("1등 점수");
	public static JLabel lblEnd_Score2 = new JLabel("2등 점수");
	public static JLabel lblEnd_Score3 = new JLabel("3등 점수");
	public static JLabel lblEnd_Score4 = new JLabel("4등 점수");

	public static JPanel Multi_Panel = new JPanel();
	public static JPanel OX_Panel = new JPanel();
	public static JLabel lblQuestion1 = new JLabel("Question");
	public static JButton btnAnswer1;
	public static JButton btnAnswer2;
	public static JButton btnAnswer3;
	public static JButton btnAnswer4;

	public static JLabel lblQuestion2;
	public static JButton btnO;
	public static JButton btnX;
	
	public static JProgressBar timebar;
	public static JLabel lblTime = new JLabel("10");
	private final JPanel panel = new JPanel();
	private final JLabel lblNewLabel_2_2 = new JLabel("\uC810");
	private final JLabel lblNewLabel_2_1_2 = new JLabel("\uC810");
	private final JLabel lblNewLabel_2_1_1_2 = new JLabel("\uC810");
	private final JLabel lblNewLabel_2_1_1_1_1 = new JLabel("\uC810");


	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public JavaGameClientRoom(String username, String character) {
		UserName = username;
		toon = character;
		initWindow();
		// AppendText(UserName +"님이 입장하셨습니다"); //모든 클라이언트에게 다 보이도록 하기!!!

	}

	private void initWindow() {

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1105, 580);
		
        ImageIcon face10;
        face10 = new ImageIcon("src/roomback.gif");
        contentPane = new JPanel() {
          public void paintComponent(Graphics g) {
             Dimension d = getSize();
             g.drawImage(face10.getImage(), 0, 0, d.width, d.height, this);
             setOpaque(false); //그림을 표시하게 설정,투명하게 조절
                 super.paintComponent(g);
          }
       };
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(782, 60, 290, 421);
		contentPane.add(scrollPane);
				scrollPane.setViewportView(textArea);
		
				textArea.setEditable(false);
				textArea.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));

		txtInput = new JTextField();
		txtInput.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		txtInput.setBounds(782, 489, 219, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBackground(Color.WHITE);
		btnSend.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		btnSend.setBounds(1003, 488, 69, 40);
		contentPane.add(btnSend);
		
		lblUserName1.setForeground(Color.WHITE);
		lblUserName1.setBackground(Color.BLACK);
		lblUserName1.setFont(new Font("배달의민족 주아", Font.BOLD, 20));
		lblUserName1.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName1.setBounds(61, 422, 121, 40);
		contentPane.add(lblUserName1);
		
		lblUserName2.setForeground(Color.WHITE);
		lblUserName2.setBackground(Color.BLACK);
		lblUserName2.setFont(new Font("배달의민족 주아", Font.BOLD, 20));
		lblUserName2.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName2.setBounds(246, 422, 121, 40);
		contentPane.add(lblUserName2);
		
		lblUserName3.setForeground(Color.WHITE);
		lblUserName3.setBackground(Color.BLACK);
		lblUserName3.setFont(new Font("배달의민족 주아", Font.BOLD, 20));
		lblUserName3.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName3.setBounds(428, 422, 121, 40);
		contentPane.add(lblUserName3);
		
		lblUserName4.setForeground(Color.WHITE);
		lblUserName4.setBackground(Color.BLACK);
		lblUserName4.setFont(new Font("배달의민족 주아", Font.BOLD, 20));
		lblUserName4.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName4.setBounds(603, 422, 115, 40);
		contentPane.add(lblUserName4);

		JButton btnNewButton = new JButton("닫 기");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton.setBounds(977, 15, 95, 30);
		contentPane.add(btnNewButton);
		btnReady.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		btnReady.setBackground(Color.WHITE);

		btnReady.setBounds(678, 15, 95, 30);
		contentPane.add(btnReady);

		lblToon1.setBounds(61, 285, 121, 127);
		lblToon1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblToon1);

		lblToon2.setBounds(246, 285, 121, 127);
		lblToon2.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblToon2);

		lblToon3.setBounds(428, 285, 121, 127);
		lblToon3.setHorizontalAlignment(SwingConstants.CENTER);
		lblToon3.setBorder(null);
		contentPane.add(lblToon3);

		lblToon4.setBounds(603, 285, 121, 127);
		lblToon4.setHorizontalAlignment(SwingConstants.CENTER);
		lblToon4.setBorder(null);
		contentPane.add(lblToon4);

		ImageIcon face1;
		face1 = new ImageIcon("src/Right.gif");
		Right_Panel = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face1.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Right_Panel.setBounds(61, 285, 121, 127);
		contentPane.add(Right_Panel);

		ImageIcon face2;
		face2 = new ImageIcon("src/Wrong.gif");
		Wrong_Panel = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face2.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Wrong_Panel.setBounds(61, 285, 121, 127);
		contentPane.add(Wrong_Panel);

		ImageIcon face3;
		face3 = new ImageIcon("src/Right.gif");
		Right_Panel2 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face3.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Right_Panel2.setBounds(246, 285, 121, 127);
		contentPane.add(Right_Panel2);

		ImageIcon face4;
		face4 = new ImageIcon("src/Wrong.gif");
		Wrong_Panel2 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face4.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Wrong_Panel2.setBounds(246, 285, 121, 127);
		contentPane.add(Wrong_Panel2);

		ImageIcon face5;
		face5 = new ImageIcon("src/Right.gif");
		Right_Panel3 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face5.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Right_Panel3.setBounds(428, 285, 121, 127);
		contentPane.add(Right_Panel3);

		ImageIcon face6;
		face6 = new ImageIcon("src/Wrong.gif");
		Wrong_Panel3 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face6.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Wrong_Panel3.setBounds(428, 285, 121, 127);
		contentPane.add(Wrong_Panel3);

		ImageIcon face7;
		face7 = new ImageIcon("src/Right.gif");
		Right_Panel4 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face7.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Right_Panel4.setBounds(603, 285, 121, 127);
		contentPane.add(Right_Panel4);

		ImageIcon face8;
		face8 = new ImageIcon("src/Wrong.gif");
		Wrong_Panel4 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face8.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		Wrong_Panel4.setBounds(603, 285, 121, 127);
		contentPane.add(Wrong_Panel4);

		lblReady.setHorizontalAlignment(SwingConstants.CENTER);
		lblReady.setForeground(Color.WHITE);
		lblReady.setFont(new Font("배달의민족 도현", Font.BOLD, 20));
		lblReady.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		lblReady.setBounds(61, 472, 121, 40);
		contentPane.add(lblReady);

		lblReady2.setHorizontalAlignment(SwingConstants.CENTER);
		lblReady2.setForeground(Color.WHITE);
		lblReady2.setFont(new Font("배달의민족 도현", Font.BOLD, 20));
		lblReady2.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		lblReady2.setBounds(246, 472, 121, 40);
		contentPane.add(lblReady2);

		lblReady3.setHorizontalAlignment(SwingConstants.CENTER);
		lblReady3.setForeground(Color.WHITE);
		lblReady3.setFont(new Font("배달의민족 도현", Font.BOLD, 20));
		lblReady3.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		lblReady3.setBounds(428, 472, 121, 40);
		contentPane.add(lblReady3);

		lblReady4.setHorizontalAlignment(SwingConstants.CENTER);
		lblReady4.setForeground(Color.WHITE);
		lblReady4.setFont(new Font("배달의민족 도현", Font.BOLD, 20));
		lblReady4.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		lblReady4.setBounds(603, 472, 121, 40);
		contentPane.add(lblReady4);
		lblScore.setForeground(Color.WHITE);

		lblScore.setFont(new Font("배달의민족 도현", Font.PLAIN, 20));
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore.setBackground(Color.WHITE);
		lblScore.setBounds(61, 472, 121, 40);
		contentPane.add(lblScore);
		lblScore2.setForeground(Color.WHITE);

		lblScore2.setFont(new Font("배달의민족 도현", Font.PLAIN, 20));
		lblScore2.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore2.setBackground(Color.WHITE);
		lblScore2.setBounds(246, 472, 121, 40);
		contentPane.add(lblScore2);
		lblScore3.setForeground(Color.WHITE);

		lblScore3.setFont(new Font("배달의민족 도현", Font.PLAIN, 20));
		lblScore3.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore3.setBackground(Color.WHITE);
		lblScore3.setBounds(428, 472, 121, 40);
		contentPane.add(lblScore3);
		lblScore4.setForeground(Color.WHITE);

		lblScore4.setFont(new Font("배달의민족 도현", Font.PLAIN, 20));
		lblScore4.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore4.setBackground(Color.WHITE);
		lblScore4.setBounds(603, 472, 121, 40);
		contentPane.add(lblScore4);
		End_panel.setBackground(Color.WHITE);

		End_panel.setBounds(12, 60, 761, 206);
		contentPane.add(End_panel);
		End_panel.setLayout(null);
		lblEnd_Name.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Name.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Name.setBounds(130, 45, 250, 30);
		End_panel.add(lblEnd_Name);
		lblEnd_Name2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Name2.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Name2.setBounds(130, 85, 250, 30);
		End_panel.add(lblEnd_Name2);
		lblEnd_Name3.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Name3.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Name3.setBounds(130, 125, 250, 30);
		End_panel.add(lblEnd_Name3);
		lblEnd_Name4.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Name4.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Name4.setBounds(130, 165, 250, 30);
		End_panel.add(lblEnd_Name4);
		lblEnd_Score.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));

		lblEnd_Score.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Score.setBounds(424, 45, 250, 30);
		End_panel.add(lblEnd_Score);
		lblEnd_Score2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Score2.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Score2.setBounds(424, 85, 250, 30);
		End_panel.add(lblEnd_Score2);
		lblEnd_Score3.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Score3.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Score3.setBounds(424, 125, 250, 30);
		End_panel.add(lblEnd_Score3);
		lblEnd_Score4.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		
		lblEnd_Score4.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd_Score4.setBounds(424, 165, 250, 30);
		End_panel.add(lblEnd_Score4);
		
		JLabel lblNewLabel_1 = new JLabel("\uCD95\uD558\uD569\uB2C8\uB2E4");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(285, 10, 182, 30);
		End_panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("1\uB4F1");
		lblNewLabel_2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(35, 45, 92, 30);
		End_panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("2\uB4F1");
		lblNewLabel_2_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1.setBounds(35, 85, 92, 30);
		End_panel.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("3\uB4F1");
		lblNewLabel_2_1_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1_1.setBounds(35, 125, 92, 30);
		End_panel.add(lblNewLabel_2_1_1);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("4\uB4F1");
		lblNewLabel_2_1_1_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1_1_1.setBounds(35, 165, 92, 30);
		End_panel.add(lblNewLabel_2_1_1_1);
		lblNewLabel_2_2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_2.setBounds(641, 45, 92, 30);
		
		End_panel.add(lblNewLabel_2_2);
		lblNewLabel_2_1_2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_1_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_1_2.setBounds(641, 85, 92, 30);
		
		End_panel.add(lblNewLabel_2_1_2);
		lblNewLabel_2_1_1_2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_1_1_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_1_1_2.setBounds(641, 125, 92, 30);
		
		End_panel.add(lblNewLabel_2_1_1_2);
		lblNewLabel_2_1_1_1_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblNewLabel_2_1_1_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_1_1_1_1.setBounds(641, 165, 92, 30);
		
		End_panel.add(lblNewLabel_2_1_1_1_1);

		Multi_Panel.setBackground(Color.WHITE);
		Multi_Panel.setForeground(Color.WHITE);
		Multi_Panel.setBounds(12, 60, 761, 206);
		contentPane.add(Multi_Panel);
		Multi_Panel.setLayout(null);
		lblQuestion1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));

		lblQuestion1.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuestion1.setBounds(12, 5, 737, 64);
		Multi_Panel.add(lblQuestion1);

		btnAnswer1 = new JButton("1");
		btnAnswer1.setBackground(Color.WHITE);
		btnAnswer1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnAnswer1.setBounds(12, 79, 362, 49);
		Multi_Panel.add(btnAnswer1);

		btnAnswer2 = new JButton("2");
		btnAnswer2.setBackground(Color.WHITE);
		btnAnswer2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnAnswer2.setBounds(387, 79, 362, 49);
		Multi_Panel.add(btnAnswer2);

		btnAnswer4 = new JButton("4");
		btnAnswer4.setBackground(Color.WHITE);
		btnAnswer4.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnAnswer4.setBounds(387, 147, 362, 49);
		Multi_Panel.add(btnAnswer4);

		btnAnswer3 = new JButton("3");
		btnAnswer3.setBackground(Color.WHITE);
		btnAnswer3.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnAnswer3.setBounds(12, 147, 362, 49);
		Multi_Panel.add(btnAnswer3);

		OX_Panel = new JPanel();
		OX_Panel.setBackground(Color.WHITE);
		OX_Panel.setBounds(12, 60, 761, 206);
		contentPane.add(OX_Panel);
		OX_Panel.setLayout(null);

		lblQuestion2 = new JLabel("Question");
		lblQuestion2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		lblQuestion2.setBounds(12, 10, 737, 59);
		OX_Panel.add(lblQuestion2);
		lblQuestion2.setHorizontalAlignment(SwingConstants.CENTER);

		btnO = new JButton("O");
		btnO.setBackground(Color.WHITE);
		btnO.setFont(new Font("나눔스퀘어", Font.PLAIN, 50));
		btnO.setBounds(12, 79, 362, 117);
		OX_Panel.add(btnO);

		btnX = new JButton("X");
		btnX.setBackground(Color.WHITE);
		btnX.setFont(new Font("나눔스퀘어", Font.PLAIN, 50));
		btnX.setBounds(386, 79, 362, 117);
		OX_Panel.add(btnX);
		
		timebar = new JProgressBar();
		timebar.setForeground(new Color(153, 153, 204));
		timebar.setValue(10);
		timebar.setMaximum(10);
		timebar.setBounds(240, 15, 309, 30);
		contentPane.add(timebar);
		RlblCoin1.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		RlblCoin1.setBounds(12, 10, 81, 35);
		
		contentPane.add(RlblCoin1);
		RlblCoin2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		RlblCoin2.setBounds(12, 10, 81, 35);
		
		contentPane.add(RlblCoin2);
		RlblCoin3.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		RlblCoin3.setBounds(12, 10, 81, 35);
		
		contentPane.add(RlblCoin3);
		RlblCoin4.setBounds(12, 10, 81, 35);
		
		contentPane.add(RlblCoin4);
		lblTime.setFont(new Font("배달의민족 도현", Font.PLAIN, 15));
		
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setBounds(191, 10, 50, 39);
		contentPane.add(lblTime);
		panel.setBackground(new Color(204, 204, 255));
		panel.setBounds(0, 0, 1101, 55);
		
		contentPane.add(panel);

		End_panel.setVisible(false);
		Multi_Panel.setVisible(false);
		OX_Panel.setVisible(false);
		Right_Panel.setVisible(false);
		Wrong_Panel.setVisible(false);
		Right_Panel2.setVisible(false);
		Wrong_Panel2.setVisible(false);
		Right_Panel3.setVisible(false);
		Wrong_Panel3.setVisible(false);
		Right_Panel4.setVisible(false);
		Wrong_Panel4.setVisible(false);
		lblScore.setVisible(false);
		lblScore2.setVisible(false);
		lblScore3.setVisible(false);
		lblScore4.setVisible(false);
		RlblCoin1.setVisible(false);
		RlblCoin2.setVisible(false);
		RlblCoin3.setVisible(false);
		RlblCoin4.setVisible(false);
		
		setVisible(true);
		// SendMessage("/login " + UserName);
		// ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
		// SendObject(obcm);

		TextSendAction action = new TextSendAction();
		btnSend.addActionListener(action);
		txtInput.addActionListener(action);
		txtInput.requestFocus();

		ReadyButtonClick action2 = new ReadyButtonClick();
		btnReady.addActionListener(action2);

		AnswerButtonClick1 Answeraction1 = new AnswerButtonClick1();
		btnAnswer1.addActionListener(Answeraction1);
		AnswerButtonClick2 Answeraction2 = new AnswerButtonClick2();
		btnAnswer2.addActionListener(Answeraction2);
		AnswerButtonClick3 Answeraction3 = new AnswerButtonClick3();
		btnAnswer3.addActionListener(Answeraction3);
		AnswerButtonClick4 Answeraction4 = new AnswerButtonClick4();
		btnAnswer4.addActionListener(Answeraction4);
		AnswerButtonClick5 Answeraction5 = new AnswerButtonClick5();
		btnO.addActionListener(Answeraction5);
		AnswerButtonClick6 Answeraction6 = new AnswerButtonClick6();
		btnX.addActionListener(Answeraction6);
	}

	// Player가 정답1번 눌렀을 때 이벤트
	class AnswerButtonClick1 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "750", "Answer");
			cm.Answer = 1;
			SendObject(cm);
		}
	}

	// Player가 정답2번 눌렀을 때 이벤트
	class AnswerButtonClick2 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "750", "Answer");
			cm.Answer = 2;
			SendObject(cm);
		}
	}

	// Player가 정답3번 눌렀을 때 이벤트
	class AnswerButtonClick3 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "750", "Answer");
			cm.Answer = 3;
			SendObject(cm);
		}
	}

	// Player가 정답4번 눌렀을 때 이벤트
	class AnswerButtonClick4 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "750", "Answer");
			cm.Answer = 4;
			SendObject(cm);
		}
	}

	// Player가 정답o번 눌렀을 때 이벤트
	class AnswerButtonClick5 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "750", "Answer");
			cm.Answer = 5;
			SendObject(cm);
		}
	}

	// Player가 정답x번 눌렀을 때 이벤트
	class AnswerButtonClick6 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "750", "Answer");
			cm.Answer = 6;
			SendObject(cm);
		}
	}

	// Player가 준비 버튼 눌렀을 떄 이벤트
	class ReadyButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(UserName, "600", "Readystatus");
			// 플레이어 ready상태로 만들기
			if (btnReady.getText().equals("Ready")) {
				btnReady.setText("Wait");
				cm.UserReady = "Ready";
			} else { // 플레이어 wait상태로 만들기
				btnReady.setText("Ready");
				cm.UserReady = "Wait";
			}
			SendObject(cm);
		}

	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
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

	// 화면에 출력
	public synchronized static void AppendText(String msg) {
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
	public synchronized static void AppendTextR(String msg) {
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
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "250", msg);
			JavaGameClientView.oos.writeObject(obcm);
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

	public synchronized void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			JavaGameClientView.oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}
