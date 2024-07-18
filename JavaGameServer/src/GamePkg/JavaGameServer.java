package gamepkg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gamepkg.player.Player;
import gamepkg.player.PlayerStatus;
import gamepkg.room.Room;
import gamepkg.room.RoomManager;
import gamepkg.util.ChatMsg;

import javax.swing.Timer;

public class JavaGameServer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	private int roomId = 0;
	private int[] addCoins = { 10, 5, 2, 0 };
	private List<Player> players_list = new ArrayList<>(); // 그냥 접속한 사람들 다 저장

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
		textArea.append("id = " + msg.username + "\n");
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
		public String user_name = "";
		public int room_id;
		// public Status user_status;

		public UserService(Socket client_socket) {
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			// this.room_id = room_id;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			AppendText("새로운 참가자 " + user_name + " 입장.");
			// WriteRoom("Welcome to Java chat server\n");
			WriteRoom(user_name + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
			// WriteRoomObject(msg);
			String msg = "[" + user_name + "]님이 입장 하였습니다.\n";
			WriteRoom(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
		}

		public void Logout() {
			String msg = "[" + user_name + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			// WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			WriteRoom(msg);
			AppendText("사용자 " + "[" + user_name + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.WriteOne(str);
			}
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.WriteOneObject(ob);
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this)
					user.WriteOne(str);
			}
		}

		// 로비의 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteRobbyObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.room_id == 0) { // 방에 입장해있지 않은 상태일 경우
					user.WriteOneObject(ob);
				}
			}
		}

		// 같은 룸에 있는 User들에게 방송. 채팅 message를 보낼 수 있다
		public void WriteRoom(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.room_id == roomId) {
					user.WriteOne(str);
				}
			}
		}

		// 같은 룸에 있는 User들에게 방송(본인 제외). 채팅 message와 player object를 보낼 수 있다
		public void WriteRoomOthers(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.room_id == roomId) {
					user.WriteOneObject(ob);
				}
			}

		}

		public void WriteRoomObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.room_id == roomId) {
					user.WriteOneObject(ob);
				}
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
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public void WriteOne(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public void WritePrivate(String msg) {
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
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		public void WriteOneObject(Object ob) {
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
					e1.printStackTrace();
				}
				Logout();
			}
		}

		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						// AppendObject(cm);
					} else
						continue;
					if (cm.code.matches("100")) {
						user_name = cm.username;
						int port = 0;
						Player player = new Player(user_name, cm.character);
						players_list.add(player);
						WriteOneObject(cm);
					} else if (cm.code.matches("200")) {

						msg = String.format("[%s] %s", cm.username, cm.data);
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args[1].matches("/exit")) {
							Logout();
							break;
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.user_name + "\n");
							}
							WriteOne("-----------------------------\n");
						} else if (args[1].matches("/to")) { // 귓속말
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.user_name.matches(args[2])) {
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
							// WriteAll(msg + "\n"); // Write All
							// WriteAllObject(cm);
							WriteRobbyObject(cm);
						}
					} else if (cm.code.matches("250")) { // 게임방 message
						// ChatMsg obcm1 = new ChatMsg(cm.username, "250", cm.data);
						findRoomId(cm.username);
						// AppendText(cm.username+" "+cm.data);
						WriteRoomObject(cm);
					} else if (cm.code.matches("300")) {// 캐릭터 shop 입장 - 구매했던 캐릭터들 넘기기
						Player player = null;
						for (Player p : players_list) {
							if (p.getName().equals(cm.username)) {
								player = p;
								break;
							}
						}

						if (isNumber(cm.data)) { // 번호면 구매
							if (player.getCoin() >= 10) { // 캐릭터 구매 = 코인 10개 필요
								player.purchaseCoustume(Integer.parseInt(cm.data));
								player.setCoin(player.getCoin() - 10);
								cm.data = "SUCCESS";
							} else { // 코인 부족하여 구매 x
								cm.data = "FAIL";
							}

						} else if (cm.data.equals("Shop")) {// shop입장
							AppendText(cm.username + "님 코인샵 입장");
						} else { // 캐릭터 변경
							player.setCharacter(cm.data);
							cm.data = "CHANGE";
						}
						cm.coin = player.getCoin();
						cm.costume = player.getCoustume().clone();
						WriteOneObject(cm);
					} else if (cm.code.matches("350")) { // 캐릭터 구매 or 변경
						//
					} else if (cm.code.matches("400")) { // exit버튼
						int id = findRoomId(cm.username);

						Room getRoom = getRoomById(id);
						if (getRoom != null) {
							Player player = getRoom.getPlayerByName(cm.username);
							cm.coin = player.getCoin();
							boolean remove = getRoom.exitPlayer(player);
							WriteOneObject(cm);

							if (cm.data.equals("GameFinishExit") && getRoom.getPlayerList() == null) {
								RoomManager.removeRoom(getRoom);
								continue;
							}
							ChatMsg obcm1 = new ChatMsg("SERVER", "450", "changePlayer"); // 방안에 있는 유저들 플레이어리스트 초기화
							WriteRoomObject(obcm1);

							ChatMsg obcm2 = new ChatMsg("SERVER", "500", "changePlayer"); // 다시 플레이어리스트 전달
							List playerlist = getRoom.getPlayerList();
							for (int i = 0; i < playerlist.size(); i++) {
								Player p = (Player) playerlist.get(i);
								obcm2.playerlist.put(p.getName(),
										Arrays.asList(p.getCharacter(), p.getPlayerStatus().toString()));
							}
							setRoomId(0); // 어느 방에도 속해있지 않음을 0으로 표시
							WriteRoomObject(obcm2);

						}

					} else if (cm.code.matches("500")) { // 게임방 입장
						ChatMsg obcm1 = new ChatMsg(cm.username, "500", "playerlist");

						Player player = null;
						for (Player p : players_list) {
							if (p.getName().equals(cm.username)) {
								player = p;
								break;
							}
						}
						player.setSocket(socket);
						player.setPlayerStatus(PlayerStatus.Status.StandBy);
						// roomlist 마지막 방에서 플레이어 4명일 경우 새로 방 만들기
						int roomCnt = RoomManager.roomCount();
						Room room = null;
						if (roomCnt == 0) { // 게임방이 아무것도 없을때 or 방이 있는데 4명으로 정원초과일때
							room = RoomManager.createRoom(player);
						} else {
							boolean newRoom = false; // 새로운 방을 만들어야하는지
							for (int i = 0; i < roomCnt; i++) {
								room = RoomManager.getRoom(i);
								if (room.getPlayerSize() <= 3) { // 3명 이하일때만 들어가기
									room.enterPlayer(player);
									newRoom = false;
									break;
								} else {
									newRoom = true;
								}
							}
							if (newRoom) { // 룸에 빈자리가 없어 새로운 방을 만들어야하는 경우
								room = RoomManager.createRoom(player);

							}
						}

						// 본인이 속한 방의 유저리스트들을 찾아서 클라이언트에게 넘겨주기
						Room getRoom = RoomManager.getRoom(room);
						room_id = getRoom.getId(); // 입장하는 방의 id를 userservice에 저장
						setRoomId(room_id);

						roomId = room_id; // room에 있는 유저한테 데이터 보낼때 비교할 변수
						List playerlist = getRoom.getPlayerList();

						for (int i = 0; i < playerlist.size(); i++) {
							Player p = (Player) playerlist.get(i);
							// 순서 바뀌는 문제 hashset ->linkedHashset으로 변경해서 해결
							obcm1.playerlist.put(p.getName(),
									Arrays.asList(p.getCharacter(), p.getPlayerStatus().toString()));
						}
						Login();
						WriteRoomObject(obcm1); // 방안에 있는 사람들한테만 전달 - user roomid를 어떻게 넣어줄건지 생각해보기

					} else if (cm.code.matches("550")) { // ready
						// 여기서 해당 유저에 대한 상태를 레디로 바꾸고 방안에 있는 사람들한테 유저 상태 변경 전달
						int id = findRoomId(cm.username);
						ChatMsg obcm1 = new ChatMsg(cm.username, "550", "playerReadyList");
						Room getRoom = getRoomById(id);
						if (getRoom != null) {
							Player player = getRoom.getPlayerByName(cm.username);
							if (cm.data.equals("준비완료")) {
								player.setPlayerStatus(PlayerStatus.Status.Ready);
							} else {
								player.setPlayerStatus(PlayerStatus.Status.StandBy);
							}
						}

						int count = 0;
						List playerlist = getRoom.getPlayerList();
						for (int i = 0; i < playerlist.size(); i++) {
							Player p = (Player) playerlist.get(i);
							if (p.getPlayerStatus().toString().equals("Ready"))
								count++;
							obcm1.playerlist.put(p.getName(),
									Arrays.asList(p.getCharacter(), p.getPlayerStatus().toString()));
						}
						if (count == 4) {
							obcm1.code = "600";
							obcm1.data = "GameStart"; // 게임 시작한다고 클라이언트에게 알리기 "600"
							WriteRoomObject(obcm1);
							RandomQuiz(); // 게임 시작하고 start표시 띄운다음 했으면 좋겠는데 일단 바로 시작

						} else
							WriteRoomObject(obcm1);

					} else if (cm.code.matches("700")) { // 퀴즈 점수 계산
						findRoomId(cm.username);
						AppendText(client_socket.isConnected() + "");
						WriteRoomOthers(cm);

					} else if (cm.code.matches("750")) { // 퀴즈 점수 계산
						int id = findRoomId(cm.username);
						Room getRoom = getRoomById(id);
						int score = Integer.valueOf(cm.data);
						getRoom.putRank(cm.username, score);
						// cm.rank.put(Integer.valueOf(cm.data), cm.username);

						cm.data = "Rank";
						if (getRoom.getRank().size() == 4) {
							// 점수대로 플레이어 등수 정렬
							List<String> list = new ArrayList<>(getRoom.getRank().keySet());
							list.forEach((i) -> AppendText(i));
							// Collections.sort(list, (s1, s2) -> ((Integer)
							// getRoom.getRank().get(s2)).compareTo((Integer)getRoom.getRank().get(s1)));
							Collections.sort(list, (s1, s2) -> ((Integer) getRoom.getRank().get(s2))
									.compareTo((Integer) getRoom.getRank().get(s1)));
							List playerlist = getRoom.getPlayerList();

							int rank = 0;
							Integer a = 0;
							for (String key : list) { // key = 플레이어 이름
								List<Integer> addCoin = new ArrayList<>();
								if (!a.equals(getRoom.getRank().get(key))) {
									rank++;
								}
								addCoin.add(rank);
								addCoin.add(addCoins[rank - 1]);
								cm.rank.put(key, addCoin);
								a = (Integer) getRoom.getRank().get(key);

								// Player마다 coin 저장
								for (int i = 0; i < playerlist.size(); i++) {
									Player p = (Player) playerlist.get(i);
									if (key.equals(p.getName())) {
										p.setCoin(p.getCoin() + addCoins[rank - 1]);
										p.setPlayerStatus(PlayerStatus.Status.StandBy);
										break;
									}
								}
							}

							WriteRoomObject(cm);
						}
					} else { // 기타 object는 모두 방송한다.
						WriteAllObject(cm);
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					}
				}
			}
		}

		public Room getRoomById(int id) {
			int roomCnt = RoomManager.roomCount();
			Room room = null;
			for (int i = 0; i < roomCnt; i++) {
				room = RoomManager.getRoom(i);
				if (room.getId() == id) {
					return room;
				}

			}
			return room;
		}

		public void RandomQuiz() {
			JavaGameServerQuiz quiz = new JavaGameServerQuiz();

			Timer timer = new Timer(1000, new ActionListener() {
				int MCQ_count = 1;
				int OX_count = 1;

				@Override
				public void actionPerformed(ActionEvent e) {
					ChatMsg obcm1 = null;
					if (MCQ_count + OX_count <= 4) {
						obcm1 = new ChatMsg("SERVER", "650", "Question");
						obcm1.quiz.clear();
						int QuizType = (int) (Math.random() * 2 + 1); // 1 - 객관식, 2 - OX

						if (QuizType == 1) {
							String q = quiz.getQuiz(QuizType, MCQ_count); // 문제
							String[] view = quiz.getchoice(MCQ_count); // 보기
							int ans = Integer.valueOf(quiz.getAnsw(QuizType, MCQ_count)); // 정답번호
							List<String> list = new ArrayList<>();
							list.add(q);
							for (String s : view) {
								list.add(s);
							}
							list.add(view[ans - 1]);
							obcm1.quiz.put(QuizType, list);
							MCQ_count++;

						} else if (QuizType == 2) {
							String q = quiz.getQuiz(QuizType, OX_count);
							String ans = String.valueOf(quiz.getAnsw(QuizType, OX_count));
							OX_count++;
							List<String> list = new ArrayList<>();
							list.add(q);
							list.add(ans);
							obcm1.quiz.put(QuizType, list);
						}
					} else {
						obcm1 = new ChatMsg("SERVER", "750", "GameOver"); // 중지
						// timer.stop();
					}

					WriteRoomObject(obcm1);
				}
			});

			timer.start();
		}

		public void setRoomId(int id) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user == this)
					user.room_id = id;
			}
		}

		public int findRoomId(String name) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.user_name.equals(name)) {
					roomId = user.room_id;
					return user.room_id; // 유저가 속한 룸id
				}
			}

			return 0;
		}

		public boolean isNumber(String s) {
			try {
				Integer.parseInt(s);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

}
