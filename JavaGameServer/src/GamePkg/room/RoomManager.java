package gamepkg.room;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import gamepkg.player.Player;

public class RoomManager {
    private static List roomList = new ArrayList();
    private static AtomicInteger atomicInteger = new AtomicInteger();

    // 게임입장버튼 누를 때 생성되게
    public static Room createRoom(Player player) {
        int roomId = atomicInteger.incrementAndGet();
        Room room = new Room(roomId, player);
        room.addPlayer(player); // 윗줄 방생성할때 미리 enterRoom 했음
        roomList.add(room);

		System.out.println("게임방 생성!");
        return room;
    }

    public static Room getRoom(Room room) {
        int idx = roomList.indexOf(room);
        if (idx >= 0) {
            return (Room) roomList.get(idx);
        } else {
            return null;
        }
    }

    public static Room getRoom(int idx) { // roomlist 돌면서 4명 아닌 방정보 넘겨줌
        if (idx >= 0) {
            return (Room) roomList.get(idx);
        } else {
            return null;
        }
    }

    public static void removeRoom(Room room) {
        room.closeRoom();
        roomList.remove(room); // 전달받은 룸을 제거한다.

		System.out.println("게임방 제거!");
    }

    public static int roomCount() {
        return roomList.size();
    }
}
