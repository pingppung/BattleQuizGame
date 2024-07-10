package gamepkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameHandler {
    private JavaGameClientRoom gameRoom;

    public GameHandler(JavaGameClientRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public void GameStart() {
        gameRoom.btn_Ready.setVisible(false);
        gameRoom.btn_Exit.setVisible(false);
        initializeTimer();
        initializeQuizPanel();
        updateUserLabels();
    }

    private void initializeTimer() {
        gameRoom.timebar = ComponentFactory.createTimer();
        gameRoom.topPane.add(gameRoom.timebar);
        gameRoom.timebar.setVisible(true);

        gameRoom.lblTime = ComponentFactory.createLabel("10", 200, 10, 50, 39);
        // gameRoom.lblTime.setFont(new Font("배달의민족 도현", Font.PLAIN, 20));
        // gameRoom.lblTime.setHorizontalAlignment(SwingConstants.CENTER);
        gameRoom.topPane.add(gameRoom.lblTime);
        gameRoom.lblTime.setVisible(true);
    }

    private void initializeQuizPanel() {
        gameRoom.QuizPane.setBackground(Color.WHITE);
        gameRoom.QuizPane.setBounds(20, 70, 740, 200);
        gameRoom.QuizPane.setLayout(null);
        gameRoom.QuizPane.setVisible(true);

        gameRoom.lblQuestion.setText("START");
        gameRoom.lblQuestion.setFont(new Font("배달의민족 도현", Font.PLAIN, 60));
        gameRoom.lblQuestion.setBounds(20, 70, 700, 70);
        gameRoom.lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
        gameRoom.lblQuestion.setVisible(true);
        gameRoom.QuizPane.add(gameRoom.lblQuestion);

        initializeQuizButtons();
        initializeOXButtons();

        gameRoom.contentPane.add(gameRoom.QuizPane);
    }

    private void initializeQuizButtons() {
        for (int i = 0; i < 4; i++) {
            gameRoom.btn_quizV[i] = new JButton(String.valueOf(i));
            gameRoom.btn_quizV[i].setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
            if (i < 2) {
                gameRoom.btn_quizV[i].setBounds(30 + i * 360, 70, 315, 50);
            } else {
                gameRoom.btn_quizV[i].setBounds(30 + (i - 2) * 360, 130, 315, 50);
            }
            gameRoom.btn_quizV[i].addActionListener(new QuizButtonActionListener());
            gameRoom.QuizPane.add(gameRoom.btn_quizV[i]);
            gameRoom.btn_quizV[i].setVisible(false);
        }
    }

    private void initializeOXButtons() {
        for (int i = 0; i < 2; i++) {
            if (i == 0)
                gameRoom.btn_OX[i] = new JButton("O");
            if (i == 1)
                gameRoom.btn_OX[i] = new JButton("X");
            gameRoom.btn_OX[i].setFont(new Font("나눔스퀘어", Font.PLAIN, 60));
            gameRoom.btn_OX[i].setBounds(30 + i * 360, 70, 315, 100);
            gameRoom.btn_OX[i].addActionListener(new QuizButtonActionListener());
            gameRoom.QuizPane.add(gameRoom.btn_OX[i]);
            gameRoom.btn_OX[i].setVisible(false);
        }
    }

    private void updateUserLabels() {
        for (int i = 0; i < 4; i++) {
            gameRoom.lblUserReady[i].setVisible(false);
            gameRoom.lblScore[i].setVisible(true);
        }
    }

    // 이벤트 리스너 클래스 정의
    private class QuizButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JavaGameClientRoom.ans = e.getActionCommand();
        }
    }
}
