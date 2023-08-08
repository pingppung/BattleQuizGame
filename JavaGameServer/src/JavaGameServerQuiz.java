
public class JavaGameServerQuiz {

	String type; // ������ ����, ox���� ��
	int anws;
	String quiz;
	String choice;

	int typeNum;
	int quizNum;

	public String getQuiz(int type, int quizNum) {
		if (type == 1) {
			switch (quizNum) {
			case 1: // chat message
				return "��Ʈ��ũ ���α׷��� ������ ������?";
			case 2:
				return "�Ѽ����б��� ���� �ǹ��̸���?";
			case 3:
				return "�Ѽ����б����� ���� ���� �ǹ� ������?";
			case 4:
				return "�Ѽ����б� ������Ʈ��?";
			case 5:
				return "�������п��� ���� Ʈ����?";
			case 6:
				return "16�� ������?";
			default:
				break;
			}
		}
		if (type == 2) {
			switch (quizNum) {
			case 1: // chat message
				return "��Ʈ��ũ �� �� �� ������?";
			case 2:
				return "���� 4�� �ȿ� �� �� ������?";
			case 3:
				return "���� ���� ���´�.";
			case 4:
				return "�����ú��� ������ �ִ�.";
			case 5:
				return "�������� ���̳��� �߹ٴ��� Ȯ���ϸ� �ȴ�.";
			case 6:
				return "�������� ������ �븮����.";
			default:
				break;
			}
		}

		return null;
	}

	// choice������ ��� -> ������ ����
	public String[] getchoice(int quizNum) {
		String[] a = new String[4];
		switch (quizNum) {
		case 1: // chat message
			a[0] = "����ȯ ������";
			a[1] = "Ȳ���� ������";
			a[2] = "�迵�� ������";
			a[3] = "���翵 ������";
			return a;
		case 2:
			a[0] = "�̷���";
			a[1] = "���Ѱ�";
			a[2] = "������";
			a[3] = "â�ǰ�";
			return a;
		case 3:
			a[0] = "13��";
			a[1] = "11��";
			a[2] = "12��";
			a[3] = "15��";
			return a;
		case 4:
			a[0] = "�ź���";
			a[1] = "�Ѽ��α�";
			a[2] = "���α�";
			a[3] = "�α�";
			return a;
		case 5:
			a[0] = "������������ ��������Ʈ��";
			a[1] = "����ڵ�ȭƮ��";
			a[2] = "���ɽý���Ʈ��";
			a[3] = "���깰���ý���Ʈ��";
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

	// ����
	public int getAnsw(int type, int quizNum) {
		// �������� ���
		if (type == 1) {
			switch (quizNum) {
			case 1: 
				return 1;   //�׳� �� ��ȣ�� �ٷ� ������
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
		// ox���� O�� 5 X�� 6
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
