package org.example.parser.utils;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.example.parser.gen.org.example.parser.WhileGrammarBaseListener;
import org.example.parser.gen.org.example.parser.WhileGrammarLexer;
import org.example.parser.gen.org.example.parser.WhileGrammarParser;

import java.util.ArrayList;
import java.util.List;

public class ANTLRParser {

    public static class SyntaxError {
        public final int line;
        public final int position;
        public final String message;

        public SyntaxError(int line, int position, String message) {
            this.line = line;
            this.position = position;
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("Line %d:%d - %s", line, position, message);
        }
    }

    public static class ParseResult {
        public final boolean success;
        public final List<SyntaxError> errors;
        public final String parseTree;
        public final String details;

        public ParseResult(boolean success, List<SyntaxError> errors, String parseTree, String details) {
            this.success = success;
            this.errors = errors;
            this.parseTree = parseTree;
            this.details = details;
        }
    }

    public ParseResult parse(String input) {
        List<SyntaxError> errors = new ArrayList<>();
        StringBuilder details = new StringBuilder();

        try {
            // Создаем лексер и парсер
            CharStream charStream = CharStreams.fromString(input);
            WhileGrammarLexer lexer = new WhileGrammarLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            WhileGrammarParser parser = new WhileGrammarParser(tokens);

            // Убираем стандартные обработчики ошибок и добавляем свой
            parser.removeErrorListeners();
            lexer.removeErrorListeners();

            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    errors.add(new SyntaxError(line, charPositionInLine, msg));
                }
            });

            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    errors.add(new SyntaxError(line, charPositionInLine, "Lexer: " + msg));
                }
            });

            // Запускаем парсинг
            details.append("Starting ANTLR parsing...\n");
            ParseTree tree = parser.program();

            if (errors.isEmpty()) {
                details.append("✓ Parsing completed successfully\n");
                details.append("Tokens processed: ").append(tokens.size()).append("\n");

                // Анализируем дерево
                analyzeParseTree(tree, parser, details);
            } else {
                details.append("✗ Parsing completed with ").append(errors.size()).append(" errors\n");
            }

            // Формируем строковое представление дерева
            String treeString = tree.toStringTree(parser);

            return new ParseResult(errors.isEmpty(), errors, treeString, details.toString());

        } catch (Exception e) {
            errors.add(new SyntaxError(0, 0, "ANTLR parsing failed: " + e.getMessage()));
            return new ParseResult(false, errors, "", "ANTLR parsing crashed: " + e.getMessage());
        }
    }

    private void analyzeParseTree(ParseTree tree, WhileGrammarParser parser, StringBuilder details) {
        ParseTreeWalker walker = new ParseTreeWalker();

        details.append("\nParse Tree Structure:\n");
        details.append("----------------------\n");

        walker.walk(new WhileGrammarBaseListener() {
            private int indent = 0;

            private String getIndent() {
                return "  ".repeat(indent);
            }

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                String ruleName = parser.getRuleNames()[ctx.getRuleIndex()];
                details.append(getIndent())
                        .append("→ ")
                        .append(ruleName)
                        .append("\n");
                indent++;
            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                indent--;
            }

            @Override
            public void visitTerminal(TerminalNode node) {
                String tokenText = node.getText();
                if (!tokenText.trim().isEmpty()) {
                    details.append(getIndent())
                            .append("• '")
                            .append(tokenText)
                            .append("'\n");
                }
            }
        }, tree);
    }

    public String parseAndFormat(String input) {
        ParseResult result = parse(input);

        StringBuilder output = new StringBuilder();

        if (result.success) {
            output.append(" SUCCESS: No syntax errors found\n\n");
        } else {
            output.append(" ERRORS: ").append(result.errors.size()).append(" syntax errors found\n");
            for (SyntaxError error : result.errors) {
                output.append("   - ").append(error).append("\n");
            }
            output.append("\n");
        }
        return output.toString();
    }
}