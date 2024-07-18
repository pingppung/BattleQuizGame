package gamepkg.view;

// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import gamepkg.util.ChatMsg;
import gamepkg.util.ComponentFactory;
import gamepkg.network.ClientSocketHandler;
import gamepkg.state.ClientState;

public class MainView extends JFrame {

	public static JLabel lblCharacter;
	public static JLabel lblCoin;
	public JScrollPane chatPane;
	private String character = "src/images/Character1.png"; // 기본 캐릭터 지정
	private String username;
	private JPanel contentPane;
	private JLabel lblUserName;
	private JPanel topPane;
	private JButton btnShop;
	private JTextField txtInput;

	private ClientSocketHandler socketHandler;

	public MainView(String username, String ip_addr, String port_no) {
		this.username = username;
		initWindow();
		try {
			// 서버에 연결
			socketHandler = new ClientSocketHandler(ip_addr, port_no);
			socketHandler.start();

			// 프로토콜 : 100 -> 서버 (로그인)
			ChatMsg obcm = new ChatMsg(username, "100", "Hello");
			obcm.character = character;
			socketHandler.SendObject(obcm);

			socketHandler.setGameRobby(this);
			socketHandler.stateManager.setState(ClientState.LOBBY);

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			socketHandler.textHandler.appendText("connect error");
		}

	}

	private void initWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 540);

		contentPane = ComponentFactory.createImagePanel("src/images/ViewBG.gif");
		setContentPane(contentPane);
		contentPane.setLayout(null);

		topPane = ComponentFactory.createColorPanel(new Color(255, 182, 193), 0, 0, 700, 55);
		contentPane.add(topPane);
		topPane.setLayout(null);

		lblCoin = ComponentFactory.createLabel("10", 10, 20, 100, 15);
		lblCoin.setHorizontalAlignment(SwingConstants.LEFT);
		topPane.add(lblCoin);

		btnShop = ComponentFactory.createTextButton("Shop", 550, 8, 105, 37);
		topPane.add(btnShop);

		chatPane = ComponentFactory.createScrollPanel(12, 345, 375, 109);
		contentPane.add(chatPane);

		txtInput = ComponentFactory.createTextField("나눔스퀘어", 12, 464, 375, 30, "");
		contentPane.add(txtInput);

		// 기본 캐릭터 설정
		lblCharacter = ComponentFactory.createLabel("", 141, 117, 90, 90);
		lblCharacter.setIcon(new ImageIcon(character));
		lblCharacter.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblCharacter);

		lblUserName = ComponentFactory.createLabel(username, 172, 217, 152, 36);
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		contentPane.add(lblUserName);

		ImageIcon normalIcon = new ImageIcon("src/images/btnRoom.png");
		ImageIcon rolloverIcon = new ImageIcon("src/images/btnRoom2.png");
		ImageIcon pressedIcon = new ImageIcon("src/images/btnRoom2.png");
		JButton btnEntry = ComponentFactory.createImageButton(normalIcon, rolloverIcon, pressedIcon, 534, 373, 152, 57);
		contentPane.add(btnEntry);

		setVisible(true);

		TextSendAction action = new TextSendAction();
		txtInput.addActionListener(action);
		txtInput.requestFocus();

		RoomEntryButtonClick action_entry = new RoomEntryButtonClick();
		btnEntry.addActionListener(action_entry);

		ShopEntryButtonClick action_shop = new ShopEntryButtonClick();
		btnShop.addActionListener(action_shop);

	}

	class RoomEntryButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 문제점 : 대기방에 여러창 띄워놓은 다음 게임방에 들어갈때 오류남
			RoomView room = new RoomView(username, character, socketHandler);
			setVisible(false);
			ChatMsg obcm = new ChatMsg(username, "500", "Entry");
			obcm.coin = Integer.valueOf(lblCoin.getText());
			obcm.character = character;
			socketHandler.SendObject(obcm);

		}
	}

	class ShopEntryButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			ShopView shop = new ShopView(username, socketHandler);
			ChatMsg obcm = new ChatMsg(username, "300", "Shop");
			socketHandler.SendObject(obcm);
			// setVisible(false);
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
				socketHandler.SendMessage(username, "200", msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}
}
