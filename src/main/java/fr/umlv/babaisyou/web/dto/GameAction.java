package fr.umlv.babaisyou.web.dto;

public record GameAction(String type, String direction, Integer level) {
    // type: "LOAD_LEVEL" | "MOVE" | "RESTART"
    // direction: "UP" | "DOWN" | "LEFT" | "RIGHT" (only for MOVE)
    // level: 0-7 (only for LOAD_LEVEL)
}
