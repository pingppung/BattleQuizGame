package GamePkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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

	private static JPanel contentPane;
	private static JPanel topPane = new JPanel();

	// jtextarea에 스크롤 넣기
	private static JTextPane textArea = new JTextPane();
	private JScrollPane scrollPane = new JScrollPane();
	private JTextField txtInput = new JTextField(); // 채팅 입력
	private JButton btn_Send = new JButton("Send");

	public static JLabel[] lblUserName = new JLabel[4];
	public static JLabel[] lblUserCharacter = new JLabel[4];

	// send, ready, exit 한번에 배열로 묶으면 init함수부분이 깔끔해질 수는 있는데
	public static JButton btn_Ready = new JButton("준비완료");
	public static JLabel[] lblUserReady = new JLabel[4];

	private static JButton btn_Exit = new JButton("Back");

	public static JLabel[] lblScore = new JLabel[4]; // 각 플레이어 점수

	public static JProgressBar timebar;
	public static JLabel lblTime = new JLabel("10");

	public static JPanel QuizPane = new JPanel();
	public static JLabel lblQuestion = new JLabel();
	public static JButton[] btn_quizV = new JButton[4];
	public static JButton[] btn_OX = new JButton[2];

	public static String ans = "";
	
	public static JLabel[] rank = new JLabel[4];
	public JavaGameClientRoom(String username, String character) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.character = character;
		initWindow();
		//GameOver();
		ReadyButtonClick action_ready = new ReadyButtonClick();
		btn_Ready.addActionListener(action_ready);

		RoomExitButtonClick action_exit = new RoomExitButtonClick();
		btn_Exit.addActionListener(action_exit);

		TextSendAction action = new TextSendAction();
		btn_Send.addActionListener(action);
		txtInput.addActionListener(action);
		txtInput.requestFocus();
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

		// fileComponent.fil

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

			lblScore[i] = new JLabel("0");
			lblScore[i].setVisible(false);
			lblScore[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblScore[i].setForeground(Color.WHITE);
			lblScore[i].setFont(new Font("배달의민족 도현", Font.BOLD, 20));
			lblScore[i].setBorder(new LineBorder(new Color(0, 0, 0), 0));
			lblScore[i].setBounds(61 + i * 180, 485, 121, 40);

			contentPane.add(lblUserName[i]);
			contentPane.add(lblUserCharacter[i]);
			contentPane.add(lblUserReady[i]);
			contentPane.add(lblScore[i]);
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // x버튼 눌러도 반응없게
		setVisible(true);

	}

	// Player가 게임방에서 레디 or 대기 버튼 눌렀을때
	class ReadyButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg obcm = new ChatMsg(username, "550", btn_Ready.getText()); // ready변경사항 서버에 보내기 (다른 유저들 화면에서 띄우게)
			// AppendText(btn_Ready.getText());
			if (btn_Ready.getText().equals("준비완료")) { // 대기 -> 레디 상태
				btn_Ready.setText("준비취소");
			} else { // 레디 -> 대기 상태
				btn_Ready.setText("준비완료");
			}
			JavaGameClientView.SendObject(obcm);
		}
	}

	// Player가 게임방 나가기 버튼 눌렀을 때
	class RoomExitButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(username, "400", "RoomExit");
			JavaGameClientView.SendObject(cm);
			dispose();
		}
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btn_Send || e.getSource() == txtInput) {
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

	// 퀴즈 정답 선택
	class QuestionChoiceAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}

	// 화면에 출력
	public synchronized static void AppendText(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
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

	public static void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(username, "250", msg);
			JavaGameClientView.oos.writeObject(obcm);
		} catch (IOException e) {
			AppendText("oos.writeObject() error");
			try {
				JavaGameClientView.ois.close();
				JavaGameClientView.oos.close();
				JavaGameClientView.socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
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

	public static void GameStart() {
		// 타이머
		timebar = new JProgressBar();
		timebar.setForeground(new Color(153, 153, 204));
		timebar.setValue(10);
		timebar.setMaximum(10);
		timebar.setBounds(250, 15, 309, 30);
		contentPane.add(timebar);

		lblTime.setFont(new Font("배달의민족 도현", Font.PLAIN, 20));
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setBounds(200, 10, 50, 39);
		contentPane.add(lblTime);

		for (int i = 0; i < 4; i++) {
			lblUserReady[i].setVisible(false);
			lblScore[i].setVisible(true);
		}

		// 퀴즈 패널
		QuizPane.setBackground(Color.WHITE);
		QuizPane.setBounds(20, 70, 740, 200);
		QuizPane.setLayout(null);
		// 문제
		lblQuestion.setText("START");
		lblQuestion.setFont(new Font("배달의민족 도현", Font.PLAIN, 60));
		lblQuestion.setBounds(20, 70, 700, 70);
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		QuizPane.add(lblQuestion);

		// 객관식 버튼, ox버튼
		for (int i = 0; i < 4; i++) {
			btn_quizV[i] = new JButton(String.valueOf(i));
			// btn_quizV[i].setBackground(Color.WHITE);
			btn_quizV[i].setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
			if (i < 2) {
				btn_quizV[i].setBounds(30 + i * 360, 70, 315, 50);
			} else {
				btn_quizV[i].setBounds(30 + (i - 2) * 360, 130, 315, 50);
			}
			btn_quizV[i].addActionListener(new ActionListener() { //이벤트 만들어서 넣으면 오류남
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // TODO Auto-generated method stub
	            	ans = e.getActionCommand();
	            }
	        });
			QuizPane.add(btn_quizV[i]);

			btn_quizV[i].setVisible(false);
		}
		for (int i = 0; i < 2; i++) {
			if (i == 0)
				btn_OX[i] = new JButton("O");
			if (i == 1)
				btn_OX[i] = new JButton("X");
			btn_OX[i].setFont(new Font("나눔스퀘어", Font.PLAIN, 60));
			btn_OX[i].setBounds(30 + i * 360, 70, 315, 100);
			QuizPane.add(btn_OX[i]);
			btn_OX[i].addActionListener(new ActionListener() { //이벤트 만들어서 넣으면 오류남
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // TODO Auto-generated method stub
	            	ans = e.getActionCommand();
	            }
	        });
			btn_OX[i].setVisible(false);
		}

		contentPane.add(QuizPane);
		contentPane.add(topPane); // 이걸 안하면 타이머가 topPane보다 밑에 위치하여 안보이는 것 같음

	}
	public static void getPlayerSeq(String user) {
		for(int i = 0; i< 4; i++) {
			if(lblUserName[i].getText().equals(user)) {
				lblScore[i].setText(Integer.valueOf(lblScore[i].getText())+1+"");
				ChatMsg cm = new ChatMsg(username, "700", String.valueOf(i)); //스코어 새로고침
				JavaGameClientView.SendObject(cm);
				break;
			}
		}
		
	}
	public static void GameOver() {
//		QuizPane.setBackground(Color.WHITE);
//		QuizPane.setBounds(20, 70, 740, 200);
//		QuizPane.setLayout(null);
//		lblQuestion.setText("게임 결과");
//		lblQuestion.setFont(new Font("배달의민족 도현", Font.PLAIN, 18));
//		lblQuestion.setBounds(20, 25, 700, 30);
//		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
//		QuizPane.add(lblQuestion);
		for (int i = 0; i < 4; i++) {
			btn_quizV[i].setVisible(false); 
			if(i < 2)btn_OX[i].setVisible(false);
			rank[i] = new JLabel("dsdafas");
			rank[i].setFont(new Font("나눔스퀘어", Font.BOLD, 20));
			rank[i].setHorizontalAlignment(SwingConstants.CENTER);
			rank[i].setBounds(100, 60+i*30, 700, 30);
			
			QuizPane.add(rank[i]);
			rank[i].setVisible(true);
			if(lblUserName[i].getText().equals(username)) {
				ChatMsg cm = new ChatMsg(username, "750", lblScore[i].getText()); //스코어 새로고침
				JavaGameClientView.SendObject(cm);
			}
			
		}
		contentPane.add(QuizPane);
	}
}