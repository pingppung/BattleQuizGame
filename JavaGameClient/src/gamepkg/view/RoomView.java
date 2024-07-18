package gamepkg.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gamepkg.GameManager;
import gamepkg.network.ClientSocketHandler;
import gamepkg.state.ClientState;
import gamepkg.util.ChatMsg;
import gamepkg.util.ComponentFactory;

public class RoomView extends JFrame {
	private static String username;
	public static String character;

	public static String ans = "";

	public static JLabel lblQuestion = new JLabel();

	public JPanel contentPane;
	public JPanel topPane;

	public JScrollPane chatPane;
	private JTextField txtInput; // 채팅 입력
	private JButton btn_Send;

	public JLabel[] lblUserName = new JLabel[4];
	public JLabel[] lblUserCharacter = new JLabel[4];

	public JButton btn_Ready;
	public JLabel[] lblUserReady = new JLabel[4];
	public JButton btn_Exit;

	public JLabel[] lblScore = new JLabel[4]; // 각 플레이어 점수

	public JProgressBar timebar;
	public JLabel lblTime;

	public JPanel QuizPane = new JPanel();
	public JButton[] btn_quizV = new JButton[4];
	public JButton[] btn_OX = new JButton[2];

	public JLabel[] rank = new JLabel[4];

	private ClientSocketHandler socketHandler;

	public RoomView(String username, String character, ClientSocketHandler socketHandler) {
		this.username = username;
		this.character = character;
		this.socketHandler = socketHandler;

		initWindow();
		// GameOver();

		socketHandler.setGameRoom(this);
		socketHandler.stateManager.setState(ClientState.GAME_ROOM);
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

		// 배경 패널 설정
		contentPane = ComponentFactory.createImagePanel("src/images/RoomBG.gif");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // 배치 관리자 없이 수동으로 위치 조정

		// 상단 패널 설정
		topPane = ComponentFactory.createColorPanel(new Color(204, 204, 255), 0, 0, 1101, 55);
		topPane.setLayout(null);
		contentPane.add(topPane);

		// 채팅 관련 컴포넌트 설정
		chatPane = ComponentFactory.createScrollPanel(770, 70, 295, 380);
		contentPane.add(chatPane);

		// 입력 필드
		txtInput = ComponentFactory.createTextField("나눔스퀘어", 770, 475, 215, 40, "");
		contentPane.add(txtInput);

		// 전송 버튼
		btn_Send = ComponentFactory.createTextButton("전송", 990, 475, 75, 40);
		contentPane.add(btn_Send);

		// 준비 버튼
		btn_Ready = ComponentFactory.createTextButton("준비완료", 670, 15, 100, 30);
		topPane.add(btn_Ready);

		// 나가기 버튼
		btn_Exit = ComponentFactory.createTextButton("나가기", 965, 12, 100, 35);
		topPane.add(btn_Exit);

		// 본인 닉네임 PlayList에 저장 && 화면에 적용
		for (int i = 0; i < 4; i++) {
			// 사용자 이름 라벨 설정
			lblUserName[i] = ComponentFactory.createLabel("", 61 + i * 180, 422, 121, 40); // 이름 라벨 생성
			lblUserName[i].setFont(new Font("배달의민족 주아", Font.BOLD, 20));

			lblUserName[i].setHorizontalAlignment(SwingConstants.CENTER);
			contentPane.add(lblUserName[i]);

			// 사용자 캐릭터 이미지 라벨 설정
			lblUserCharacter[i] = ComponentFactory.createLabel("", 61 + i * 180, 285, 121, 127); // 캐릭터 이미지 라벨 생성
			contentPane.add(lblUserCharacter[i]);

			// 사용자 준비 상태 라벨 설정
			lblUserReady[i] = ComponentFactory.createLabel("준비", 61 + i * 180, 485, 121, 40); // 준비 상태 라벨 생성
			lblUserReady[i].setFont(new Font("배달의민족 도현", Font.BOLD, 20)); // 폰트 설정
			lblUserReady[i].setVisible(false); // 초기에는 보이지 않도록 설정
			lblUserReady[i].setForeground(Color.WHITE);
			contentPane.add(lblUserReady[i]);

			// 점수 라벨 설정
			lblScore[i] = ComponentFactory.createLabel("0", 61 + i * 180, 485, 121, 40); // 점수 라벨 생성
			lblScore[i].setVisible(false); // 초기에는 보이지 않도록 설정
			lblScore[i].setFont(new Font("배달의민족 도현", Font.BOLD, 20)); // 폰트 설정
			lblUserReady[i].setForeground(Color.WHITE);
			contentPane.add(lblScore[i]); // 배경 패널에 점수 라벨 추가
		}
		// setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // x버튼 눌러도 반응없게
		setVisible(true);

	}

	public void GameStart() {
		GameManager gameManager = new GameManager(this);
		gameManager.GameStart();
	}

	public void getPlayerSeq(String user) {
		for (int i = 0; i < 4; i++) {
			if (lblUserName[i].getText().equals(user)) {
				lblScore[i].setText(Integer.valueOf(lblScore[i].getText()) + 1 + "");
				ChatMsg cm = new ChatMsg(username, "700", String.valueOf(i)); // 스코어 새로고침
				socketHandler.SendObject(cm);
				break;
			}
		}

	}

	public void GameOver() {
		for (int i = 0; i < 4; i++) {
			btn_quizV[i].setVisible(false);
			if (i < 2)
				btn_OX[i].setVisible(false);
			rank[i] = new JLabel("오류나서 결과 안나옴");
			rank[i].setFont(new Font("나눔스퀘어", Font.BOLD, 20));
			rank[i].setHorizontalAlignment(SwingConstants.LEFT);
			rank[i].setBounds(100, 60 + i * 30, 700, 30);

			QuizPane.add(rank[i]);
			rank[i].setVisible(true);
			if (lblUserName[i].getText().equals(username)) {
				ChatMsg cm = new ChatMsg(username, "750", lblScore[i].getText()); // 스코어 새로고침
				socketHandler.SendObject(cm);
			}

		}
		contentPane.add(QuizPane);
	}

	public void Restart() {
		QuizPane.setVisible(false);
		btn_Ready.setText("준비완료");
		btn_Ready.setVisible(true);
		btn_Exit.setVisible(true);
		timebar.setVisible(false);
		lblTime.setVisible(false);
		for (int i = 0; i < 4; i++) {
			rank[i].setVisible(false);

			lblScore[i].setText("0");
			lblScore[i].setVisible(false);

			lblUserReady[i].setVisible(false);

		}
	}

	// Player가 게임방에서 레디 or 대기 버튼 눌렀을때
	class ReadyButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(username);
			ChatMsg obcm = new ChatMsg(username, "550", btn_Ready.getText()); // ready변경사항 서버에 보내기 (다른 유저들 화면에서 띄우게)
			if (btn_Ready.getText().equals("준비완료")) { // 대기 -> 레디 상태
				btn_Ready.setText("준비취소");
			} else { // 레디 -> 대기 상태
				btn_Ready.setText("준비완료");
			}
			socketHandler.SendObject(obcm);
		}
	}

	// Player가 게임방 나가기 버튼 눌렀을 때
	class RoomExitButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(username, "400", "RoomExit");
			socketHandler.SendObject(cm);
			// 게임방에서 나가니깐 로비로 설정해주기
			socketHandler.stateManager.setState(ClientState.LOBBY);
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
				msg = txtInput.getText();
				socketHandler.SendMessage(username, "250", msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

}