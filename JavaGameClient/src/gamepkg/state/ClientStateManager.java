package gamepkg.state;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextPane;

import gamepkg.util.TextHandler;

// 클라이언트 상태 변경을 관찰하는 클래스
public class ClientStateManager {
    private ClientState currentState;
    private TextHandler textHandler;
    private Map<ClientState, JTextPane> stateChatPanes;

    public ClientStateManager(TextHandler textHandler) {
        this.textHandler = textHandler;
        this.stateChatPanes = new HashMap<>();
    }

    public void setState(ClientState newState) {
        this.currentState = newState;
        handleState(newState);
    }

    private void handleState(ClientState newState) {
        JTextPane chatPane = stateChatPanes.get(newState);
        if (chatPane != null) {
            textHandler.setTextPane(chatPane);
        } else {
            // Handle error or log warning
            System.out.println(newState + " 상태에 대한 chatPane이 설정되지 않았습니다.");
        }
    }

    public void setChatPaneForState(ClientState state, JTextPane chatPane) {
        stateChatPanes.put(state, chatPane);
        System.out.println(stateChatPanes.size());
    }
}
