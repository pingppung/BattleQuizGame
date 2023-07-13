package GamePkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

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

public class JavaGameClientRoom extends JFrame {

	private static final long serialVersionUID = 1L;
	private static String username;
	private static String character;

	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JPanel contentPane;
	private JPanel topPane = new JPanel();

	// jtextarea에 스크롤 넣기
	private static JTextPane textArea = new JTextPane();
	private JScrollPane scrollPane = new JScrollPane();
	private JTextField txtInput = new JTextField(); // 채팅 입력
	private JButton btn_Send = new JButton("Send");

	public static JLabel[] lblUserName = new JLabel[4];
	public static JLabel[] lblUserCharacter = new JLabel[4];

	private static JButton btn_Ready = new JButton("준비완료");
	public static JLabel[] lblUserReady = new JLabel[4];

	private static JButton btn_Exit = new JButton("Back");

	public JavaGameClientRoom(String username, String character) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.character = character;
		initWindow();
	}

	private void initWindow() {

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 580); // (가로위치, 세로위치, 가로길이, 세로길이);
		ImageIcon bg = new ImageIcon("src/images/RoomBG.gif");
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(bg.getImage(), 0, 0, d.width, d.height, this);
				// setOpaque(false); //그림을 표시하게 설정,투명하게 조절
				// super.paintComponent(g);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 채팅
		scrollPane.setBounds(770, 70, 295, 380);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));

		txtInput.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		txtInput.setBounds(770, 475, 215, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btn_Send.setBackground(Color.WHITE);
		btn_Send.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		btn_Send.setBounds(990, 475, 75, 40);
		contentPane.add(btn_Send);
		//

		// 준비버튼
		btn_Ready.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		btn_Ready.setBackground(Color.WHITE);
		btn_Ready.setBounds(670, 15, 100, 30);
		contentPane.add(btn_Ready);

		// 나가기=뒤로가기 버튼
		btn_Exit.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		btn_Exit.setBackground(Color.WHITE);
		btn_Exit.setBounds(965, 12, 100, 35);
		contentPane.add(btn_Exit);

		// 상단 패널
		topPane.setBackground(new Color(204, 204, 255));
		topPane.setBounds(0, 0, 1101, 55);
		contentPane.add(topPane);

		// 본인 닉네임 PlayList에 저장 && 화면에 적용
		for (int i = 0; i < 4; i++) {
			lblUserName[i] = new JLabel("");
			lblUserName[i].setBackground(Color.BLACK);
			lblUserName[i].setForeground(Color.WHITE);
			lblUserName[i].setFont(new Font("배달의민족 주아", Font.BOLD, 20));
			lblUserName[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblUserName[i].setBounds(61 + i * 180, 422, 121, 40);

			lblUserCharacter[i] = new JLabel("");
			lblUserCharacter[i].setBounds(61 + i * 180, 285, 121, 127);
			lblUserCharacter[i].setHorizontalAlignment(SwingConstants.CENTER);

			lblUserReady[i] = new JLabel("Ready");
			lblUserReady[i].setVisible(false);
			lblUserReady[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblUserReady[i].setForeground(Color.WHITE);
			lblUserReady[i].setFont(new Font("배달의민족 도현", Font.BOLD, 20));
			lblUserReady[i].setBorder(new LineBorder(new Color(0, 0, 0), 0));
			lblUserReady[i].setBounds(61 + i * 180, 485, 121, 40);
			

			contentPane.add(lblUserName[i]);
			contentPane.add(lblUserCharacter[i]);
			contentPane.add(lblUserReady[i]);
		}

		setVisible(true);
		ReadyButtonClick action_ready = new ReadyButtonClick();
		btn_Ready.addActionListener(action_ready);

		RoomExitButtonClick action_exit = new RoomExitButtonClick();
		btn_Exit.addActionListener(action_exit);

	}

	// Player가 게임방에서 레디 or 대기 버튼 눌렀을때
	class ReadyButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			ChatMsg obcm = new ChatMsg(username, "550", btn_Ready.getText()); //ready변경사항 서버에 보내기 (다른 유저들 화면에서 띄우게)
			AppendText(btn_Ready.getText());
			if(btn_Ready.getText().equals("준비완료")) { //대기 -> 레디 상태
				btn_Ready.setText("준비취소");
			} else { //레디 -> 대기 상태
				btn_Ready.setText("준비완료");
			}
			JavaGameClientView.SendObject(obcm);
		}
	}

	// Player가 게임방 나가기 버튼 눌렀을 때
	class RoomExitButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(username, "600", "RoomExit");
			JavaGameClientView.SendObject(cm);
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

}