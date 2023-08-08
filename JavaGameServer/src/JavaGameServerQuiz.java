
public class JavaGameServerQuiz {

	String type; // 객관식 퀴즈, ox퀴즈 등
	int anws;
	String quiz;
	String choice;

	int typeNum;
	int quizNum;

	public String getQuiz(int type, int quizNum) {
		if (type == 1) {
			switch (quizNum) {
			case 1: // chat message
				return "네트워크 프로그래밍 교수님 성함은?";
			case 2:
				return "한성대학교에 없는 건물이름은?";
			case 3:
				return "한성대학교에서 제일 높은 건물 층수는?";
			case 4:
				return "한성대학교 마스코트는?";
			case 5:
				return "공과대학에서 없는 트랙은?";
			case 6:
				return "16의 제곱은?";
			default:
				break;
			}
		}
		if (type == 2) {
			switch (quizNum) {
			case 1: // chat message
				return "네트워크 다 할 수 있을까?";
			case 2:
				return "오늘 4시 안에 잘 수 있을까?";
			case 3:
				return "고래는 알을 낳는다.";
			case 4:
				return "나무늘보는 지문이 있다.";
			case 5:
				return "강아지는 열이나면 발바닥을 확인하면 된다.";
			case 6:
				return "스위스의 수도는 취리히다.";
			default:
				break;
			}
		}

		return null;
	}

	// choice에서만 사용 -> 객관식 보기
	public String[] getchoice(int quizNum) {
		String[] a = new String[4];
		switch (quizNum) {
		case 1: // chat message
			a[0] = "정인환 교수님";
			a[1] = "황기태 교수님";
			a[2] = "김영웅 교수님";
			a[3] = "장재영 교수님";
			return a;
		case 2:
			a[0] = "미래관";
			a[1] = "우총관";
			a[2] = "지선관";
			a[3] = "창의관";
			return a;
		case 3:
			a[0] = "13층";
			a[1] = "11층";
			a[2] = "12층";
			a[3] = "15층";
			return a;
		case 4:
			a[0] = "거북이";
			a[1] = "한성부기";
			a[2] = "상상부기";
			a[3] = "부기";
			return a;
		case 5:
			a[0] = "디지털콘텐츠 가상현실트랙";
			a[1] = "기계자동화트랙";
			a[2] = "지능시스템트랙";
			a[3] = "생산물류시스템트랙";
			return a;
		case 6:
			a[0] = "256";
			a[1] = "216";
			a[2] = "196";
			a[3] = "286";
			return a;
		default:
			break;

		}
		return null;
	}

	// 정답
	public int getAnsw(int type, int quizNum) {
		// 객관식일 경우
		if (type == 1) {
			switch (quizNum) {
			case 1: 
				return 1;   //그냥 답 번호로 바로 보내기
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 3;
			case 5:
				return 2;
			case 6:
				return 1;
			default:
				break;
			}
		}
		// ox퀴즈 O는 5 X는 6
		if (type == 2) {
			switch (quizNum) {
			case 1:	 		// chat message
				return 6;
			case 2:
				return 6;
			case 3:
				return 6;
			case 4:
				return 5;
			case 5:
				return 5;
			case 6:
				return 5;
			default:
				break;
			}
		}
	
	return 0;
	}
}
