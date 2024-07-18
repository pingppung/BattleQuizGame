package gamepkg.util;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

//채팅
//GUI 업데이트 
public class TextHandler {
	private JTextPane textPane;

	public TextHandler(JTextPane textArea) {
		this.textPane = textArea;
	}

	public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
    }
	
	public void appendIcon(ImageIcon icon) {
		int len = textPane.getDocument().getLength();
		textPane.setCaretPosition(len);
		textPane.insertIcon(icon);
	}

	public void appendText(String msg) {
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", left);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void appendTextR(String msg) {
		msg = msg.trim();
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);
		doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", right);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
