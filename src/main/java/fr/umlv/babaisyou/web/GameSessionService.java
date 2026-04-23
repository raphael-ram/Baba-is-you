package fr.umlv.babaisyou.web;

import fr.umlv.babaisyou.model.Behaviour;
import fr.umlv.babaisyou.model.Direction;
import fr.umlv.babaisyou.model.Grid;
import fr.umlv.babaisyou.web.dto.GameStateDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {

    private static final String[] LEVEL_PATHS = {
        "/levels/level0.txt",
        "/levels/level1.txt",
        "/levels/level2.txt",
        "/levels/level3.txt",
        "/levels/level4.txt",
        "/levels/level5.txt",
        "/levels/level6.txt",
        "/levels/level7.txt",
    };

    private record GameSession(Grid grid, Behaviour behaviour, int level) {}

    private final ConcurrentHashMap<String, GameSession> sessions = new ConcurrentHashMap<>();
    private final GameSerializer serializer;

    public GameSessionService(GameSerializer serializer) {
        this.serializer = serializer;
    }

    public GameStateDto loadLevel(String sessionId, int level) throws IOException {
        if (level < 0 || level >= LEVEL_PATHS.length) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        Grid grid = new Grid();
        grid.launchingData(LEVEL_PATHS[level]);
        Behaviour behaviour = new Behaviour(grid);
        behaviour.processing();
        sessions.put(sessionId, new GameSession(grid, behaviour, level));
        return serializer.serialize(grid, "continue", level);
    }

    public GameStateDto move(String sessionId, String direction) {
        GameSession session = sessions.get(sessionId);
        if (session == null) return null;

        Direction dir = switch (direction) {
            case "UP"    -> Direction.Up;
            case "DOWN"  -> Direction.Down;
            case "LEFT"  -> Direction.Left;
            case "RIGHT" -> Direction.Right;
            default -> null;
        };
        if (dir == null) return null;

        session.behaviour().movementManager(dir);
        String status = session.behaviour().play();

        // Auto-advance: if won and next level exists, load it
        if ("win".equals(status)) {
            int next = session.level() + 1;
            if (next < LEVEL_PATHS.length) {
                try {
                    return loadLevel(sessionId, next);
                } catch (IOException e) {
                    // fallback: return win state
                }
            }
        }
        return serializer.serialize(session.grid(), status, session.level());
    }

    public GameStateDto restart(String sessionId) throws IOException {
        GameSession session = sessions.get(sessionId);
        if (session == null) return null;
        return loadLevel(sessionId, session.level());
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
