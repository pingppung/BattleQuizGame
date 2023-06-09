package GamePkg;
// JavaObjClient.java
// ObjecStream 사용하는 채팅 Client

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JavaGameClientMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public JavaGameClientMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 540);
		ImageIcon face =  new ImageIcon("src/images/MainBG.png");
		contentPane = new JPanel() {
			 public void paintComponent(Graphics g) {
	               Dimension d = getSize();
	               g.drawImage(face.getImage(), 0, 0, d.width, d.height, this);
	               
	               setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	               super.paintComponent(g);
			 }
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//logo
        ImageIcon logo = new ImageIcon("src/images/quiz.gif");
             
         JPanel logo_panel = new JPanel() {
           public void paintComponent(Graphics g) {
              Dimension d = getSize();
              g.drawImage(logo.getImage(), 0, 0, d.width, d.height, this);
              
              setOpaque(false); //그림을 표시하게 설정,투명하게 조절
              super.paintComponent(g);
           }
        };
        logo_panel.setBounds(250, 100, 200, 150);
        contentPane.add(logo_panel);
		
		//username
		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setBackground(Color.WHITE);
		//lblNewLabel.setFont(new Font("나눔스퀘어", Font.BOLD, 10));
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(251, 287, 63, 24);
		contentPane.add(lblNewLabel);
		
		txtUserName = new JTextField();
		txtUserName.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		txtUserName.setBackground(Color.WHITE);
		txtUserName.setHorizontalAlignment(SwingConstants.RIGHT);
		txtUserName.setBounds(318, 288, 120, 24);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		
		//ip
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBackground(Color.WHITE);
		lblIpAddress.setForeground(Color.BLACK);
		lblIpAddress.setBounds(251, 311, 63, 24);
		contentPane.add(lblIpAddress);
		
		txtIpAddress = new JTextField();
        txtIpAddress.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
        txtIpAddress.setBackground(Color.WHITE);
        txtIpAddress.setHorizontalAlignment(SwingConstants.RIGHT);
        txtIpAddress.setText("127.0.0.1");
        txtIpAddress.setColumns(10);
        txtIpAddress.setBounds(318, 312, 120, 24);
        contentPane.add(txtIpAddress);
		
		
		//port
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setForeground(Color.BLACK);
        lblPortNumber.setBounds(251, 336, 63, 24);
		contentPane.add(lblPortNumber);
		
		txtPortNumber = new JTextField();
        txtPortNumber.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
        txtPortNumber.setBackground(Color.WHITE);
        txtPortNumber.setText("30000");
        txtPortNumber.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPortNumber.setColumns(10);
        txtPortNumber.setBounds(318, 336, 120, 24);
        contentPane.add(txtPortNumber);
        
        //connect BTN
		JButton btnConnect = new JButton("Start");
		btnConnect.setBackground(Color.WHITE);
        btnConnect.setForeground(Color.BLACK);
        btnConnect.setBounds(307, 389, 83, 24);
        contentPane.add(btnConnect);
		
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		
	}
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
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


