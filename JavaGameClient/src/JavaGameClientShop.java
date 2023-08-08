
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Choice;

public class JavaGameClientShop extends JFrame {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private String UserName;

	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	

	public static JButton btnPurchase = new JButton("구입");
	public static JButton btnPurchase2 = new JButton("\uAD6C\uC785");
	public static JButton btnPurchase3 = new JButton("\uAD6C\uC785");
	public static JButton btnPurchase4 = new JButton("\uAD6C\uC785");
	public static JButton btnPurchase5 = new JButton("\uAD6C\uC785");
	public static JButton btnPurchase6 = new JButton("\uAD6C\uC785");
	public static JButton btnPurchase7 = new JButton("\uAD6C\uC785");
	public static JButton btnPurchase8 = new JButton("\uAD6C\uC785");

	public static JButton btnChar = new JButton("Char1");
	public static JButton btnChar2 = new JButton("Char2");
	public static JButton btnChar3 = new JButton("Char3");
	public static JButton btnChar4 = new JButton("Char4");
	public static JButton btnChar5 = new JButton("Char5");
	public static JButton btnChar6 = new JButton("Char6");
	public static JButton btnChar7 = new JButton("Char7");
	public static JButton btnChar8 = new JButton("Char8");

	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public JavaGameClientShop(String UserName) {
		this.UserName = UserName;

		initWindow();
	}

