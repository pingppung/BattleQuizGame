//JavaObjServer.java ObjectStream ��� ä�� Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaGameServer extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����

	// ���ӹ濡 ���� player �̸� �����Ű�� ����
	private static String[] PlayerName = new String[4];
	// ������� ������ ���� �����ϴ� ����
	private static String[] sortPlayerName = new String[4];
	private static String[] PlayerCharacter = new String[4];
	private int RoomEntryPlayerNum = 0;
	private static String[] PlayerReady = new String[4];
	private int ReadyNum = 0;
	private int QuizType; // ���� ����
	private int multiNum = 1; // ������ ������ȣ - �ߺ��� ��츦 ����ؼ� �ϴ� ���ʴ�� ���� ������
	private int oxNum = 1; // ox ���� ��ȣ - �ߺ��� ��츦 ����ؼ� �ϴ� ���ʴ�� ���� ������
	private int[] RealAnswer = new int[4];
	private int answ;
	private int[] score = new int[4];
	private int[] sortscore = new int[4];

	private long startTime = 0;
	private long endTime = 0;

	private Timer m_timer;
	private TimerTask m_task;

	private int ShopEntryPlayerNum = 0;
	// �α����� �÷��̾� �� �����Ű�� ����
	private String[] LoginPlayer = new String[4];
	private int LoginPlayerNum = 0;

	private int[] PlayerCoin = new int[4];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaGameServer frame = new JavaGameServer();
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
	public JavaGameServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String UserName = "";
		public String UserStatus;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// �Ű������� �Ѿ�� �ڷ� ����
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
//            is = client_socket.getInputStream();
//            dis = new DataInputStream(is);
//            os = client_socket.getOutputStream();
//            dos = new DataOutputStream(os);

				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

				// line1 = dis.readUTF();
				// /login user1 ==> msg[0] msg[1]
//            byte[] b = new byte[BUF_LEN];
//            dis.read(b);      
//            String line1 = new String(b);
//
//            //String[] msg = line1.split(" ");
//            //UserName = msg[1].trim();
//            UserStatus = "O"; // Online ����
//            Login();
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public synchronized void Login() {
			AppendText("���ο� ������ " + UserName + " ����.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(UserName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			WriteOthers(msg); // ���� user_vc�� ���� ������ user�� ���Ե��� �ʾҴ�.
		}

		public synchronized void Logout() {
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			UserVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			WriteAll(msg); // ���� ������ �ٸ� User�鿡�� ����
			AppendText("����� " + "[" + UserName + "] ����. ���� ������ �� " + UserVec.size());
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public synchronized void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public synchronized void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public synchronized void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
		public synchronized void WriteOne(String msg) {
			try {
				// dos.writeUTF(msg);
//            byte[] bb;
//            bb = MakePacket(msg);
//            dos.write(bb, 0, bb.length);
				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//               dos.close();
//               dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		// �ӼӸ� ����
		public synchronized void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		public synchronized void WriteOneObject(Object ob) {
			try {
				oos.writeObject(ob);
			} catch (IOException e) {
				AppendText("oos.writeObject(ob) error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}

		// �� �÷��̾� �� ����� ���� ó��
		public void Answer() {
			// ù��°�� ���ӹ濡 ���� �÷��̾�
			if (answ == RealAnswer[0] && answ != 0) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 0;
				score[0] += 100;
				obcm2.Score[0] = score[0];
				WriteAllObject(obcm2);
			} else if (answ != RealAnswer[0]) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 1;
				WriteAllObject(obcm2);
			} else if (answ == 0 && RealAnswer[0] == 0) {
				System.out.println("������ ���۵Ǿ����ϴ�.");
			}

			// �ι�°�� ���ӹ濡 ���� �÷��̾�
			if (answ == RealAnswer[1] && answ != 0) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 2;
				score[1] += 100;
				obcm2.Score[1] = score[1];
				WriteAllObject(obcm2);
			} else if (answ != RealAnswer[1]) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 3;
				WriteAllObject(obcm2);
			} else if (answ == 0 && RealAnswer[1] == 0) {
				System.out.println("������ ���۵Ǿ����ϴ�.");
			}

			// ����°�� ���ӹ濡 ���� �÷��̾�
			if (answ == RealAnswer[2] && answ != 0) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 4;
				score[2] += 100;
				obcm2.Score[2] = score[2];
				WriteAllObject(obcm2);
			} else if (answ != RealAnswer[2]) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 5;
				WriteAllObject(obcm2);
			} else if (answ == 0 && RealAnswer[2] == 0) {
				System.out.println("������ ���۵Ǿ����ϴ�.");
			}

			// �׹�°�� ���ӹ濡 ���� �÷��̾�
			if (answ == RealAnswer[3] && answ != 0) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 6;
				score[3] += 100;
				obcm2.Score[3] = score[3];
				WriteAllObject(obcm2);
			} else if (answ != RealAnswer[3]) {
				ChatMsg obcm2 = new ChatMsg("SEVER", "800", "Real Answer");
				obcm2.RealAnswer = 7;
				WriteAllObject(obcm2);
			} else if (answ == 0 && RealAnswer[3] == 0) {
				System.out.println("������ ���۵Ǿ����ϴ�.");
			}

		}

		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					// String msg = dis.readUTF();
