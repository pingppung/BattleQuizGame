package gamepkg.network;

import gamepkg.ServerWindow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//클라이언트 연결을 수락하고, 각 클라이언트에 대해 UserService를 시작
public class AcceptServer extends Thread {
    private ServerSocket serverSocket;
    private UserManager userManager;
    private ServerWindow serverUI;

    public AcceptServer(ServerSocket serverSocket, UserManager userManager, ServerWindow serverUI) {
        this.serverSocket = serverSocket;
        this.userManager = userManager;
        this.serverUI = serverUI;
    }

    @Override
    public void run() {
        while (true) {
            try {
            	//serverUI.appendText("새로운 플레이어를 기다리는 중입니다.");
                Socket clientSocket = serverSocket.accept();
                //serverUI.appendText("새로운 플레이어가 입장했습니다.");
                UserService userService = new UserService(clientSocket, userManager, serverUI); //Player 당 하나의 Thread 생성
                userManager.addUser(userService); //새로운 참가자 배열에 추가
                userService.start();
            } catch (IOException e) {
                e.printStackTrace();
                serverUI.appendText("플레이어 연결 실패!!! error: " + e.getMessage());
            }
        }
    }
}