	public void initWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 540);

		ImageIcon face0;
		face0 = new ImageIcon("src/Background.png");
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face0.getImage(), 0, 0, d.width, d.height, this);

				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnChar.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar.setBackground(Color.WHITE);
		btnChar.setBounds(104, 245, 91, 23);
		contentPane.add(btnChar);
		btnChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "ChangeCharacter");
				cm.UserToon = "src/Character.gif";
				// ImageIcon character = new ImageIcon(cm.UserToon);
				// JavaGameClientView.lblCharacter.setIcon(character);
				SendObject(cm);
			}
		});
		btnChar2.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar2.setBackground(Color.WHITE);
		btnChar2.setBounds(320, 245, 91, 23);
		contentPane.add(btnChar2);
		btnChar2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character2.gif";
				// ImageIcon character = new ImageIcon(cm.Character);
				// JavaGameClientView.lblCharacter.setIcon(character);
				SendObject(cm);
			}
		});
		btnChar3.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar3.setBackground(Color.WHITE);
		btnChar3.setBounds(540, 245, 91, 23);
		contentPane.add(btnChar3);
		btnChar3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character3.gif";
				// ImageIcon character = new ImageIcon(cm.Character);
				// JavaGameClientView.lblCharacter.setIcon(character);
				SendObject(cm);
			}
		});
		btnChar4.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar4.setBackground(Color.WHITE);
		btnChar4.setBounds(756, 245, 91, 23);
		contentPane.add(btnChar4);
		btnChar4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character4.gif";
				SendObject(cm);
			}
		});
		btnChar5.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar5.setBackground(Color.WHITE);
		btnChar5.setBounds(103, 452, 91, 23);
		contentPane.add(btnChar5);
		btnChar5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character5.gif";
				SendObject(cm);
			}
		});
		btnChar6.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar6.setBackground(Color.WHITE);
		btnChar6.setBounds(320, 452, 91, 23);
		contentPane.add(btnChar6);
		btnChar6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character6.gif";
				SendObject(cm);
			}
		});
		btnChar7.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar7.setBackground(Color.WHITE);
		btnChar7.setBounds(540, 452, 91, 23);
		contentPane.add(btnChar7);
		btnChar7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character7.gif";
				SendObject(cm);
			}
		});
		btnChar8.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		btnChar8.setBackground(Color.WHITE);
		btnChar8.setBounds(756, 452, 91, 23);
		contentPane.add(btnChar8);
		btnChar8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "550", "Char");
				cm.UserToon = "src/Character8.gif";
				SendObject(cm);
			}
		});

		ImageIcon face;
		face = new ImageIcon("src/Character.gif");
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel.setBounds(104, 78, 90, 90);
		contentPane.add(panel);

		ImageIcon face2;
		face2 = new ImageIcon("src/Character2.gif");
		JPanel panel2 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face2.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel2.setBounds(321, 78, 90, 90);
		contentPane.add(panel2);

		ImageIcon face3;
		face3 = new ImageIcon("src/Character3.gif");
		JPanel panel3 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face3.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel3.setBounds(541, 78, 90, 90);
		contentPane.add(panel3);

		ImageIcon face4;
		face4 = new ImageIcon("src/Character4.gif");
		JPanel panel4 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face4.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel4.setBounds(757, 78, 90, 90);
		contentPane.add(panel4);

		ImageIcon face5;
		face5 = new ImageIcon("src/Character5.gif");
		JPanel panel5 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face5.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel5.setBounds(104, 284, 90, 90);
		contentPane.add(panel5);

		ImageIcon face6;
		face6 = new ImageIcon("src/Character6.gif");
		JPanel panel6 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face6.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel6.setBounds(320, 284, 90, 90);
		contentPane.add(panel6);

		ImageIcon face7;
		face7 = new ImageIcon("src/Character7.gif");
		JPanel panel7 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face7.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel7.setBounds(541, 284, 90, 90);
		contentPane.add(panel7);

		ImageIcon face8;
		face8 = new ImageIcon("src/Character8.gif");
		JPanel panel8 = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(face8.getImage(), 0, 0, d.width, d.height, this);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		panel8.setBounds(756, 284, 90, 90);
		contentPane.add(panel8);

		JButton btnNewButton = new JButton("->");
		btnNewButton.setBounds(853, 10, 91, 44);
		contentPane.add(btnNewButton);

		btnPurchase.setBounds(104, 210, 91, 23);
		contentPane.add(btnPurchase);
		
		JLabel lblNewLabel = new JLabel("코인 10개");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(104, 189, 91, 23);
		contentPane.add(lblNewLabel);
		
		btnPurchase2.setBounds(320, 212, 91, 23);
		contentPane.add(btnPurchase2);
		
		btnPurchase3.setBounds(540, 212, 91, 23);
		contentPane.add(btnPurchase3);

		btnPurchase4.setBounds(756, 212, 91, 23);
		contentPane.add(btnPurchase4);
		
		btnPurchase5.setBounds(104, 419, 91, 23);
		contentPane.add(btnPurchase5);
		
		btnPurchase6.setBounds(320, 419, 91, 23);
		contentPane.add(btnPurchase6);
		
		btnPurchase7.setBounds(540, 419, 91, 23);
		contentPane.add(btnPurchase7);

		btnPurchase8.setBounds(756, 419, 91, 23);
		contentPane.add(btnPurchase8);
		
		JLabel lblNewLabel_1 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(320, 189, 91, 23);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(540, 189, 91, 23);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setBounds(756, 193, 91, 23);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(104, 395, 91, 23);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setBounds(320, 399, 91, 23);
		contentPane.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1_1.setBounds(540, 399, 91, 23);
		contentPane.add(lblNewLabel_2_1_1);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("\uCF54\uC778 10\uAC1C");
		lblNewLabel_2_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1_1_1.setBounds(756, 399, 91, 23);
		contentPane.add(lblNewLabel_2_1_1_1);

		BackBtn back = new BackBtn();
		btnNewButton.addActionListener(back);
	
		PurchaseBtn pB = new PurchaseBtn();
		btnPurchase.addActionListener(pB);
		btnPurchase2.addActionListener(pB);
		btnPurchase3.addActionListener(pB);
		btnPurchase4.addActionListener(pB);
		btnPurchase5.addActionListener(pB);
		btnPurchase6.addActionListener(pB);
		btnPurchase7.addActionListener(pB);
		btnPurchase8.addActionListener(pB);
		
		btnChar.setVisible(false);
		btnChar2.setVisible(false);
		btnChar3.setVisible(false);
		btnChar4.setVisible(false);
		btnChar5.setVisible(false);
		btnChar6.setVisible(false);
		btnChar7.setVisible(false);
		btnChar8.setVisible(false);
		
		setVisible(true);
	}

	class PurchaseBtn implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			ChatMsg obcm = new ChatMsg(UserName, "900", "purchase Toon");
			
			if(e.getSource() == btnPurchase)
				obcm.ToonBtn = "btnChar";
			else if(e.getSource() == btnPurchase2)
				obcm.ToonBtn = "btnChar2";
			else if(e.getSource() == btnPurchase3)
				obcm.ToonBtn = "btnChar3";
			else if(e.getSource() == btnPurchase4)
				obcm.ToonBtn = "btnChar4";
			else if(e.getSource() == btnPurchase5)
				obcm.ToonBtn = "btnChar5";
			else if(e.getSource() == btnPurchase6)
				obcm.ToonBtn = "btnChar6";
			else if(e.getSource() == btnPurchase7)
				obcm.ToonBtn = "btnChar7";
			else if(e.getSource() == btnPurchase8)
				obcm.ToonBtn = "btnChar8";
			obcm.ToonPrice = 10;
			SendObject(obcm);
		}
		
	}
	
	// 카트 버튼 눌렀을 경우
	class BackBtn implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			JavaGameClientView.oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			JavaGameClientView.oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}
}