package gamepkg;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// JavaObjClient.java
// ObjecStream 사용하는 채팅 Client
public class JavaGameClientMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaGameClientMain frame = new JavaGameClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JavaGameClientMain() {
		initialize();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 540);
		setTitle("Java Game Client");

		contentPane = ComponentFactory.createImagePanel("src/images/MainBG.png");
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel logoPanel = ComponentFactory.createImagePanel("src/images/quiz.gif");
		logoPanel.setBounds(250, 100, 200, 150);
		contentPane.add(logoPanel);

		JLabel lblUserName = ComponentFactory.createLabel("User Name", 251, 287, 63, 24);
		contentPane.add(lblUserName);
		txtUserName = ComponentFactory.createTextField("나눔스퀘어", 318, 288, 120, 24, "");
		contentPane.add(txtUserName);

		JLabel lblIpAddress = ComponentFactory.createLabel("IP Address", 251, 311, 63, 24);
		contentPane.add(lblIpAddress);
		txtIpAddress = ComponentFactory.createTextField("나눔스퀘어", 318, 312, 120, 24, "127.0.0.1");
		contentPane.add(txtIpAddress);

		JLabel lblPortNumber = ComponentFactory.createLabel("Port Number", 251, 336, 63, 24);
		contentPane.add(lblPortNumber);
		txtPortNumber = ComponentFactory.createTextField("나눔스퀘어", 318, 336, 120, 24, "30000");
		contentPane.add(txtPortNumber);

		JButton btnConnect = ComponentFactory.createTextButton("Start", 307, 389, 83, 24);
		contentPane.add(btnConnect);

		btnConnect.addActionListener(new ConnectButtonAction());
	}

	class ConnectButtonAction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = txtUserName.getText().trim();
			String ip_addr = txtIpAddress.getText().trim();
			String port_no = txtPortNumber.getText().trim();
			JavaGameClientView view = new JavaGameClientView(username, ip_addr, port_no);
			setVisible(false);
		}
	}
}
