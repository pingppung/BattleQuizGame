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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



public class JavaGameClientRoom extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel topPane = new JPanel();
	private static JButton btn_Exit = new JButton("Back");
	
	private static String username;
	private static String character;
	
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public static JLabel[] lblUserName= new JLabel[4];
	public static JLabel[] lblUserCharacter= new JLabel[4];
	
	
	public static class Player{
		int port;
		String name;
		String character;
		int roomId;
		Player(int port, String name, String character, int roomId){
			this.port = port;
			this.name = name;
			this.character = character;
			this.roomId = roomId;
		}
	}
	
	
	//private static Player[] PlayerList = new Player[4];
	//public static ArrayList<Player> PlayerList = new ArrayList<>();
	public JavaGameClientRoom(String username, String character) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.character = character;
		initWindow();
	}
	private void initWindow() {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 580);
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
       
     //나가기=뒤로가기 버튼
       btn_Exit.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
       btn_Exit.setBackground(Color.WHITE);
       btn_Exit.setBounds(850, 12, 100, 35);
       contentPane.add(btn_Exit);
       
       //상단 패널
       topPane.setBackground(new Color(204, 204, 255));
       topPane.setBounds(0, 0, 1101, 55);
       contentPane.add(topPane);
		
       
       

       //본인 닉네임 PlayList에 저장 && 화면에 적용
       for(int i = 0; i < 4; i++) {
    	  lblUserName[i] = new JLabel("");
    	  //lblUserName[i].setText("");
    	  lblUserName[i].setBackground(Color.BLACK);
    	  lblUserName[i].setForeground(Color.WHITE);
    	  lblUserName[i].setFont(new Font("배달의민족 주아", Font.BOLD, 20));
    	  lblUserName[i].setHorizontalAlignment(SwingConstants.CENTER);
    	  lblUserName[i].setBounds(61 + i * 180, 422, 121, 40);
    	   
    	  lblUserCharacter[i] = new JLabel("");
    	  //lblUserCharacter[i].setIcon(new ImageIcon(PlayerList.get(i).character));
    	   //PlayerList[i].lblCharacter.setIcon(new ImageIcon(this.character));
    	  //(가로위치, 세로위치, 가로길이, 세로길이);
    	  lblUserCharacter[i].setBounds(61 + i * 180, 285, 121, 127);
    	  lblUserCharacter[i].setHorizontalAlignment(SwingConstants.CENTER);
    	   
    	   contentPane.add(lblUserName[i]);
    	   contentPane.add(lblUserCharacter[i]);
       }
       
       
       setVisible(true);
       
       RoomExitButtonClick action_exit = new RoomExitButtonClick();
       btn_Exit.addActionListener(action_exit);
	}
	
	// Player가 게임방 나가기 버튼 눌렀을 때
	class RoomExitButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg cm = new ChatMsg(username, "600", "RoomExit");
			JavaGameClientView.SendObject(cm);
		}
	}
	
}