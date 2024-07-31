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

import gamepkg.network.ServerSocketHandler;
import gamepkg.network.UserManager;
import gamepkg.player.Player;
import gamepkg.player.PlayerStatus;
import gamepkg.room.Room;
import gamepkg.room.RoomManager;
import gamepkg.util.ChatMsg;

import javax.swing.Timer;

public class JavaGameServer extends JFrame {
	public static void main(String[] args) {
		// UI 생성
        ServerWindow serverUI = new ServerWindow();
        serverUI.setVisible(true);

        // 서버 매니저 생성 및 UI 연결
        UserManager userManager = new UserManager();
        ServerSocketHandler serverSocketHandler = new ServerSocketHandler(serverUI, userManager);

        // UI에서 서버 시작 버튼 클릭 시 서버 시작
        serverUI.setServerSocketHandler(serverSocketHandler);
	}
}
