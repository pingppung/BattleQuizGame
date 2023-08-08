//JavaObjServer.java ObjectStream 기반 채팅 Server

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

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	// 게임방에 들어가는 player 이름 저장시키는 변수
	private static String[] PlayerName = new String[4];
	// 퀴즈게임 결과대로 순위 결정하는 변수
	private static String[] sortPlayerName = new String[4];
	private static String[] PlayerCharacter = new String[4];
	private int RoomEntryPlayerNum = 0;
	private static String[] PlayerReady = new String[4];
	private int ReadyNum = 0;
	private int QuizType; // 문제 유형
	private int multiNum = 1; // 객관식 문제번호 - 중복될 경우를 대비해서 일단 차례대로 문제 나오게
	private int oxNum = 1; // ox 문제 번호 - 중복될 경우를 대비해서 일단 차례대로 문제 나오게
	private int[] RealAnswer = new int[4];
	private int answ;
	private int[] score = new int[4];
	private int[] sortscore = new int[4];

	private long startTime = 0;
	private long endTime = 0;

	private Timer m_timer;
	private TimerTask m_task;

	private int ShopEntryPlayerNum = 0;
	// 로그인한 플레이어 다 저장시키는 변수
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
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
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
			// 매개변수로 넘어온 자료 저장
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
//            UserStatus = "O"; // Online 상태
//            Login();
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public synchronized void Login() {
			AppendText("새로운 참가자 " + UserName + " 입장.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
			String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
			WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
		}

		public synchronized void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public synchronized void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public synchronized void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public synchronized void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(str);
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
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
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
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public synchronized void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
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
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
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

		// 각 플레이어 별 정답과 오답 처리
		public void Answer() {
			// 첫번째로 게임방에 들어온 플레이어
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
				System.out.println("게임이 시작되었습니다.");
			}

			// 두번째로 게임방에 들어온 플레이어
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
				System.out.println("게임이 시작되었습니다.");
			}

			// 세번째로 게임방에 들어온 플레이어
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
				System.out.println("게임이 시작되었습니다.");
			}

			// 네번째로 게임방에 들어온 플레이어
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
				System.out.println("게임이 시작되었습니다.");
			}

		}

		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
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
//                  } // catch문 끝
//               }
//               String msg = new String(b, "euc-kr");
//               msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거
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
						UserStatus = "O"; // Online 상태
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
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
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
						} else if (args[1].matches("/to")) { // 귓속말
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// 실제 message 부분
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to 빼고.. [귓속말] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									// user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // 일반 채팅 메시지
							UserStatus = "O";
							// WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.code.matches("250")) {
						msg = String.format("[%s] %s", cm.UserName, cm.data);
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
							UserStatus = "O";
						}
						WriteAllObject(cm);
					} else if (cm.code.matches("400")) { // logout message 처리
						Logout();
						break;
					} else if (cm.code.matches("500")) { // 게임방 입장

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
							WriteOne("정원초과되어 입장할 수 없습니다");
						}
					} else if (cm.code.matches("550")) { // 캐릭터 정보 넘기기
						AppendText("a");
						WriteAllObject(cm); // 바꾸고 싶은 캐릭터 정보를 바로 view로 넘기기

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
										QuizType = (int) (Math.random() * 2 + 1); // 문제 유형

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

									} else if (multiNum + oxNum >= 11) { // 현재 5문제밖에 안만들었기 때문에 이런식으로 코딩했음
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

										// 오름차순 정렬이 안되서 내림차순으로 들어감 ex) 1, 5, 8
										Arrays.sort(sortscore);
										for (int j = 0; j < RoomEntryPlayerNum; j++) {
											// 제일 점수 높은 사람
											if (score[j] == sortscore[3])
												sortPlayerName[3] = PlayerName[j];
											else if (score[j] == sortscore[2])
												sortPlayerName[2] = PlayerName[j];
											else if (score[j] == sortscore[1])
												sortPlayerName[1] = PlayerName[j];
											else if (score[j] == sortscore[0])
												sortPlayerName[0] = PlayerName[j];
										}

										// 차례대로 점수 넣어주기
										// 정렬을 하면 score[0] ~ score[3] 의 점수들이 내림차순으로 서로 바뀐다
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
										m_timer.cancel(); // 타이머 끝내기
									}
								}
							};
							m_timer.schedule(m_task, 0, 13000);
						}

					} else if (cm.code.matches("750")) { // 정답 처리
						System.out.println(UserName + "님이 선택한 답" + cm.Answer);
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
							System.out.println("코인 부족");
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
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}

}