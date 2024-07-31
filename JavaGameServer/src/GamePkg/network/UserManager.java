package gamepkg.network;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import gamepkg.room.Room;
import gamepkg.util.ChatMsg;

//사용자 관리 및 메시지 브로드캐스트를 담당
public class UserManager {
	private Vector<UserService> userVec = new Vector<>();

	public void addUser(UserService user) {
		userVec.add(user);
	}

	public void removeUser(UserService user) {
		userVec.remove(user);
	}

	// 본인 포함 모든 유저들에게 전송
	public void writeAll(ChatMsg obj) {
		for (UserService user : userVec) {
			user.write(obj);
		}
	}

	// 본인한테만 전송
	public void writeOne(ChatMsg obj) {
		String sender = obj.username;
		for (UserService user : userVec) {
			if (user.getPlayer().getName().equals(sender)) {
				user.write(obj);
				break;
			}
		}
	}

	// 본인 제외 모든 유저들에게 전송
	public void writeOthers(ChatMsg obj) {
		String sender = obj.username;
		for (UserService user : userVec) {
			if (!user.getPlayer().getName().equals(sender)) {
				user.write(obj);
			}
		}
	}

	// 로비의 모든 User들에게 Object를 전송
	public void writeRobby(ChatMsg obj) {
		for (UserService user : userVec) {
			if (!user.isInRoom()) { // 방에 입장해있지 않은 상태일 경우
				user.write(obj);
			}
		}
	}

	// 같은 게임방에 있는 유저들에게 전송
	public void writeRoom(Room room, ChatMsg obj) {
		for (UserService user : userVec) {
			if (user.isInRoom() && user.getPlayer().getRoom().equals(room)) {
				user.write(obj);
			}
		}
	}

	// 본인제외!!! 같은 게임방에 있는 유저들에게 전송
	public void writeRoomOthers(Room room, ChatMsg obj) {
		String sender = obj.username;
		for (UserService user : userVec) {
			if (user.isInRoom() && user.getPlayer().getRoom().equals(room)
					&& !user.getPlayer().getName().equals(sender)) {
				user.write(obj);
			}
		}
	}
}
