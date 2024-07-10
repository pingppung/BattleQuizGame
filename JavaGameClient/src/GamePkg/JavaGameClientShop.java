package gamepkg;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JavaGameClientShop extends JFrame {

	public static JButton[] btnSelect = new JButton[8];
	
	private String username;
	private JPanel contentPane;

	public JLabel[] character = new JLabel[8];

	public JButton btn_Exit = new JButton("EXIT");
	private NetworkListener networkListener;

	public JavaGameClientShop(String name, NetworkListener networkListener) {
		this.username = name;
		this.networkListener = networkListener;
		initWindow();

		ExitButtonClick back = new ExitButtonClick();
		btn_Exit.addActionListener(back);
	}

	public void initWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 960, 540);

		contentPane = ComponentFactory.createImagePanel("src/images/MainBG.png");
		setContentPane(contentPane);
		contentPane.setLayout(null);

		for (int i = 0; i < 8; i++) {
			character[i] = ComponentFactory.createLabel("", 0, 0, 100, 100);
			String iconPath = "src/images/Character" + (i + 1) + ".png";
			ImageIcon icon = new ImageIcon(iconPath);
			character[i].setIcon(icon);

			btnSelect[i] = ComponentFactory.createTextButton("구매", 0, 0, 100, 25);
			btnSelect[i].setName(String.valueOf(i));

			if (i < 4) {// 위 4개 아래 4개
				character[i].setBounds(100 + i * 220, 100, 100, 100);
				btnSelect[i].setBounds(100 + i * 220, 210, 100, 25);
			} else {
				character[i].setBounds(100 + (i - 4) * 220, 280, 100, 100);
				btnSelect[i].setBounds(100 + (i - 4) * 220, 390, 100, 25);
			}
			btnSelect[i].addActionListener(new SelectButtonClick());
			contentPane.add(character[i]);
			contentPane.add(btnSelect[i]);
		}

		btn_Exit.setBounds(830, 10, 100, 30);
		contentPane.add(btn_Exit);
		setVisible(true);
	}

	class SelectButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton o = (JButton) e.getSource();
			ChatMsg obcm = null;
			if (e.getActionCommand().equals("선택")) { // 캐릭터 변경 - 변경할 떄는 변경할 캐릭터 경로 보내기
				int idx = Integer.valueOf(o.getName());
				String route = "src/images/Character" + (idx + 1) + ".png";
				ImageIcon update_icon = new ImageIcon(route);
				JavaGameClientView.lblCharacter.setIcon(update_icon);

				obcm = new ChatMsg(username, "300", route);

			} else if (e.getActionCommand().equals("구매")) {// 캐릭터 구매 - 구매할 떄는 구매할 캐릭터 번호 cm.data로 보내기
				obcm = new ChatMsg(username, "300", o.getName());

			}
			networkListener.SendObject(obcm);

		}
	}

	class ExitButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
