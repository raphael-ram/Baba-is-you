package fr.umlv.babaisyou.web.dto;

import java.util.List;

public record GameStateDto(
    String status,   // "continue" | "win" | "loose"
    int level,
    int rows,
    int cols,
    List<List<List<String>>> grid  // [row][col][cellStack] — bottom to top
) {}
