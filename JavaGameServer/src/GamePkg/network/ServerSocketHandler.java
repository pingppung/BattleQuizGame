package gamepkg.network;

import gamepkg.GameManager;
import gamepkg.ServerWindow;
import gamepkg.player.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// 서버 소켓을 설정하고 클라이언트의 연결을 관리
public class ServerSocketHandler {
    private ServerSocket serverSocket;
    private ServerWindow serverUI;
    private UserManager userManager;

    public ServerSocketHandler(ServerWindow serverUI, UserManager userManager) {
        this.serverUI = serverUI;
        this.userManager = userManager;
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverUI.appendText("Server started on port " + port);
            new AcceptServer(serverSocket, userManager, serverUI).start();
        } catch (IOException e) {
            e.printStackTrace();
            serverUI.appendText("Server error: " + e.getMessage());
        }
    }
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                serverUI.appendText("Server stopped.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
