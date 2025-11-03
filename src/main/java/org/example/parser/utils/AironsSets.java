package org.example.parser.utils;

import java.util.Set;

public class AironsSets {
    // Синхронизационные множества для метода Айронса
    private final Set<TokenType> syncW = Set.of(TokenType.EOF);
    private final Set<TokenType> syncC = Set.of(TokenType.RPAREN, TokenType.LBRACE);
    private final Set<TokenType> syncE = Set.of(TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncT = Set.of(TokenType.ADD, TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncO = Set.of(TokenType.ADD, TokenType.MULTIPLY, TokenType.RPAREN, TokenType.SEMICOLON, TokenType.RBRACE, TokenType.COMPARE);
    private final Set<TokenType> syncSL = Set.of(TokenType.RBRACE);
    private final Set<TokenType> syncS = Set.of(TokenType.RBRACE, TokenType.SEMICOLON);

//
//    public static Set<TokenType> getSyncSet(String notTerminal) {
//
//    }
}
