package gamepkg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import gamepkg.network.UserManager;
import gamepkg.player.Player;
import gamepkg.player.PlayerStatus;
import gamepkg.room.Room;
import gamepkg.util.ChatMsg;
import gamepkg.util.PlayerRank;

//게임 로직을 처리
public class GameManager {
	private static final int[] ADD_COINS = { 10, 5, 2, 0 }; // 순위에 따른 코인 보상

	private QuizData quiz = new QuizData();
	private Map<String, Integer> rank = new HashMap<>();
	private Room room;

	public GameManager(Room room) {
		this.room = room; 
	}

	public boolean allPlayersReady() {
		List<Player> players = room.getPlayerList();
		if (players.size() != 4) { // 4명이 다 들어와야 함
	        return false;
	    }
		for (Player player : players) {
			if (player.getStatus() != PlayerStatus.Status.Ready) {
				return false;
			}
		}
		return true;
	}

	public void startGame() {
		System.out.println(room.getId() + "번 방 게임 시작!");
		ChatMsg obcm = new ChatMsg("SERVER", "600", "GameStart");
		room.sendMessageToAll(obcm);
		startRandomQuiz();
	}

	public void startRandomQuiz() {
		Timer timer = new Timer(true);
		TimerTask m_task = new TimerTask() {
			int MCQ_count = 1, OX_count = 1;

			@Override

			public void run() {
				ChatMsg obcm1 = null;
				if (MCQ_count + OX_count <= 4) {
					obcm1 = new ChatMsg("SERVER", "650", "Question");
					obcm1.quiz.clear();
					int QuizType = (int) (Math.random() * 2 + 1); // 1 - 객관식, 2- ox

					if (QuizType == 1) {
						String q = quiz.getQuiz(QuizType, MCQ_count); // 문제
						String view[] = quiz.getchoice(MCQ_count); // 보기
						int ans = Integer.valueOf(quiz.getAnsw(QuizType, MCQ_count));// 정답번호
						// List<String> view = Arrays.asList(quiz.getchoice(MCQ_count));
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
					obcm1 = new ChatMsg("SERVER", "750", "GameOver");// 중지
					timer.cancel();

				}

				room.sendMessageToAll(obcm1);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(m_task, 0, 17000);


    }

	public void handleQuizScoreCalculation(String playerName, int score) {
		// 플레이어들은 본인 점수를 보내고 계산 
		addPlayerScore(playerName, score);

		// 모든 플레이어의 점수가 도착했는지 확인
		if (rank.size() == room.getPlayerList().size()) {
			List<Player> sortedPlayers = getSortedPlayers();
			distributeScores(sortedPlayers);
		}

	}

	public void addPlayerScore(String playerName, int score) {
		rank.put(playerName, score);
	}

	private List<Player> getSortedPlayers() {
		List<Player> sortedPlayers = new ArrayList<>(room.getPlayerList());
		sortedPlayers.sort((p1, p2) -> rank.get(p2.getName()).compareTo(rank.get(p1.getName())));
		return sortedPlayers;
	}

	private void distributeScores(List<Player> sortedPlayers) {
		int rankIndex = 0;
		Integer prevScore = null;

		ChatMsg obcm = new ChatMsg("SERVER", "750", "Rank");
		// 등수 부여 및 rankList 생성
        List<PlayerRank> rankList = new ArrayList<>();
        
		for (Player player : sortedPlayers) {
			if (!rank.get(player.getName()).equals(prevScore)) {
				rankIndex++;
			}
			int reward = ADD_COINS[rankIndex - 1];
			player.setCoin(player.getCoin() + reward);
			player.setStatus(PlayerStatus.Status.StandBy);
			
			PlayerRank playerRank = new PlayerRank(player.getName(), player.getCoin(), rankIndex);
			rankList.add(playerRank);
            
			prevScore = rank.get(player.getName());
		}
		obcm.rank = rankList;
		room.sendMessageToAll(obcm);
	}  

}
