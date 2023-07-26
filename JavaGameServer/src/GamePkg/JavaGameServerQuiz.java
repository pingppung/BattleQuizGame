package GamePkg;

public class JavaGameServerQuiz {
	String type; // 객관식 퀴즈, ox퀴즈, 주관식 등
	String quiz;
	String choice;

	int typeNum;
	int quizNum;
	public String getQuiz(int type, int quizNum) {
		if (type == 1) { //객관식
			switch (quizNum) {
			case 1: // chat message
				return "객1";
			case 2:
				return "객2";
			case 3:
				return "객3";
			case 4:
				return "객4";
			case 5:
				return "객5";
			case 6:
				return "객6";
			default:
				break;
			}
		}if (type == 2) { //OX
			switch (quizNum) {
			case 1: // chat message
				return "ox1";
			case 2:
				return "ox2";
			case 3:
				return "ox3";
			case 4:
				return "ox4";
			case 5:
				return "ox5";
			case 6:
				return "o6";
			default:
				break;
			}
		}
		return null;
	}
	public String[] getchoice(int quizNum) { //객관식 보기
		String[] ans = new String[4];
		switch (quizNum) {
		case 1: // chat message
			ans[0] = "정1";
			ans[1] = "정2";
			ans[2] = "정3";
			ans[3] = "정4";
			return ans;
		case 2:
			ans[0] = "정1";
			ans[1] = "정2";
			ans[2] = "정3";
			ans[3] = "정4";
			return ans;
		case 3:
			ans[0] = "정1";
			ans[1] = "정2";
			ans[2] = "정3";
			ans[3] = "정4";
			return ans;
		case 4:
			ans[0] = "정1";
			ans[1] = "정2";
			ans[2] = "정3";
			ans[3] = "정4";
			return ans;
		case 5:
			ans[0] = "정1";
			ans[1] = "정2";
			ans[2] = "정3";
			ans[3] = "정4";
			return ans;
		case 6:
			ans[0] = "정1";
			ans[1] = "정2";
			ans[2] = "정3";
			ans[3] = "정4";
			return ans;
		default:
			break;

		}
		return null;
	}
	// 정답
		public String getAnsw(int type, int quizNum) {
			// 객관식일 경우
			if (type == 1) {
				switch (quizNum) {
				case 1: 
					return "1";   //그냥 답 번호로 바로 보내기
				case 2:
					return "1";
				case 3:
					return "1";
				case 4:
					return "1";
				case 5:
					return "1";
				case 6:
					return "1";
				default:
					break;
				}
			}
			// ox퀴즈 O는 5 X는 6
			if (type == 2) {
				switch (quizNum) {
				case 1:	 		// chat message
					return "O";
				case 2:
					return "O";
				case 3:
					return "X";
				case 4:
					return "O";
				case 5:
					return "O";
				case 6:
					return "X";
				default:
					break;
				}
			}
		
		return "";
		}
}
