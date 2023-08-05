package GamePkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class JavaGameClientShop extends JFrame{
	private String username;
	private JPanel contentPane;
	
	public static JButton[] btnSelect = new JButton[8];
	public static JLabel[] character = new JLabel[8];
	//private String character = "src/images/Character.gif"; // 기본 캐릭터 지정
	
	public JavaGameClientShop(String name) {
		this.username = name;
		
		initWindow();
	}
	public void initWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 960, 540);
		
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
		
		
		for(int i = 0; i < 8; i++) {
			btnSelect[i] = new JButton("구매");
			character[i] = new JLabel("");
			character[i].setForeground(Color.blue);
			
			btnSelect[i].setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
			
			String icon = "src/images/Character"+ (i+1) + ".png";
			ImageIcon image_icon = new ImageIcon(icon);
			
			character[i].setIcon(image_icon);
			
			
			if(i < 4) {//위 4개 아래 4개
				character[i].setBounds(100+i*220, 100, 100, 100);
				btnSelect[i].setBounds(100+i*220, 210, 100, 25);
			} else  {
				character[i].setBounds(100+(i-4)*220, 280, 100, 100);
				btnSelect[i].setBounds(100+(i-4)*220, 390, 100, 25);
			}
//			btnSelect[i].putClientProperty("id", (i+1));
//			Object property = btnSelect[i].getClientProperty("id");
			btnSelect[i].setName(String.valueOf(i));
			
			
			SelectButtonClick action_select = new SelectButtonClick();
			btnSelect[i].addActionListener(action_select);
			
			contentPane.add(character[i]);
			contentPane.add(btnSelect[i]);
		}
		setVisible(true);
	}
	
	class SelectButtonClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton o = (JButton)e.getSource();
		//	JavaGameClientView.AppendText(o.getName());
			ChatMsg obcm = new ChatMsg(username, "300", o.getName());
			JavaGameClientView.SendObject(obcm);
		}
	}
}