//               byte[] b = new byte[BUF_LEN];
//               int ret;
//               ret = dis.read(b);
//               if (ret < 0) {
//                  AppendText("dis.read() < 0 error");
//                  try {
//                     dos.close();
//                     dis.close();
//                     client_socket.close();
//                     Logout();
//                     break;
//                  } catch (Exception ee) {
//                     break;
//                  } // catch�� ��
//               }
//               String msg = new String(b, "euc-kr");
//               msg = msg.trim(); // �յ� blank NULL, \n ��� ����
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.code.matches("100")) {
						UserName = cm.UserName;
						UserStatus = "O"; // Online ����
						Login();

						ChatMsg obcm2 = new ChatMsg("SEVER", "150", "PayCoin");
						LoginPlayer[LoginPlayerNum] = UserName;
						PlayerCoin[LoginPlayerNum] = 10;
						obcm2.LoginPlayer[0] = LoginPlayer[0];
						obcm2.LoginPlayer[1] = LoginPlayer[1];
						obcm2.LoginPlayer[2] = LoginPlayer[2];
						obcm2.LoginPlayer[3] = LoginPlayer[3];
						obcm2.PlayerCoin[0] = PlayerCoin[0];
						obcm2.PlayerCoin[1] = PlayerCoin[1];
						obcm2.PlayerCoin[2] = PlayerCoin[2];
						obcm2.PlayerCoin[3] = PlayerCoin[3];
						LoginPlayerNum++;
						obcm2.LoginPlayerNum = LoginPlayerNum;
						WriteAllObject(obcm2);

					} else if (cm.code.matches("200")) {
						msg = String.format("[%s] %s", cm.UserName, cm.data);
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
							UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
							}
							WriteOne("-----------------------------\n");
						} else if (args[1].matches("/sleep")) {
							UserStatus = "S";
						} else if (args[1].matches("/wakeup")) {
							UserStatus = "O";
						} else if (args[1].matches("/to")) { // �ӼӸ�
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// ���� message �κ�
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to ����.. [�ӼӸ�] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									// user.WriteOne("[�ӼӸ�] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // �Ϲ� ä�� �޽���
							UserStatus = "O";
							// WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.code.matches("250")) {
						msg = String.format("[%s] %s", cm.UserName, cm.data);
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
							UserStatus = "O";
						}
						WriteAllObject(cm);
					} else if (cm.code.matches("400")) { // logout message ó��
						Logout();
						break;
					} else if (cm.code.matches("500")) { // ���ӹ� ����

						if (RoomEntryPlayerNum < 4) {
							PlayerName[RoomEntryPlayerNum] = UserName;
							PlayerCharacter[RoomEntryPlayerNum] = cm.UserToon;
							RoomEntryPlayerNum++;
							cm.PlayerN[0] = PlayerName[0];
							cm.PlayerN[1] = PlayerName[1];
							cm.PlayerN[2] = PlayerName[2];
							cm.PlayerN[3] = PlayerName[3];
							cm.Character[0] = PlayerCharacter[0];
							cm.Character[1] = PlayerCharacter[1];
							cm.Character[2] = PlayerCharacter[2];
							cm.Character[3] = PlayerCharacter[3];
							cm.RoomEntryPlayerNum = RoomEntryPlayerNum;
							// data.playername = PlayerName[0];

							WriteAllObject(cm);

						} else {
							WriteOne("�����ʰ��Ǿ� ������ �� �����ϴ�");
						}
					} else if (cm.code.matches("550")) { // ĳ���� ���� �ѱ��
						AppendText("a");
						WriteAllObject(cm); // �ٲٰ� ���� ĳ���� ������ �ٷ� view�� �ѱ��

					} else if (cm.code.matches("600")) {
						for (int i = 0; i < RoomEntryPlayerNum; i++) {
							if (PlayerName[i].equals(UserName)) {
								cm.i = i;
							}
						}

						if (cm.UserReady.matches("Ready")) {
							ReadyNum++;
							cm.ReadyNum = ReadyNum;
						} else {
							ReadyNum--;
							cm.ReadyNum = ReadyNum;
						}
						cm.RoomEntryPlayerNum = RoomEntryPlayerNum;
						WriteAllObject(cm);

						if (cm.ReadyNum == 4) {
							m_timer = new Timer();
							m_task = new TimerTask() {
								@Override
								public void run() {
									// TODO Auto-generated method stub

									if (multiNum + oxNum < 11) {
										Answer();
										ChatMsg obcm2 = new ChatMsg("SEVER", "700", "quiz");
										QuizType = (int) (Math.random() * 2 + 1); // ���� ����

										JavaGameServerQuiz view = new JavaGameServerQuiz();

										RealAnswer[0] = 7;
										RealAnswer[1] = 7;
										RealAnswer[2] = 7;
										RealAnswer[3] = 7;
										long starttime = System.currentTimeMillis();
										if (QuizType == 1) {
											String msg2 = view.getQuiz(QuizType, multiNum);
											String c[] = view.getchoice(multiNum);
											answ = view.getAnsw(QuizType, multiNum);
											obcm2.QuizPanel = QuizType;
											obcm2.Quiz = msg2;
											obcm2.choice = c;
											multiNum++;

										} else if (QuizType == 2) {
											String msg2 = view.getQuiz(QuizType, oxNum);
											answ = view.getAnsw(QuizType, oxNum);
											obcm2.QuizPanel = QuizType;
											obcm2.Quiz = msg2;
											oxNum++;
										}

										WriteAllObject(obcm2);
										long endtime = System.currentTimeMillis();
										System.out.println(endtime - starttime);

									} else if (multiNum + oxNum >= 11) { // ���� 5�����ۿ� �ȸ������ ������ �̷������� �ڵ�����
										Answer();
										// m_timer.cancel();
										sortscore[0] = score[0];
										sortscore[1] = score[1];
										sortscore[2] = score[2];
										sortscore[3] = score[3];
										sortPlayerName[0] = PlayerName[0];
										sortPlayerName[1] = PlayerName[1];
										sortPlayerName[2] = PlayerName[2];
										sortPlayerName[3] = PlayerName[3];
										ChatMsg obcm3 = new ChatMsg("SEVER", "850", "End Game");

										// �������� ������ �ȵǼ� ������������ �� ex) 1, 5, 8
										Arrays.sort(sortscore);
										for (int j = 0; j < RoomEntryPlayerNum; j++) {
											// ���� ���� ���� ���
											if (score[j] == sortscore[3])
												sortPlayerName[3] = PlayerName[j];
											else if (score[j] == sortscore[2])
												sortPlayerName[2] = PlayerName[j];
											else if (score[j] == sortscore[1])
												sortPlayerName[1] = PlayerName[j];
											else if (score[j] == sortscore[0])
												sortPlayerName[0] = PlayerName[j];
										}

										// ���ʴ�� ���� �־��ֱ�
										// ������ �ϸ� score[0] ~ score[3] �� �������� ������������ ���� �ٲ��
										obcm3.EndScore[0] = sortscore[3];
										obcm3.EndScore[1] = sortscore[2];
										obcm3.EndScore[2] = sortscore[1];
										obcm3.EndScore[3] = sortscore[0];
										obcm3.EndName[0] = sortPlayerName[3];
										obcm3.EndName[1] = sortPlayerName[2];
										obcm3.EndName[2] = sortPlayerName[1];
										obcm3.EndName[3] = sortPlayerName[0];

										obcm3.LoginPlayer[0] = LoginPlayer[0];
										obcm3.LoginPlayer[1] = LoginPlayer[1];
										obcm3.LoginPlayer[2] = LoginPlayer[2];
										obcm3.LoginPlayer[3] = LoginPlayer[3];
										obcm3.PlayerCoin[0] = PlayerCoin[0];
										obcm3.PlayerCoin[1] = PlayerCoin[1];
										obcm3.PlayerCoin[2] = PlayerCoin[2];
										obcm3.PlayerCoin[3] = PlayerCoin[3];
										for (int j = 0; j < 4; j++) {
											if (sortPlayerName[3].equals(LoginPlayer[j])) {
												PlayerCoin[j]++;
											} else if (sortscore[3] == sortscore[2]
													&& sortPlayerName[2].equals(LoginPlayer[j])) {
												PlayerCoin[j]++;
											} else if (sortscore[3] == sortscore[1]
													&& sortPlayerName[1].equals(LoginPlayer[j])) {
												PlayerCoin[j]++;
											} else if (sortscore[3] == sortscore[0]
													&& sortPlayerName[0].equals(LoginPlayer[j])) {
												PlayerCoin[j]++;
											}

										}

										obcm3.PlayerCoin[0] = PlayerCoin[0];
										obcm3.PlayerCoin[1] = PlayerCoin[1];
										obcm3.PlayerCoin[2] = PlayerCoin[2];
										obcm3.PlayerCoin[3] = PlayerCoin[3];
										WriteAllObject(obcm3);
										m_task.cancel();
										m_timer.cancel(); // Ÿ�̸� ������
									}
								}
							};
							m_timer.schedule(m_task, 0, 13000);
						}

					} else if (cm.code.matches("750")) { // ���� ó��
						System.out.println(UserName + "���� ������ ��" + cm.Answer);
						if (PlayerName[0] == UserName) {
							RealAnswer[0] = cm.Answer;
						} else if (PlayerName[1] == UserName) {
							RealAnswer[1] = cm.Answer;
						} else if (PlayerName[2] == UserName) {
							RealAnswer[2] = cm.Answer;
						} else if (PlayerName[3] == UserName) {
							RealAnswer[3] = cm.Answer;
						}
					} else if (cm.code.matches("900")) {
						cm.LoginPlayer[0] = LoginPlayer[0];
						cm.LoginPlayer[1] = LoginPlayer[1];
						cm.LoginPlayer[2] = LoginPlayer[2];
						cm.LoginPlayer[3] = LoginPlayer[3];
						if (UserName.equals(LoginPlayer[0]) && PlayerCoin[0] >= 10) {
							cm.PlayerCoin[0] = PlayerCoin[0] - cm.ToonPrice;
							PlayerCoin[0] = cm.PlayerCoin[0];
							cm.PlayerCoin[1] = PlayerCoin[1];
							cm.PlayerCoin[2] = PlayerCoin[2];
							cm.PlayerCoin[3] = PlayerCoin[3];
							WriteAllObject(cm);
						} else if (UserName.equals(LoginPlayer[1]) && PlayerCoin[1] >= 10) {
							cm.PlayerCoin[0] = PlayerCoin[0];
							cm.PlayerCoin[1] = PlayerCoin[1] - cm.ToonPrice;
							PlayerCoin[1] = cm.PlayerCoin[1];
							cm.PlayerCoin[2] = PlayerCoin[2];
							cm.PlayerCoin[3] = PlayerCoin[3];
							WriteAllObject(cm);
						} else if (UserName.equals(LoginPlayer[2]) && PlayerCoin[2] >= 10) {
							cm.PlayerCoin[0] = PlayerCoin[0];
							cm.PlayerCoin[1] = PlayerCoin[1];
							cm.PlayerCoin[2] = PlayerCoin[2] - cm.ToonPrice;
							PlayerCoin[2] = cm.PlayerCoin[2];
							cm.PlayerCoin[3] = PlayerCoin[3];
							WriteAllObject(cm);
						} else if (UserName.equals(LoginPlayer[3]) && PlayerCoin[3] >= 10) {
							cm.PlayerCoin[0] = PlayerCoin[0];
							cm.PlayerCoin[1] = PlayerCoin[1];
							cm.PlayerCoin[2] = PlayerCoin[2];
							cm.PlayerCoin[3] = PlayerCoin[3] - cm.ToonPrice;
							PlayerCoin[3] = cm.PlayerCoin[3];
							WriteAllObject(cm);
						} else {
							System.out.println("���� ����");
						}
					} else {
						WriteAllObject(cm);
					}

				} catch (

				IOException e) {
					AppendText("ois.readObject() error");
					try {
//                  dos.close();
//                  dis.close();
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}

}