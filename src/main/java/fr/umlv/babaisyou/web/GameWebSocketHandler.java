package fr.umlv.babaisyou.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.umlv.babaisyou.web.dto.GameAction;
import fr.umlv.babaisyou.web.dto.GameStateDto;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameSessionService sessionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameWebSocketHandler(GameSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GameAction action = objectMapper.readValue(message.getPayload(), GameAction.class);
        GameStateDto state = switch (action.type()) {
            case "LOAD_LEVEL" -> sessionService.loadLevel(session.getId(), action.level());
            case "MOVE"       -> sessionService.move(session.getId(), action.direction());
            case "RESTART"    -> sessionService.restart(session.getId());
            default           -> null;
        };
        if (state != null) {
            send(session, state);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionService.removeSession(session.getId());
    }

    private void send(WebSocketSession session, GameStateDto state) throws Exception {
        String json = objectMapper.writeValueAsString(state);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }
}
