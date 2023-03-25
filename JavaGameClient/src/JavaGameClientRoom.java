import java.awt.Dimension;
import java.awt.Graphics;
import java.io.*;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class JavaGameClientRoom extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static String username;
	private static String character;
	
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public JavaGameClientRoom(String username, String character) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.character = character;
		initWindow();
	}
	private void initWindow() {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1105, 580);
		ImageIcon bg = new ImageIcon("src/images/RoomBG.gif");
        contentPane = new JPanel() {
          public void paintComponent(Graphics g) {
             Dimension d = getSize();
             g.drawImage(bg.getImage(), 0, 0, d.width, d.height, this);
             setOpaque(false); //그림을 표시하게 설정,투명하게 조절
             super.paintComponent(g);
          }
       };
       contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
       setContentPane(contentPane);
       contentPane.setLayout(null);
       setVisible(true);
	}
}