package gamepkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class ComponentFactory {
    public static JPanel createImagePanel(String path) {
        ImageIcon face = new ImageIcon(path);
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                Dimension d = getSize();
                g.drawImage(face.getImage(), 0, 0, d.width, d.height, this);
                setOpaque(false);
                super.paintComponent(g);
            }
        };
    }

    public static JPanel createColorPanel(Color c, int x, int y, int w, int h) {
        JPanel pane = new JPanel();
        pane.setBackground(c);
        pane.setBounds(x, y, w, h);
        return pane;
    }

    public static JScrollPane createScrollPanel(int x, int y, int w, int h) {
        JTextPane textArea = new JTextPane();
        textArea.setEditable(false); // 편집 불가능 설정
        textArea.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(x, y, w, h);
        return scrollPane;
    }

    public static JButton createTextButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setFont(new Font("배달의민족 도현", Font.PLAIN, 12));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBounds(x, y, w, h);
        return button;
    }

    public static JButton createImageButton(ImageIcon normalIcon, ImageIcon rolloverIcon, ImageIcon pressedIcon, int x,
            int y, int w, int h) {
        JButton button = new JButton(normalIcon);
        button.setPressedIcon(pressedIcon);
        button.setRolloverIcon(rolloverIcon);
        button.setBounds(x, y, w, h);
        return button;
    }

    public static JLabel createLabel(String text, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        //label.setFont(new Font("배달의민족 도현", Font.PLAIN, 15));
        label.setBackground(Color.WHITE);
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(x, y, w, h);
        return label;
    }

    public static JTextField createTextField(String fontName, int x, int y, int w, int h, String defaultText) {
        JTextField textField = new JTextField();
        textField.setFont(new Font(fontName, Font.PLAIN, 12));
        textField.setBackground(Color.WHITE);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setText(defaultText);
        textField.setColumns(10);
        textField.setBounds(x, y, w, h);
        return textField;
    }

    public static JProgressBar createTimer() {
        JProgressBar bar = new JProgressBar();
        bar.setForeground(new Color(153, 153, 204));
        bar.setValue(10);
        bar.setMaximum(10);
        bar.setBounds(250, 15, 309, 30);
        return bar;
    }
}
