package gamepkg;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gamepkg.network.ServerSocketHandler;
import gamepkg.util.ChatMsg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

//서버의 UI와 관련된 코드를 처리
public class ServerWindow extends JFrame {
    private JPanel contentPane;
    private JTextArea textArea;
    private JTextField txtPortNumber;
    private JButton btnServerStart;
    private ServerSocketHandler serverSocketHandler;
    
    public ServerWindow() {
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

        btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(createStartServerAction());
        btnServerStart.setBounds(12, 356, 300, 35);
        contentPane.add(btnServerStart);
    }

    private ActionListener createStartServerAction() {
        return e -> {
            if (serverSocketHandler != null) {
                int port = Integer.parseInt(txtPortNumber.getText());
                serverSocketHandler.startServer(port);
                btnServerStart.setText("Server Running...");
                btnServerStart.setEnabled(false);
                txtPortNumber.setEnabled(false);
            }
        };
    }

    public void appendText(String message) {
        textArea.append(message + "\n");
    }
    
    public void appendObject(ChatMsg msg) {
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.username + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
    public void setServerSocketHandler(ServerSocketHandler handler) {
        this.serverSocketHandler = handler;
    }
}
